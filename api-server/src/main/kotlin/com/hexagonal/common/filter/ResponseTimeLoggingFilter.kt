package com.hexagonal.common.filter

import com.hexagonal.common.logging.Log
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
@Order(1)
class ResponseTimeLoggingFilter : Filter, Log {
    companion object {
        private const val LONG_RESPONSE_TIME = 1000L
    }
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain,
    ) {
        val startTime = System.currentTimeMillis()
        val req: HttpServletRequest = request as HttpServletRequest
        chain.doFilter(request, response)
        val executionTime = System.currentTimeMillis() - startTime
        if (executionTime > LONG_RESPONSE_TIME) {
            log.warn(
                "Slow Api call request : {}, executionTime : {}",
                req.requestURI,
                executionTime,
            )
        }
    }
}
