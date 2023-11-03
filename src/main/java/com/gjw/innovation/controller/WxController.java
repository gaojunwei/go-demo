package com.gjw.innovation.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.gjw.common.enums.CacheKey;
import com.gjw.common.result.SingleResult;
import com.gjw.common.result.cache.MapCache;
import com.gjw.innovation.dto.WxLoginOpenIdVo;
import com.gjw.innovation.service.WxUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("wx")
public class WxController {
    @Resource
    private WxUserService wxUserService;


    /**
     * 获取微信授权信息
     */
    @PostMapping
    public SingleResult<WxLoginOpenIdVo> postFun(@RequestBody Map<String, String> param) throws WxErrorException, ExecutionException, InterruptedException {
        log.info("获取微信授权信息 start param:{}", JSON.toJSONString(param));
        return new SingleResult<WxLoginOpenIdVo>().success(wxUserService.wxLogin(param.get("code")));
    }
}
