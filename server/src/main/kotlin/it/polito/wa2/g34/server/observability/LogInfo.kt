package it.polito.wa2.g34.server.observability

import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy


@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LogInfo