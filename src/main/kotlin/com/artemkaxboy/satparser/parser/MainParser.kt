package com.artemkaxboy.satparser.parser

import com.artemkaxboy.satparser.domain.Satellite
import com.artemkaxboy.satparser.extensions.toDoubleOrNullBeginning
import com.artemkaxboy.satparser.service.SatelliteSource
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.stereotype.Component
import javax.transaction.Transactional
import kotlin.system.measureTimeMillis

private val logger = KotlinLogging.logger {}

@Component
class MainParser(
    val satelliteSourceService: SatelliteSource,
) : Parser {

    @Transactional
    override fun parse(): Collection<Satellite> {

        var result: Collection<Satellite>

        measureTimeMillis {
            result = runBlocking {
/*
//        phpSatelliteRepository.sync(satellites.toPhpSatelliteEntities(packs))
//        satelliteDocRepository.upsert(packs.toSatelliteDocs())
*/
                val packs = satelliteSourceService.fetchPacksAsync(this)
                val satellites =  satelliteSourceService.fetchSatellitesAsync(this)

//                println(packs.await())
                satellites.await().map { it.toDomain(packs.await()) }
            }
        }.also { println("Time to fetch: $it ms") }

        return result
    }
}

fun parseSatellitePosition(string: String): Double? {
    return string
        .toDoubleOrNullBeginning()
        ?.let {
            multipleByLongitudeFactor(it, string)
        }
}

fun multipleByLongitudeFactor(longitude: Double, longitudeString: String): Double {
    return when (isWestLongitude(longitudeString)) {
        true -> -longitude
        else -> longitude
    }
}

fun isWestLongitude(longitudeString: String): Boolean {
    return longitudeString.contains("w", true)
}

