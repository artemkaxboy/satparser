package com.artemkaxboy.satparser.service.trusted.pack.stages

import com.artemkaxboy.satparser.dto.PackDto
import org.jsoup.nodes.Element

interface TrustedPackBuildStage {

    fun process(jsoupRow: Element, builder: PackDto.Builder)
}
