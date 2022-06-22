package com.htt.bis.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

const val JWT_TOKEN_API_KEY_NAME = "JWT Token"

@EnableWebMvc
@Configuration
@EnableSwagger2
internal class SwaggerConfig {


    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.htt.bis.controller"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(
            ApiInfoBuilder()
                .title("BIS API")
                .build()
        )
        .securitySchemes(listOf(ApiKey(JWT_TOKEN_API_KEY_NAME, HttpHeaders.AUTHORIZATION, "header")) as List<SecurityScheme>)

}


