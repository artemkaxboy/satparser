package com.artemkaxboy.satparser.service

import com.artemkaxboy.satparser.dto.PackDto

interface PackParser {

    suspend fun parse(url: String): Collection<PackDto>?
}
