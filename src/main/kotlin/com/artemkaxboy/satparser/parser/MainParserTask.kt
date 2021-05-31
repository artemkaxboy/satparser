package com.artemkaxboy.satparser.parser

import com.artemkaxboy.satparser.metrics.Meter
import com.artemkaxboy.satparser.metrics.MetricsRegistry
import com.artemkaxboy.satparser.service.PhpSatelliteService
import com.artemkaxboy.satparser.task.Task
import mu.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class MainParserTask(

    private val mainParser: MainParser,
    private val phpSatelliteService: PhpSatelliteService,
    private val metricsRegistry: MetricsRegistry,
) : Task() {

    override fun run() {
        mainParser.parse()
            .also { logger.info { "Got online satellites: ${it.size}" } }
            .also { metricsRegistry.updateMeter(Meter.SATELLITES_ONLINE, it.size) }
            .map { it.toPhpEntity() }
            .also(phpSatelliteService::sync)

//        satelliteDocRepository.upsert(packs.toSatelliteDocs()) // todo !!
    }
}

