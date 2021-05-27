package com.artemkaxboy.satparser.dto

import com.artemkaxboy.satparser.domain.Pack
import com.artemkaxboy.satparser.extensions.asFailure
import com.artemkaxboy.satparser.extensions.asSuccess
import java.time.LocalDate

data class PackDto(
    val possibleSatelliteNames: Collection<String>,
    val packName: String,
    val packUrl: String,
    val updated: LocalDate
) {

    fun toDomain() =
        Pack(name = packName, url = packUrl, updated = updated)

    class Builder {

        private val defaultUrl = ""
        private val defaultUpdated = LocalDate.now()

        var possibleSatelliteNames: Collection<String>? = null
        fun setPossibleSatelliteNames(possibleSatelliteNames: Collection<String>) =
            this.apply { this.possibleSatelliteNames = possibleSatelliteNames }

        var name: String? = null
        fun setName(name: String) =
            this.apply { this.name = name }

        var url: String? = null
        fun setUrl(url: String) =
            this.apply { this.url = url }

        var date: LocalDate? = null
        fun setDate(date: LocalDate) =
            this.apply { this.date = date }

        fun build(): Result<PackDto> {

            val possibleSatelliteNames =
                possibleSatelliteNames ?: return IllegalStateException("Pack must have satellite name").asFailure()
            val packName = name ?: return IllegalStateException("Pack must have name").asFailure()

            return PackDto(
                possibleSatelliteNames = possibleSatelliteNames,
                packName = packName,
                packUrl = url ?: defaultUrl,
                updated = date ?: defaultUpdated,
            ).asSuccess()
        }

    }
}
