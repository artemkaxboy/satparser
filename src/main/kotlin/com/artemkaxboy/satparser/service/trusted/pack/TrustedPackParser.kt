package com.artemkaxboy.satparser.service.trusted.pack

import com.artemkaxboy.satparser.dto.PackDto
import com.artemkaxboy.satparser.service.DataExtractor
import com.artemkaxboy.satparser.service.DataFetcher
import com.artemkaxboy.satparser.service.PackParser
import org.springframework.stereotype.Component

@Component
class TrustedPackParser(
    private val trustedDataFetcher: DataFetcher,
    private val trustedPackExtractor: DataExtractor<PackDto>,
) : PackParser {

    override suspend fun parse(url: String): Collection<PackDto>? {

        return trustedDataFetcher.fetchDocument(url)
            ?.let { trustedPackExtractor.extractObjects(it) }
    }
}
