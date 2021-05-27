package com.artemkaxboy.satparser.service.trusted

import com.artemkaxboy.satparser.service.DataFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import java.net.URL

private val logger = KotlinLogging.logger {}

@Component
class TrustedDataFetcher : DataFetcher {

    override suspend fun fetchDocument(url: String, timeout: Int): Document? =
        runCatching { withContext(Dispatchers.IO) { Jsoup.parse(URL(url), timeout) } }
            .onSuccess { logger.debug { "Got document $url" } }
            .onFailure { logger.warn { "Cannot fetch data (URL = $url): $it" } }
            .getOrNull()
}
