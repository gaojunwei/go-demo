package com.gjw.common.innovation.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author gjw
 * @since 2020-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GoUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String account;

    private String code;

    private Date createdDate;

    /**
     * 是否删除(0未删除，1已删除)
     */
    private Integer delFlag;

    private String email;

    private Date modifiedDate;

    private String password;


}
