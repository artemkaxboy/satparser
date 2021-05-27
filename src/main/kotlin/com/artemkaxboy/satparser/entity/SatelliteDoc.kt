package com.artemkaxboy.satparser.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import java.time.LocalDateTime

@Document
data class SatelliteDoc(

    @MongoId
    val name: String,
    val position: Double,
    val url: String,
    val band: String?,
    val updated: LocalDateTime = LocalDateTime.now(),
    val oldName: String?,
    val packs: Collection<PackDoc>,
) {

    @Suppress("DuplicatedCode")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SatelliteDoc

        if (name != other.name) return false
        if (position != other.position) return false
        if (url != other.url) return false
        if (band != other.band) return false
        if (oldName != other.oldName) return false
        if (packs != other.packs) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + position.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + (band?.hashCode() ?: 0)
        result = 31 * result + (oldName?.hashCode() ?: 0)
        result = 31 * result + packs.hashCode()
        return result
    }
}
