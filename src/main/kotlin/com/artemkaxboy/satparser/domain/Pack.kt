package com.artemkaxboy.satparser.domain

import java.time.LocalDate

data class Pack (

    val name: String,
    val url: String,
    val updated: LocalDate? = null,
)
