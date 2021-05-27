package com.artemkaxboy.satparser.service.trusted.pack.stages

import com.artemkaxboy.satparser.dto.PackDto
import com.artemkaxboy.satparser.service.getColumn
import com.artemkaxboy.satparser.service.getLink
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

private const val PACK_COL_INDEX = 3

private val DENIED_CHARS = listOf('')

@Component
class SetPackNameStage : TrustedPackBuildStage {

    override fun process(jsoupRow: Element, builder: PackDto.Builder) {

        builder.name = removeDeniedChars(
            jsoupRow.getColumn(PACK_COL_INDEX)?.getLink()?.text()
        )
    }

    private fun removeDeniedChars(name: String?): String? {
        return name?.filterNot { DENIED_CHARS.contains(it) }?.trim()
    }
}
