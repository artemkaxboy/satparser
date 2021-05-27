package com.artemkaxboy.satparser.service

import com.artemkaxboy.satparser.dto.PackDto
import com.artemkaxboy.satparser.dto.SatelliteDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

interface SatelliteSource {

    suspend fun fetchSatellitesAsync(coroutineScope: CoroutineScope): Deferred<List<SatelliteDto>>

    suspend fun fetchPacksAsync(coroutineScope: CoroutineScope): Deferred<List<PackDto>>
}
