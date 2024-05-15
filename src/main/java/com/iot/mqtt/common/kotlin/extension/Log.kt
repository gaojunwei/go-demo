package com.iot.mqtt.common.kotlin.extension

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val <reified T> T.logger: Logger
    inline get() = LoggerFactory.getLogger(T::class.java)
