package com.htt.bis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
class BisApplication

fun main(args: Array<String>) {
    runApplication<BisApplication>(*args)
}