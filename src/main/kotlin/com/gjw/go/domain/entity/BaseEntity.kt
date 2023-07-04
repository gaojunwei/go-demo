package com.gjw.go.domain.entity

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableLogic
import java.util.*

open class BaseEntity {
    /**
     * 逻辑删除标识（0未删除，1已删除）
     * @see com.gjw.go.common.enums.DelFlagEnums
     */
    @TableLogic
    var delFlag: Char? = null

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    var createBy: String? = null

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    var createTime: Date? = null

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    var updateBy: String? = null

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    var updateTime: Date? = null
}