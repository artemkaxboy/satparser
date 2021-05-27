package com.artemkaxboy.satparser.service.trusted.satellite

import com.artemkaxboy.satparser.configuration.properties.DatasourceProperties
import com.artemkaxboy.satparser.dto.SatelliteDto
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

@Component
class TrustedSatelliteBuildLinkStage(val datasourceProperties: DatasourceProperties) : TrustedSatelliteBuildStage {

    override fun process(jsoupRow: Element, builder: SatelliteDto.Builder) {

        // todo check if can use getLink
        val link = jsoupRow.select("td span a").attr("href")
        builder.link = if(link.contains(datasourceProperties.baseUrl)) {
            link
        } else {
            "${datasourceProperties.baseUrl}$link"
        }
    }
}
