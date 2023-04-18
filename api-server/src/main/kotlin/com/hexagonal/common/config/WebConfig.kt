package com.hexagonal.common.config

import com.hexagonal.common.jackson.Jackson
import com.hexagonal.common.interceptor.TokenVerifyInterceptor
import com.hexagonal.common.resolver.LoginStudioUserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.charset.StandardCharsets

@Configuration
@EnableWebMvc
class WebConfig(
    private val tokenVerifyInterceptor: TokenVerifyInterceptor,
    private val loginStudioUserArgumentResolver: LoginStudioUserArgumentResolver,
) : WebMvcConfigurer {
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val stringConverter = StringHttpMessageConverter()
        stringConverter.defaultCharset = StandardCharsets.UTF_8
        converters.add(stringConverter)

        converters.add(MappingJackson2HttpMessageConverter(Jackson.mapper()))
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(tokenVerifyInterceptor)
            .addPathPatterns("/api/v1/**")
            .excludePathPatterns(
                "/api/v1/user/signup",
                "/api/v1/user/login",
                "/api/v1/user/auto-login",
                "/api/v1/user/refresh-token",
                "/api/v1/user/email",
                "/api/v1/user/password",
            )
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("http://localhost:**", "https://localhost:**")
            .allowedMethods("*")
            .allowedOrigins(
                "http://localhost:8080",
                "https://localhost:8080",
            )
    }

    /**
     * method argument resolver 정의
     */
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginStudioUserArgumentResolver)
    }
}
