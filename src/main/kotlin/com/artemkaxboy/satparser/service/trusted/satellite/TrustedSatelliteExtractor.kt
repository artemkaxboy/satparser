package com.artemkaxboy.satparser.service.trusted.satellite

import com.artemkaxboy.satparser.alerting.AlertGateway
import com.artemkaxboy.satparser.alerting.CATEGORY_API
import com.artemkaxboy.satparser.dto.SatelliteDto
import com.artemkaxboy.satparser.service.DataExtractor
import mu.KotlinLogging
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class TrustedSatelliteExtractor(

    val buildStages: Collection<TrustedSatelliteBuildStage>,
    val alertGateway: AlertGateway,
) : DataExtractor<SatelliteDto>() {

    override fun extractObjects(document: Document): Collection<SatelliteDto> {
        return extractRows(document)
            .mapNotNull { row ->
                makeSatellite(row)
                    .onFailure { throwable ->

                        val errorNumber = alertGateway.alert(CATEGORY_API)
                        logger.warn { "#$errorNumber - Cannot make satellite: ${throwable.message}\nRaw data: $row" }
                    }
                    .getOrNull()
            }
    }

    private fun makeSatellite(tableRow: Element): Result<SatelliteDto> {

        val builder = SatelliteDto.Builder()

        buildStages.forEach {
            it.process(tableRow, builder)
        }

        return builder.build()
    }
}
