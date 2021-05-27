package com.artemkaxboy.satparser.entity

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document
data class PackDoc(
    val name: String,
    val url: String,
    val updated: LocalDate?,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PackDoc

        if (name != other.name) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + url.hashCode()
        return result
    }
}
