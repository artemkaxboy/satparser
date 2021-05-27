package com.artemkaxboy.satparser.service.trusted.satellite

import com.artemkaxboy.satparser.dto.SatelliteDto
import com.artemkaxboy.satparser.service.getColumns
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

@Component
class TrustedSatelliteBuildBandStage : TrustedSatelliteBuildStage {

    override fun process(jsoupRow: Element, builder: SatelliteDto.Builder) {

        builder.band = jsoupRow.getColumns()
            .dropLast(1)
            .last()
            .text()
    }
}
