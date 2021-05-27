package com.artemkaxboy.satparser.service.trusted.satellite

import com.artemkaxboy.satparser.dto.SatelliteDto
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

private const val OLD_NAME_START_CHAR = '('
private const val OLD_NAME_END_CHAR = ')'
private const val NAMES_DELIMITER = '/'

@Component
class TrustedSatelliteBuildNameStage : TrustedSatelliteBuildStage {

    override fun process(jsoupRow: Element, builder: SatelliteDto.Builder) {

        /* Multiname may contain "name 1/name 2" or "name (old name)" */
        val multiname = jsoupRow.select("td span a").text()

        builder.oldName = multiname.substringAfter(OLD_NAME_START_CHAR, "")
            .trim(' ', OLD_NAME_END_CHAR)

        builder.names = multiname.substringBefore(OLD_NAME_START_CHAR)
            .split(NAMES_DELIMITER)
            .map { it.trim() }
    }
}
