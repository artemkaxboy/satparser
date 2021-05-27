package com.artemkaxboy.satparser.service.trusted

import com.artemkaxboy.satparser.testtools.HttpdContainerBaseTest
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TrustedDataFetcherTest : HttpdContainerBaseTest() {

    @Nested
    inner class FetchDocument {

        @Test
        fun pass() {

            runBlocking { TrustedDataFetcher().fetchDocument(getURL()) } shouldNotBe null
        }

        @Test
        fun `socket timeout`() {

            runBlocking { TrustedDataFetcher().fetchDocument(getURL(), 1) } shouldBe null
        }

        @Test
        fun `unknown host`() {

            runBlocking { TrustedDataFetcher().fetchDocument(getURL(host = "com.artemkaxboy")) } shouldBe null
        }

        @Test
        fun `SSL exception`() {

            runBlocking { TrustedDataFetcher().fetchDocument(getURL(secure = true)) } shouldBe null
        }

        @Test
        fun `connection refused`() {

            runBlocking { TrustedDataFetcher().fetchDocument(getURL(port = 1)) } shouldBe null
        }
    }
}
