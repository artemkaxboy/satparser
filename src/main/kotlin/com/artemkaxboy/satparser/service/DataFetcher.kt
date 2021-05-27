package com.artemkaxboy.satparser.service

import org.jsoup.nodes.Document

private const val DEFAULT_TIMEOUT = 30_000

interface DataFetcher {

    suspend fun fetchDocument(url: String, timeout: Int = DEFAULT_TIMEOUT): Document?
}
