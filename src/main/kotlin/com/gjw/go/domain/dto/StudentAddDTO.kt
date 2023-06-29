package com.gjw.go.domain.dto

import com.gjw.go.common.validation.AddGroup
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.Range
import java.time.LocalDate
import javax.validation.constraints.AssertFalse
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import kotlin.math.min

/**
 * 添加学生信息
 */
open class StudentAddDTO {
    //姓名
    @NotEmpty(message = "姓名不能为空", groups = [AddGroup::class])
    @Length(min=2, max = 4, message = "姓名字符长度必须大于等于2或小于等于4",groups = [AddGroup::class])
    var name: String? = null

    //年龄
    @NotNull(message = "年龄不能为空", groups = [AddGroup::class])
    //@Min(value = 6, message = "年龄必须大于等于6岁", groups = [AddGroup::class])
    //@Max(value = 18, message = "年龄必须小于等于18岁", groups = [AddGroup::class])
    @Range(min=6, max = 18, message = "年龄必须大于等于6岁且小于等于18岁之间", groups = [AddGroup::class])
    var age: Int? = null

    //学校
    @NotEmpty(message = "学校不能为空", groups = [AddGroup::class])
    var school: String? = null

    //班级
    @NotEmpty(message = "班级不能为空", groups = [AddGroup::class])
    var clzz: String? = null

    //生日
    @NotNull(message = "生日不能为空", groups = [AddGroup::class])
    var birthDay: LocalDate? = null
}