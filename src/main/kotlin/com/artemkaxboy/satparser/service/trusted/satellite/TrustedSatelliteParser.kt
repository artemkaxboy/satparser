package com.artemkaxboy.satparser.service.trusted.satellite

import com.artemkaxboy.satparser.alerting.AlertGateway
import com.artemkaxboy.satparser.alerting.CATEGORY_API
import com.artemkaxboy.satparser.dto.SatelliteDto
import com.artemkaxboy.satparser.service.DataExtractor
import com.artemkaxboy.satparser.service.DataFetcher
import com.artemkaxboy.satparser.service.SatelliteParser
import mu.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class TrustedSatelliteParser(
    private val trustedDataFetcher: DataFetcher,
    private val trustedSatelliteExtractor: DataExtractor<SatelliteDto>,
    private val alertGateway: AlertGateway,
) : SatelliteParser {

    override suspend fun parse(url: String): Collection<SatelliteDto>? {

        return trustedDataFetcher.fetchDocument(url)
            ?.let { trustedSatelliteExtractor.extractObjects(it) }
            ?.let { fixRowspans(it) }
    }

    /**
     * Some data rows do not contain position column due to rowspan on previous ones.
     * Function copies position from previous element if it is missing.
     * Drops first elements while position is missing.
     */
    private fun fixRowspans(satellites: Collection<SatelliteDto>): List<SatelliteDto> {

        var lastKnownPosition: Double? = null

        return satellites
            .mapNotNull { dto ->
                when (dto.position.isFinite()) {
                    true -> dto.also { lastKnownPosition = dto.position }
                    false -> lastKnownPosition
                        ?.let { dto.copy(position = it) }
                        ?: null.also {

                            val errorNumber = alertGateway.alert(CATEGORY_API)
                            logger.error { "#$errorNumber - Unknown position, dropped: $dto" }
                        }
                }
            }
    }
}

