package com.htt.bis.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.core.GrantedAuthorityDefaults

@Configuration
class AuthoritiesConfig {

    /**
     * Nie może to być w SecuirtyConfig, ponieważ: https://stackoverflow.com/questions/48971937/ugrade-spring-boot-2-0-0-rc2-exception-no-servletcontext-set
     */
    @Bean
    fun grantedAuthorityDefaults() = GrantedAuthorityDefaults("")
}
