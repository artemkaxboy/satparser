package com.artemkaxboy.satparser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["com.artemkaxboy.satparser.configuration.properties"])
class SatelliteParserApplication

fun main(args: Array<String>) {
	runApplication<SatelliteParserApplication>(*args)
}
