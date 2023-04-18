import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    val springVersion = "2.7.5"
    val kotlinVersion = "1.6.21"
    val ktlintVersion = "11.2.0"

    id("org.springframework.boot") version springVersion apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.kapt") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion apply false
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.noarg") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion apply false
    id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
    idea
}

allprojects {
    apply(plugin = "idea")

    group = "com.klonesales"
    version = "0.6.0-SNAPSHOT"

    tasks.withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
            )
            jvmTarget = "17"
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }

    idea {
        module {
            isDownloadSources = true
        }
    }
    configurations {
        all {
            exclude("junit", "junit")
            exclude("org.slf4j", "slf4j-log4j12")
            exclude("log4j", "log4j")
            exclude("commons-logging", "commons-logging")
        }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    val junitJupiterVersion by extra { "5.8.2" }
    val junitPlatformVersion by extra { "1.8.2" }
    val mockitoVersion by extra { "4.5.1" }
    val mockitoKotlinVersion by extra { "2.2.0" }
    val logbackVersion by extra { "1.2.6" }
    val slf4jVersion by extra { "1.7.32" }
    val mockkVersion by extra { "1.12.4" }
    val kotestVersion by extra { "5.3.2" }
    val resilience4jVersion by extra { "1.7.1" }

//    kapt {
//        correctErrorTypes = true
//    }

    dependencies {

        kapt("org.springframework.boot:spring-boot-configuration-processor")
        compileOnly("org.springframework.boot:spring-boot-configuration-processor")

        // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-configuration-processor
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

        implementation(kotlin("stdlib-jdk8"))
        implementation(kotlin("reflect"))

        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-aop")
        implementation("io.github.resilience4j:resilience4j-spring-boot2:$resilience4jVersion")
        implementation("io.github.resilience4j:resilience4j-kotlin:$resilience4jVersion")
        implementation("io.github.resilience4j:resilience4j-retry:$resilience4jVersion")

        implementation("org.slf4j:slf4j-api:$slf4jVersion")
        implementation("org.slf4j:log4j-over-slf4j:$slf4jVersion")
        implementation("org.slf4j:jcl-over-slf4j:$slf4jVersion")
        implementation("org.slf4j:jul-to-slf4j:$slf4jVersion")
        implementation("org.apache.logging.log4j:log4j-to-slf4j:2.14.0")
        implementation("ch.qos.logback:logback-core:$logbackVersion")
        implementation("ch.qos.logback:logback-classic:$logbackVersion")
        implementation("net.logstash.logback:logstash-logback-encoder:6.6")
        implementation("org.springframework.security:spring-security-crypto")
        implementation("org.springdoc:springdoc-openapi-kotlin:1.6.9")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

        // https://mvnrepository.com/artifact/io.seruco.encoding/base62
        implementation("io.seruco.encoding:base62:0.1.3")

        // https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload
        implementation("commons-fileupload:commons-fileupload:1.4")

        // cache
        implementation("org.springframework.boot:spring-boot-starter-cache")
        implementation("com.github.ben-manes.caffeine:caffeine:3.1.1")

        /**
         * aws cognito jwt 검증 구현을 위한 라이브러리
         */
        // https://mvnrepository.com/artifact/com.auth0/java-jwt
        implementation("com.auth0:java-jwt:4.2.1")
        // https://mvnrepository.com/artifact/com.auth0/jwks-rsa
        implementation("com.auth0:jwks-rsa:0.21.2")

        // 자체 jwt
        implementation("io.jsonwebtoken:jjwt-api:0.11.2")
        runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
        runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")

        // Test
        testImplementation("com.h2database:h2")
        testImplementation("org.springframework.boot:spring-boot-starter-test")

        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")

        testImplementation("org.mockito:mockito-inline:$mockitoVersion")
        testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
        testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:$mockitoKotlinVersion")
        testImplementation("io.mockk:mockk:$mockkVersion")
        testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.3")
        testImplementation("io.projectreactor:reactor-test")
        testImplementation("org.dbunit:dbunit:2.7.3")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        debug.set(true)
    }
}

project("domain") {
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "org.jetbrains.kotlin.plugin.noarg")
    apply(plugin = "io.spring.dependency-management")

    buildscript {
        allOpen {
            annotation("javax.persistence.Entity")
            annotation("javax.persistence.MappedSuperclass")
        }

        noArg {
            annotation("javax.persistence.Entity")
            annotation("javax.persistence.MappedSuperclass")
        }
    }

    dependencies {
        implementation(project(":commons"))

        // Spring
        implementation("org.springframework:spring-web")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-jdbc")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("org.springframework.boot:spring-boot-starter-mail")
        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

        // ETC
        implementation("org.apache.httpcomponents:httpclient:4.5.13")

        // DataSource
        implementation("mysql:mysql-connector-java:8.0.14")

        // querydsl
        implementation("com.querydsl:querydsl-jpa:5.0.0")
        kapt("com.querydsl:querydsl-apt:5.0.0:jpa")
        kapt("org.springframework.boot:spring-boot-configuration-processor")

        // Kotlin JDSL, https://github.com/line/kotlin-jdsl
        implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter:2.0.4.RELEASE")

        // aws

        /**
         * aws sdk java 1.x
         * cloudfront signed url 제공 기능이 2.x 에 없어 이를 위해 추가
         */
        implementation("com.amazonaws:aws-java-sdk-cloudfront:1.12.372")

        /**
         * aws sdk java 2.x
         */
        implementation(platform("software.amazon.awssdk:bom:2.18.33"))

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation("software.amazon.awssdk:s3")
        implementation("software.amazon.awssdk:cloudfront")
        implementation("software.amazon.awssdk:iam")
        implementation("software.amazon.awssdk:cloudwatch")
        implementation("software.amazon.awssdk:cognitoidentityprovider")
        implementation("software.amazon.awssdk:rekognition")
        implementation("software.amazon.msk:aws-msk-iam-auth:1.1.5")

        /**
         * 엑셀 파일 로드용 라이브러리
         */
        // https://mvnrepository.com/artifact/org.apache.poi/poi
        implementation("org.apache.poi:poi:5.2.3")
        // https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml
        implementation("org.apache.poi:poi-ooxml:5.2.3")

        // https://mvnrepository.com/artifact/org.imgscalr/imgscalr-lib
        implementation("org.imgscalr:imgscalr-lib:4.2")
    }

    // querydsl
    kotlin.sourceSets.main {
        setBuildDir("$buildDir")
    }

    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = false
    jar.enabled = true
}

project("commons") {
    val jacksonVersion by extra { "2.13.3" }

    dependencies {
        api("org.apache.commons:commons-lang3:3.12.0")
        api("org.apache.commons:commons-collections4:4.4")
        api("commons-io:commons-io:2.11.0")
        api("commons-codec:commons-codec:1.15")
        api("com.google.guava:guava:31.1-jre")

        api("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
        api("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
        api("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
        implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
        api("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    }

    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = false
    jar.enabled = true
}

task<Exec>("down") {
    group = "docker"
    commandLine = listOf("/bin/sh", "-c", "docker-compose -f ./docker/mysql/docker-compose.yml -p skeleton down")
}

task<Exec>("setup") {
    group = "docker"
    commandLine = listOf("/bin/sh", "-c", "docker-compose -f ./docker/mysql/docker-compose.yml -p skeleton up -d")
}
