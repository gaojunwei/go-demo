package com.gjw.go.common.inline

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val <reified T> T.log: Logger
    inline get() = LoggerFactory.getLogger(T::class.java)