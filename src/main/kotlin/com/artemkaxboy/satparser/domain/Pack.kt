package com.artemkaxboy.satparser.domain

import com.artemkaxboy.satparser.entity.PackDoc
import java.time.LocalDate

data class Pack (

    val name: String,
    val url: String,
    val updated: LocalDate? = null,
) {

    fun toDoc() =
        PackDoc(name = name, url = url, updated = updated)
}
