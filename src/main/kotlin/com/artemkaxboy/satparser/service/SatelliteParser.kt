package com.artemkaxboy.satparser.service

import com.artemkaxboy.satparser.dto.SatelliteDto

interface SatelliteParser {

    suspend fun parse(url: String): Collection<SatelliteDto>?
}
