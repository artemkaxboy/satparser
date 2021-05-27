package com.artemkaxboy.satparser.service.trusted.pack.stages

import com.artemkaxboy.satparser.dto.PackDto
import com.artemkaxboy.satparser.service.getColumn
import com.artemkaxboy.satparser.service.getLink
import com.artemkaxboy.satparser.service.getLinkAddress
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

private const val PACK_COL_INDEX = 3

@Component
class SetPackUrlStage : TrustedPackBuildStage {

    override fun process(jsoupRow: Element, builder: PackDto.Builder) {

        builder.url = jsoupRow.getColumn(PACK_COL_INDEX)?.getLink()?.getLinkAddress()
    }
}
