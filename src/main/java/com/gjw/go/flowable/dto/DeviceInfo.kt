package com.gjw.go.flowable.dto

import java.io.Serializable

class DeviceInfo:Serializable {
    var deviceId: String? = null // 设备ID
    var deviceType: DeviceTypeEnums? = null // 设备类型
    var oneDagongzai:List<String> = emptyList() // 设备对应的打工仔
    var oneZongjingli:List<String> = emptyList() // 设备对应的总经理
    var twoDagongzaiList:List<String> = emptyList() // 设备对应的二级打工仔

}