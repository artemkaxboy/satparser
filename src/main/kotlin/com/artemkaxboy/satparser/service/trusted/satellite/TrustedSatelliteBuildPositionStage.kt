package com.artemkaxboy.satparser.service.trusted.satellite

import com.artemkaxboy.satparser.dto.SatelliteDto
import com.artemkaxboy.satparser.parser.parseSatellitePosition
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

private val POSITION_REGEX = Regex("\\d{1,3}\\.\\d{1,2}°[EW]")

@Component
class TrustedSatelliteBuildPositionStage : TrustedSatelliteBuildStage {

    override fun process(jsoupRow: Element, builder: SatelliteDto.Builder) {

        jsoupRow.select("td:contains(°)")
            .firstOrNull() // row may contain (incl. 1.5°) in name string, may be null if previous row has rowspan
            ?.text()
            ?.takeIf { it.matches(POSITION_REGEX) }
            ?.let { parseSatellitePosition(it) }
            ?.also { builder.position = it }
    }
}
