package com.gjw.go.config.handler

import cn.hutool.core.util.ObjectUtil
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.gjw.go.domain.entity.BaseEntity
import org.apache.ibatis.reflection.MetaObject
import java.util.*

class CreateAndUpdateMetaObjectHandler : MetaObjectHandler {
    override fun insertFill(metaObject: MetaObject) {
        if (ObjectUtil.isNotNull(metaObject) && metaObject.originalObject is BaseEntity) {
            val baseEntity: BaseEntity = metaObject.originalObject as BaseEntity
            val current = baseEntity.createTime ?: Date()
            val username = baseEntity.updateBy ?: "sys"
            baseEntity.createTime = current
            baseEntity.updateTime = current
            // 当前已登录 且 创建人为空 则填充
            baseEntity.createBy = username
            // 当前已登录 且 更新人为空 则填充
            baseEntity.updateBy = username
        }
    }

    override fun updateFill(metaObject: MetaObject) {
        if (ObjectUtil.isNotNull(metaObject) && metaObject.originalObject is BaseEntity) {
            val baseEntity: BaseEntity = metaObject.originalObject as BaseEntity
            val current = Date()
            // 更新时间填充(不管为不为空)
            baseEntity.updateTime = current
            val username: String = "sys_update"
            baseEntity.updateBy = username
        }
    }
}