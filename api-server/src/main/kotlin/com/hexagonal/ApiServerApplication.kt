package com.hexagonal

import com.hexagonal.common.constant.PackageNameConstant
import com.hexagonal.common.filter.ResponseTimeLoggingFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*
import javax.annotation.PostConstruct

@EnableScheduling
@ConfigurationPropertiesScan(basePackages = [PackageNameConstant.BASE_SCAN_PACKAGES])
@SpringBootApplication(scanBasePackages = [PackageNameConstant.BASE_SCAN_PACKAGES])
class StudioApplication {
    @PostConstruct
    fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    }
}

@SuppressWarnings("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<StudioApplication>(*args)
}

@Bean
fun loggingFilter(): FilterRegistrationBean<ResponseTimeLoggingFilter> {
    val registrationBean = FilterRegistrationBean<ResponseTimeLoggingFilter>()
    registrationBean.filter = ResponseTimeLoggingFilter()
    registrationBean.addUrlPatterns("/api/v1/**")
    return registrationBean
}
