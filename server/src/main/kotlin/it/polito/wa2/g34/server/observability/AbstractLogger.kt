package it.polito.wa2.g34.server.observability

import org.aspectj.lang.ProceedingJoinPoint
import org.slf4j.LoggerFactory

abstract class AbstractLogger {

    private data class LogInfo(val className: String, val methodName: String, val classType: Class<*>, val args: Array<Any?>)

    open fun logInfoAround(joinPoint: ProceedingJoinPoint): Any {
        val logInfo = getLogInfo(joinPoint)
        val log = LoggerFactory.getLogger(logInfo.classType)
        logBefore(logInfo, log)
        val obj = joinPoint.proceed()
        logAfter(logInfo, log)
        return obj
    }

    private fun getLogInfo(joinPoint: ProceedingJoinPoint) : LogInfo {
        val className = joinPoint.signature.declaringTypeName
        val methodName = joinPoint.signature.name
        val classType = joinPoint.signature.declaringType
        val args = joinPoint.args
        return LogInfo(className, methodName, classType, args)
    }

    private fun logBefore(logInfo: LogInfo, log: org.slf4j.Logger) {
        log.info("[${logInfo.className}.${logInfo.methodName}] start (${logInfo.args.map { it.toString() }})")
    }
    fun logBefore(joinPoint: ProceedingJoinPoint) {
        val logInfo = getLogInfo(joinPoint)
        val logger = LoggerFactory.getLogger(logInfo.classType)
        logBefore(logInfo, logger);
    }

    private fun logAfter(logInfo: LogInfo, log: org.slf4j.Logger) {
        log.info("[${logInfo.className}.${logInfo.methodName}] end")
    }

    fun logAfter(joinPoint: ProceedingJoinPoint) {
        val logInfo = getLogInfo(joinPoint)
        val logger = LoggerFactory.getLogger(logInfo.classType)
        logAfter(logInfo, logger);
    }

}