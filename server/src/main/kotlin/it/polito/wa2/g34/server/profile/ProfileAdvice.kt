package it.polito.wa2.g34.server.profile

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ProfileAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException): ProblemDetail {
        val d = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
        d.title = "Wrong Input"
        d.detail = e.message
        return d
    }

    @ExceptionHandler(ProfileNotFoundException::class)
    fun handleProfileNotFoundException(e: ProfileNotFoundException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
    }

    @ExceptionHandler(ProfileAlreadyExistException::class)
    fun handleProfileAlreadyExistException(e: ProfileAlreadyExistException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
    }

    @ExceptionHandler(ProfileDataException::class)
    fun handleProfileDataException(e: ProfileDataException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
    }


}

class ProfileNotFoundException(message: String) : Exception(message)
class ProfileDataException(message: String) : Exception(message)
class ProfileAlreadyExistException(message: String) : Exception(message)

