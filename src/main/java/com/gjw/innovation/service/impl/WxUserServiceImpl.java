package com.gjw.innovation.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjw.common.enums.CacheKey;
import com.gjw.common.result.cache.MapCache;
import com.gjw.innovation.domain.WxUser;
import com.gjw.innovation.dto.WxLoginOpenIdVo;
import com.gjw.innovation.mapper.WxUserMapper;
import com.gjw.innovation.service.LocalLockService;
import com.gjw.innovation.service.WxUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    private String wxOpenidLockKey = "wx_get_openid";

    @Override
    public WxLoginOpenIdVo saveOpenid(String code) throws WxErrorException, ExecutionException, InterruptedException {
        ReentrantLock lock = localLockService.getReentrantLock(wxOpenidLockKey);
        if(!lock.tryLock(5, TimeUnit.SECONDS)){
            throw
        }

        WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
        log.info("获取微信授权信息 getOpenid:{}", sessionInfo.getOpenid());
        log.info("获取微信授权信息 getSessionKey:{}", sessionInfo.getSessionKey());
        log.info("获取微信授权信息 getUnionid:{}", sessionInfo.getUnionid());
        String token = IdUtil.randomUUID();
        // 保存到缓存中


        MapCache.put(CacheKey.openid.getValue(), sessionInfo.getOpenid());
        MapCache.put(CacheKey.sessionKey.getValue(), sessionInfo.getSessionKey());
        MapCache.put(CacheKey.token.getValue(), token);
    }
}
