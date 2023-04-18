package com.hexagonal.common.exception

import com.hexagonal.common.exception.ConstraintErrorMessage
import com.hexagonal.common.exception.ErrorCode
import com.hexagonal.common.exception.ServiceException
import com.hexagonal.common.logging.Log
import com.hexagonal.domain.common.dto.ApiResponse
import org.slf4j.event.Level
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

/**
 * 예외처리 advice
 */
@RestControllerAdvice
class ExceptionHandlerAdvice : Log {

    @ExceptionHandler(ServiceException::class)
    fun serviceExceptionHandler(e: ServiceException, request: HttpServletRequest): ResponseEntity<ApiResponse<Any?>> {
        val errorMessage = e.message.orEmpty()
        val errorCode = e.errorCode

        when (errorCode.logLevel) {
            Level.DEBUG -> log.debug("[ServiceException] request method: ${request.method}, request uri: ${request.requestURI}, error message: $errorMessage")
            Level.INFO -> log.info("[ServiceException] request method: ${request.method}, request uri: ${request.requestURI}, error message: $errorMessage")
            Level.WARN -> log.warn(
                "[ServiceException] request method: ${request.method}, request uri: ${request.requestURI}, error message: $errorMessage",
                e,
            )

            Level.ERROR -> log.error(
                "[ServiceException] request method: ${request.method}, request uri: ${request.requestURI}, error message: $errorMessage",
                e,
            )

            Level.TRACE -> log.trace(
                "[ServiceException] request method: ${request.method}, request uri: ${request.requestURI}, error message: $errorMessage",
                e,
            )
        }

        return errorResponseEntity(
            errorCode.httpStatusCode,
            errorCode.code,
            errorCode.name,
            errorMessage,
            request.requestURI.orEmpty(),
        )
    }

    @ExceptionHandler(BindException::class)
    fun bindExceptionHandler(e: BindException, request: HttpServletRequest): ResponseEntity<ApiResponse<Any?>> {
        val mappedConstraintMessage = e.bindingResult.fieldErrors.map { error ->
            ConstraintErrorMessage(
                property = error.field,
                message = error.defaultMessage.orEmpty(),
            )
        }
        val errorCode = ErrorCode.INVALID_ENTITY_ERROR
        log.warn(errorCode.message, e)

        return errorResponseEntity(
            errorCode.httpStatusCode,
            errorCode.code,
            errorCode.name,
            errorCode.message,
            request.requestURI.orEmpty(),
            mappedConstraintMessage,
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableExceptionHandler(
        e: HttpMessageNotReadableException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiResponse<Any?>> {
        log.error("HttpMessageNotReadableException! message: {}", e.message, e)

        val errorCode = ErrorCode.BAD_REQUEST_ERROR

        return errorResponseEntity(
            errorCode.httpStatusCode,
            errorCode.code,
            errorCode.name,
            errorCode.message,
            request.requestURI.orEmpty(),
        )
    }

    @ExceptionHandler(Throwable::class)
    fun unexpectedExceptionHandler(e: Exception, request: HttpServletRequest): ResponseEntity<ApiResponse<Any?>> {
        log.error("INTERNAL_SERVER_ERROR !!! message: {}", e.message, e)

        return errorResponseEntity(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "F99999",
            "INTERNAL_SERVER_ERROR",
            e.message,
            request.requestURI.orEmpty(),
        )
    }

    private fun errorResponseEntity(
        httpStatus: HttpStatus,
        code: String,
        status: String,
        message: String?,
        path: String,
        invalidParams: List<ConstraintErrorMessage>? = null,
    ): ResponseEntity<ApiResponse<Any?>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                ApiResponse(
                    success = false,
                    errorCode = code,
                    message = message,
                ),
            )
    }
}
