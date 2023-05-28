package it.polito.wa2.g34.server.observability

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect


@Aspect
class DefaultLogger : AbstractLogger() {


    @Around("@annotation(LogInfo) || @within(LogInfo)")
    override fun logInfoAround(joinPoint: ProceedingJoinPoint): Any {
        return super.logInfoAround(joinPoint)
    }
}