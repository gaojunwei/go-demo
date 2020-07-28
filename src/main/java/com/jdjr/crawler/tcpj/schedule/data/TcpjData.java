package com.jdjr.crawler.tcpj.schedule.data;

import lombok.Getter;
import lombok.Setter;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/20 10:15
 **/
@Getter
@Setter
public class TcpjData extends BaseData {
    /**
     * 手机类型（0:爬取列表数据类,1:爬取票据信息类）
     */
    private Integer phoneType;
}
