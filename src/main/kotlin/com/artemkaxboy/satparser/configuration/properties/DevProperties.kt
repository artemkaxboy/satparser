package com.artemkaxboy.satparser.configuration.properties

import org.hibernate.validator.constraints.time.DurationMin
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import java.time.Duration

// https://stackoverflow.com/a/54003713/1452052
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "dev")
class DevProperties(

    val logGenerator: Boolean = false,

    //    https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config-conversion-duration
    @Suppress("unused") // needs for injecting to SpEL [UpdateTask]
    @field:DurationMin(message = "Task interval cannot be less than a minute.", minutes = 1)
    val testTaskInterval: Duration = Duration.ofSeconds(60),
)

@Suppress("unused") // needs for injecting to SpEL [UpdateTask]
@Component
class DevConfiguration(val properties: DevProperties)
