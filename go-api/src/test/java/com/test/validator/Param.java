package com.test.validator;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @author: gaojunwei
 * @Date: 2018/7/25 15:36
 * @Description:
 */
@Data
public class Param {
    @Length(min = 2, max = 3, message = "姓名字符长度必须2~3个字符")
    private String userName;//姓名

    @Min(value = 6, message = "年龄必须在6~10岁范围之内，包含6和10")
    @Max(value = 10, message = "年龄必须在6~10岁范围之内，包含6和10")
    @NotNull(message = "年龄不能为NULL或空字符串")
    private Integer age;//年龄

    @AssertTrue(message = "必须要有爱心")
    @NotNull(message = "爱心选项不能为NULL")
    private Boolean love;//是否有爱心

    @DecimalMin(value = "50.8", message = "血糖不能低于50.8")
    @DecimalMax(value = "100.8", message = "血糖不能高于50.8")
    @NotNull(message = "血糖值不能为NULL")
    private Double bloodSugar;//血糖值

    @Past(message = "创建时间必须小于当前时间")
    @NotNull(message = "档案创建时间不能为NULL")
    private Date createdDate;//档案创建时间

    @Future(message = "签证生效必须大于当前时间")
    @NotNull(message = "签证生效时间不能为NULL")
    private Date visaDate;//签证生效时间

    @NotEmpty(message = "邮箱地址不能为空")
    private String email;//邮箱地址

    @Pattern(regexp = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", message = "备用邮箱地址格式错误")
    @NotNull(message = "备用邮箱地址不能为NULL")
    private String emailBack;//备用邮箱地址

    @Range(min = 1, max = 2, message = "准备几年内结婚必须在1~2年范围之内")
    @NotNull(message = "准备几年内结婚不能为NULL")
    private String marryYears;//准备几年内结婚

    @URL(host = "www.jd.com", protocol = "http", message = "网址格式错误")//自定义验证
    private String url;//网址地址

    //@Money(message = "存款金额不合法")//自定义验证
    private Double haveMoney;//有多少存款
}