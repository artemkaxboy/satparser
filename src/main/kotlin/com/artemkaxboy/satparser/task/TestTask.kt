package com.artemkaxboy.satparser.task

import com.artemkaxboy.satparser.configuration.properties.DevProperties
import mu.KotlinLogging
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import kotlin.random.Random

private val logger = KotlinLogging.logger {}

@Component
@Profile("dev")
class TestTask(
    val devProperties: DevProperties,
) : ITask {

    override fun run() {
        if (devProperties.logGenerator) {
            log(Random.nextInt(1, 5))
        }
    }

    fun log(v: Int) {
        when(v) {
            1 -> logger.debug { "debug" }
            2 -> logger.info { "info" }
            3 -> logger.warn { "warning" }
            else -> logger.debug { "debug" }
        }
    }
}
