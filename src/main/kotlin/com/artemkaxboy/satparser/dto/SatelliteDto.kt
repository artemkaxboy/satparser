package com.artemkaxboy.satparser.dto

import com.artemkaxboy.satparser.domain.Satellite
import com.artemkaxboy.satparser.extensions.asFailure
import com.artemkaxboy.satparser.extensions.asSuccess
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val NAMES_SEPARATOR = "/"

data class SatelliteDto(
    val position: Double,
    val names: Collection<String>,
    val url: String,
    val band: String?,
    val updated: LocalDate?,
    val oldName: String?,
) {

    val name: String
        get() = names.joinToString(NAMES_SEPARATOR)

    fun toDomain(allPacks: Collection<PackDto> = emptyList()): Satellite {

        val currentSatellitePacks = allPacks
            .filter { pack ->
                pack.possibleSatelliteNames.any { possibleName -> names.contains(possibleName) }
            }
            .map { it.toDomain() }

        return Satellite(
            position = position,
            name = name,
            url = url,
            band = band,
            updated = updated,
            oldName = oldName,
            packs = currentSatellitePacks
        )
    }

    class Builder {

        private val defaultPosition = Double.NaN

        var position: Double? = null
        var names: Collection<String>? = null
        var link: String? = null
        var band: String? = null
        var updated: LocalDate? = null
        var oldName: String? = null

        fun build(): Result<SatelliteDto> {

            val names = names ?: return IllegalStateException("Satellite must have name").asFailure()
            val link = link ?: return IllegalStateException("Satellite must have link").asFailure()

            return SatelliteDto(position ?: defaultPosition, names, link, band, updated, oldName).asSuccess()
        }

    }
}
