package com.artemkaxboy.satparser.service.trusted.satellite

import com.artemkaxboy.satparser.dto.SatelliteDto
import org.jsoup.nodes.Element

interface TrustedSatelliteBuildStage {

    fun process(jsoupRow: Element, builder: SatelliteDto.Builder)
}
