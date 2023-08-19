package it.polito.wa2.g34.server.sales

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class SaleAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(SaleNotFoundException::class)
    fun handleSaleNotFoundException(e : SaleNotFoundException) : ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
    }

    @ExceptionHandler(SaleAlreadyAssociatedException::class)
    fun handleSaleNotFoundException(e : SaleAlreadyAssociatedException) : ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
    }


}


class SaleNotFoundException(saleId: String, message : String? = null) : Exception("Sale with id $saleId not found. ${message ?: ""}")

class SaleAlreadyAssociatedException(saleId: String, message : String? = null) : Exception("Sale with id $saleId already associated. ${message ?: ""}")
