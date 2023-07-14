package com.gjw.go.common.inline

import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val <reified T> T.log: Logger
    inline get() = LoggerFactory.getLogger(T::class.java)

inline fun KieSession.insertAll(objects: Collection<Any>) {
    objects.forEach { this.insert(it) }
}