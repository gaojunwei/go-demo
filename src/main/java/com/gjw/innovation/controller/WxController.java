package com.gjw.innovation.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.gjw.common.enums.CacheKey;
import com.gjw.common.result.SingleResult;
import com.gjw.common.result.cache.MapCache;
import com.gjw.innovation.dto.WxLoginOpenIdVo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("wx")
public class WxController {
    @Resource
    private WxMaService wxMaService;

    /**
     * 获取微信授权信息
     */
    @PostMapping
    public SingleResult<WxLoginOpenIdVo> postFun(@RequestBody Map<String, String> param) throws WxErrorException {
        log.info("获取微信授权信息 start param:{}", JSON.toJSONString(param));
        //获取openId、unionid、session_key
        WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(param.get("code"));
        log.info("获取微信授权信息 getOpenid:{}", sessionInfo.getOpenid());
        log.info("获取微信授权信息 getSessionKey:{}", sessionInfo.getSessionKey());
        log.info("获取微信授权信息 getUnionid:{}", sessionInfo.getUnionid());
        String token = IdUtil.randomUUID();
        //保存到缓存中
        MapCache.put(CacheKey.openid.getValue(),sessionInfo.getOpenid());
        MapCache.put(CacheKey.sessionKey.getValue(),sessionInfo.getSessionKey());
        MapCache.put(CacheKey.token.getValue(),token);

        WxLoginOpenIdVo vo = new WxLoginOpenIdVo(sessionInfo.getOpenid(),sessionInfo.getSessionKey(),token);
        return new SingleResult<WxLoginOpenIdVo>().success(vo);
    }
}
