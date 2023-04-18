package com.hexagonal

import com.hexagonal.common.common.constant.PackageNameConstant
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan(basePackages = [PackageNameConstant.BASE_SCAN_PACKAGES])
@SpringBootApplication(scanBasePackages = [PackageNameConstant.BASE_SCAN_PACKAGES])
class DomainTestApplication {
    fun main(args: Array<String>) {
        runApplication<DomainTestApplication>(*args)
    }
}
