package it.polito.wa2.g34.server.observability

import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationHandler
import io.micrometer.observation.aop.ObservedAspect

abstract class AbstractObserveAroundMethodHandler : AbstractLogger(), ObservationHandler<ObservedAspect.ObservedAspectContext> {

    override fun onStart(context: ObservedAspect.ObservedAspectContext) {
        val joinPoint = context.proceedingJoinPoint;
        super.logBefore(joinPoint)
    }

    override fun onStop(context: ObservedAspect.ObservedAspectContext) {
        val joinPoint = context.proceedingJoinPoint;
        super.logAfter(joinPoint)
    }
    override fun supportsContext(context: Observation.Context): Boolean {
        return context is ObservedAspect.ObservedAspectContext
    }


}