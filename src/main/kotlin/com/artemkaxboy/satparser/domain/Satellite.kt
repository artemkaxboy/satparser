package com.artemkaxboy.satparser.domain

import com.artemkaxboy.satparser.entity.PhpSatelliteEntity
import java.time.LocalDate

private const val PHP_TAGS_SEPARATOR = ";"
private const val PHP_DEFAULT_BAND = ""

data class Satellite(

    val position: Double,
    val name: String,
    val url: String,
    val band: String? = null,
    val updated: LocalDate? = null,
    val oldName: String? = null,
    val packs: Collection<Pack> = emptyList()
) {

    fun toPhpEntity(): PhpSatelliteEntity =
        PhpSatelliteEntity(
            name = name,
            position = position,
            link = url,
            band = band ?: PHP_DEFAULT_BAND,
            tags = packs.joinToString(PHP_TAGS_SEPARATOR) { it.name },
        )
}
