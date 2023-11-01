package com.gjw.innovation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gjw.innovation.domain.WxUser;
import com.gjw.innovation.dto.WxLoginOpenIdVo;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.concurrent.ExecutionException;

public interface WxUserService extends IService<WxUser> {
    WxLoginOpenIdVo saveOpenid(String code) throws WxErrorException, ExecutionException, InterruptedException;
}
