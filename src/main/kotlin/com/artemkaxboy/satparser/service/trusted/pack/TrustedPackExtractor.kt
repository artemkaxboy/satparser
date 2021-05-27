package com.artemkaxboy.satparser.service.trusted.pack

import com.artemkaxboy.satparser.alerting.AlertGateway
import com.artemkaxboy.satparser.alerting.CATEGORY_API
import com.artemkaxboy.satparser.dto.PackDto
import com.artemkaxboy.satparser.extensions.asFailure
import com.artemkaxboy.satparser.service.DataExtractor
import com.artemkaxboy.satparser.service.trusted.pack.stages.TrustedPackBuildStage
import com.artemkaxboy.satparser.service.trusted.pack.stages.TrustedPackCheckStage
import mu.KotlinLogging
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class TrustedPackExtractor(

    val checkStages: Collection<TrustedPackCheckStage>,
    val buildStages: Collection<TrustedPackBuildStage>,
    val alertGateway: AlertGateway,
) : DataExtractor<PackDto>() {

    override fun extractObjects(document: Document): Collection<PackDto> {

        return extractRows(document)
            .mapNotNull { row ->
                makePack(row)
                    .onFailure { throwable ->
                        val errorNumber = alertGateway.alert(CATEGORY_API)
                        logger.warn { "#$errorNumber - Cannot make pack: ${throwable.message}\nRaw data: $row" }
                    }
                    .getOrNull()
            }
    }

    private fun makePack(tableRow: Element): Result<PackDto> {

        checkStages.asSequence()                // todo make it cute!
            .mapNotNull { it.check(tableRow) }
            .firstOrNull()
            ?.let {

                return it.asFailure("Wrong input data")
            }

        val builder = PackDto.Builder()

        buildStages.forEach {
            it.process(tableRow, builder)
        }

        return builder.build()
    }
}
