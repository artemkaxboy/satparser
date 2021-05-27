package com.artemkaxboy.satparser.service.trusted.pack.stages

import com.artemkaxboy.satparser.dto.PackDto
import com.artemkaxboy.satparser.dto.parser.parseDate
import com.artemkaxboy.satparser.service.getColumn
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

private const val DATE_COL_INDEX = 7

@Component
class SetPackDateStage : TrustedPackBuildStage {

    override fun process(jsoupRow: Element, builder: PackDto.Builder) {

        builder.date = parseDate(jsoupRow.getColumn(DATE_COL_INDEX)?.text())
    }
}
