package com.gjw.innovation.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjw.common.utils.ExceptionUtils;
import com.gjw.common.utils.JwtUtils;
import com.gjw.innovation.domain.WxUser;
import com.gjw.innovation.dto.WxLoginOpenIdVo;
import com.gjw.innovation.mapper.WxUserMapper;
import com.gjw.innovation.service.LocalLockService;
import com.gjw.innovation.service.WxUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements WxUserService {
    @Resource
    private WxMaService wxMaService;
    @Resource
    private LocalLockService localLockService;

    private final String wxOpenidLockKey = "wx_get_openid";

    @Override
    public WxLoginOpenIdVo wxLogin(String code) throws WxErrorException, ExecutionException, InterruptedException {
        ReentrantLock lock = localLockService.getReentrantLock(wxOpenidLockKey);
        if (!lock.tryLock(5, TimeUnit.SECONDS)) {
            throw ExceptionUtils.fail("系统繁忙，稍后再试");
        }
        try {
            // 获取openId
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            String token = JwtUtils.createJWT(sessionInfo.getOpenid(),sessionInfo.getOpenid());
            // 保存数据
            if (this.baseMapper.exists(initLambdaQueryWrapper(sessionInfo.getOpenid()))) {
                this.baseMapper.update(new LambdaUpdateWrapper<WxUser>()
                        .set(WxUser::getLoginTime, LocalDateTime.now())
                        .set(WxUser::getSessionKey, sessionInfo.getSessionKey())
                        .eq(WxUser::getOpenId, sessionInfo.getOpenid())
                );
            } else {
                WxUser wxUser = new WxUser();
                wxUser.setOpenId(sessionInfo.getOpenid());
                wxUser.setSessionKey(sessionInfo.getSessionKey());
                wxUser.setLoginTime(LocalDateTime.now());
                wxUser.setCreateTime(LocalDateTime.now());
                wxUser.setCreateBy("0");
                wxUser.setUpdateTime(LocalDateTime.now());
                wxUser.setUpdateBy("0");
                this.baseMapper.insert(wxUser);
            }
            return new WxLoginOpenIdVo(sessionInfo.getOpenid(), token);
        }finally {
            lock.unlock();
        }
    }

    private LambdaQueryWrapper<WxUser> initLambdaQueryWrapper(String openId) {
        return new LambdaQueryWrapper<WxUser>()
                .eq(openId != null, WxUser::getOpenId, openId);
    }
}
