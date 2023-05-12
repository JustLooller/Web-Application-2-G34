package it.polito.wa2.g34.server.ticketing.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class TicketAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(TicketNotFoundException::class)
    fun handleTicketNotFoundException(e : TicketNotFoundException) : ProblemDetail{
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
    }

    @ExceptionHandler(IllegalUpdateException::class)
    fun handleIllegalUpdateException(e : IllegalUpdateException) : ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.message!!)
    }

    @ExceptionHandler(TicketBadRequestException::class)
    fun handleTicketBadRequestException(e : TicketBadRequestException) : ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
    }



}


class TicketNotFoundException(message : String) : Exception(message)
class IllegalUpdateException(message : String) : Exception(message)
class TicketBadRequestException(message : String) : Exception(message)