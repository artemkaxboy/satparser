package com.artemkaxboy.satparser.entity

import java.time.LocalDate
import javax.persistence.Id
import javax.persistence.Table

//@Entity
@Table(name = "satellite")
data class SatelliteEntity(

    @Id
    val name: String,

    val position: Double,
    val link: String,
    val band: String?,
    val updated: LocalDate?,
    val tags: String,
)
