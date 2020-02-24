package com.gjw.common.innovation.repository.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Renqun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 京东PIN
     */
    private String jdPin;
    /**
     * 人群ID
     */
    private String renQunId;
    /**
     * 人群名称
     */
    private String renQunName;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 命中状态
     */
    private String hitState;
    /**
     * 备注信息
     */
    private String remark;
}
