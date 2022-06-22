import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
	kotlin("plugin.jpa") version "1.6.10"
	kotlin("kapt") version "1.6.10"
}

group = "com.htt"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

springBoot {
	mainClass.set("com.htt.bis.BisApplicationKt")
	buildInfo()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.7")
	implementation("org.springframework.boot:spring-boot-starter-data-rest:2.6.7")
	implementation("org.springframework.boot:spring-boot-starter-jdbc:2.6.7")
	implementation("org.springframework.boot:spring-boot-starter-security:2.6.7")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:2.6.7")
	implementation("org.springframework.boot:spring-boot-starter-web:2.6.7")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5:3.0.4.RELEASE")
	implementation("javax.validation:validation-api:2.0.1.Final")
	runtimeOnly("org.postgresql:postgresql:42.3.4")
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.7")
	implementation("io.github.microutils:kotlin-logging:2.0.11")
	implementation("io.minio:minio:8.3.9")
	implementation( "org.keycloak:keycloak-spring-boot-starter:15.0.2")
	implementation("org.keycloak.bom:keycloak-adapter-bom:15.0.2")
	implementation("org.keycloak:keycloak-admin-client:15.0.2")
	implementation( "io.springfox:springfox-boot-starter:3.0.0")
	implementation( "io.springfox:springfox-bean-validators:3.0.0")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client:2.6.7")
	implementation("org.springframework.security:spring-security-oauth2-jose:5.6.3")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:2.6.7")
	implementation("org.mapstruct:mapstruct:1.3.1.Final")
	kapt("org.mapstruct:mapstruct-processor:1.3.1.Final")
	implementation("org.slf4j:slf4j-api:1.7.36")
	implementation("org.imgscalr:imgscalr-lib:4.2")
	implementation("org.springframework:spring-test:5.3.19")
	implementation("com.querydsl:querydsl-jpa:5.0.0")
	implementation("com.querydsl:querydsl-core:5.0.0")
	implementation("com.querydsl:querydsl-apt:5.0.0")
	kapt("com.querydsl:querydsl-apt:5.0.0:jpa")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

dependencyManagement {
	imports {
		mavenBom("org.keycloak.bom:keycloak-adapter-bom:15.0.2")
	}
}
