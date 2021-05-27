package com.artemkaxboy.satparser.service.trusted.satellite

import com.artemkaxboy.satparser.dto.SatelliteDto
import com.artemkaxboy.satparser.service.getColumns
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class TrustedSatelliteBuildUpdatedStage : TrustedSatelliteBuildStage {

    override fun process(jsoupRow: Element, builder: SatelliteDto.Builder) {

        builder.updated = jsoupRow.getColumns()
            .last()
            .text()
            .let { parseDate(it) }
    }

    private fun parseDate(string: String): LocalDate {
        return runCatching { LocalDate.parse(string, DateTimeFormatter.ofPattern("yyMMdd")) }
            .getOrDefault(LocalDate.of(2000, 1, 1))
    }
}
