package com.artemkaxboy.satparser.service

import com.artemkaxboy.satparser.configuration.properties.DatasourceProperties
import com.artemkaxboy.satparser.dto.PackDto
import com.artemkaxboy.satparser.dto.SatelliteDto
import kotlinx.coroutines.*
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class TrustedSatelliteSource(
    private val datasourceProperties: DatasourceProperties,
    private val trustedSatelliteParser: SatelliteParser,
    private val trustedPackParser: PackParser,
) : SatelliteSource {

    override suspend fun fetchSatellitesAsync(coroutineScope: CoroutineScope): Deferred<List<SatelliteDto>> {

        return coroutineScope.async {
            datasourceProperties.satellitePageUrls()
                .map { address ->
                    async(Dispatchers.Default) {
                        trustedSatelliteParser.parse(address)
                    }
                }
                .awaitAll()
                .asSequence()
                .filterNotNull()
                .flatten()
                .onEach { logger.trace { it } }
                .toList()
        }
    }

    override suspend fun fetchPacksAsync(coroutineScope: CoroutineScope): Deferred<List<PackDto>> {

        return coroutineScope.async {
            datasourceProperties.packPageUrls()
                .map { address ->
                    async(Dispatchers.Default) {
                        trustedPackParser.parse(address)
                    }
                }
                .awaitAll()
                .asSequence()
                .filterNotNull()
                .flatten()
                .onEach { logger.trace { it } }
                .toList()
        }
    }
}
