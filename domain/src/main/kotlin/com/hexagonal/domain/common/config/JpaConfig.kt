package com.hexagonal.domain.common.config

import com.hexagonal.common.constant.PackageNameConstant
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = [PackageNameConstant.BASE_SCAN_PACKAGES])
@EntityScan(basePackages = [PackageNameConstant.BASE_SCAN_PACKAGES])
@EnableJpaAuditing
class JpaConfig
