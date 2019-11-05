package com.gjw.common.innovation.controller;

import com.alibaba.fastjson.JSON;
import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.result.BasicResult;
import com.gjw.common.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统功能
 *
 * @author gaojunwei
 * @date 2019/10/14 14:52
 */
@RestController
@RequestMapping("/sys")
@Slf4j
public class SystemController {

    @RequestMapping("refresh")
    public BasicResult refresh() {
        String logId = UuidUtils.getUUID();
        logger.info("SystemController_refresh start 刷新系统配置 logId:{}", logId);
        BasicResult result = new BasicResult();
        result.setCode(SystemCodeEnums.SUCCESS.getCode());
        result.setMsg(SystemCodeEnums.SUCCESS.getMsg());
        logger.info("SystemController_refresh end 刷新系统配置 logId:{},result:{}", logId, JSON.toJSONString(result));
        return result;
    }
}