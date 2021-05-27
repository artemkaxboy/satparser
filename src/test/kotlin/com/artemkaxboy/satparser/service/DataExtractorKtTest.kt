package com.artemkaxboy.satparser.service

import com.artemkaxboy.satparser.service.trusted.pack.makeContainerElement
import com.artemkaxboy.satparser.service.trusted.pack.makeLinkElement
import com.artemkaxboy.satparser.service.trusted.pack.makeTrElement
import com.artemkaxboy.satparser.testtools.InExpectedArgumentsProvider
import com.artemkaxboy.satparser.testtools.InExpectedValues
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

internal class DataExtractorKtTest {

    @Nested
    inner class GetColumns {

        @ParameterizedTest
        @ArgumentsSource(GetColumnsDataProvider::class)
        fun `valid data`(data: InExpectedValues<Element, Elements>) {

            data.input.getColumns() shouldBe data.expected
        }
    }

    class GetColumnsDataProvider : InExpectedArgumentsProvider<Element, Elements>() {

        private val noRow = makeContainerElement()
        private val emptyRow = makeTrElement()
        private val oneColRow = makeTrElement(1)
        private val multiColsRow = makeTrElement(5)

        private val noColumnsResult = Elements()

        override val arguments
            get() = sequenceOf(
                InExpectedValues(noRow, noColumnsResult),
                InExpectedValues(emptyRow, noColumnsResult),
                InExpectedValues(oneColRow, Elements(oneColRow.children())),
                InExpectedValues(multiColsRow, Elements(multiColsRow.children())),
            )
    }

    @Nested
    inner class GetColumn {

        @ParameterizedTest
        @ArgumentsSource(GetColumnDataProvider::class)
        fun `valid data`(data: InExpectedValues<Pair<Int, Element>, Element?>) {

            data.input.second.getColumn(data.input.first) shouldBe data.expected
        }
    }

    class GetColumnDataProvider : InExpectedArgumentsProvider<Pair<Int, Element>, Element?>() {

        private val noRow = makeContainerElement()
        private val emptyRow = makeTrElement()
        private val oneColRow = makeTrElement(1)
        private val multiColsRow = makeTrElement(5)

        override val arguments
            get() = sequenceOf(
                InExpectedValues(0 to noRow, null),
                InExpectedValues(0 to emptyRow, null),
                InExpectedValues(0 to oneColRow, oneColRow.children().first()),
                InExpectedValues(9 to oneColRow, null),
                InExpectedValues(0 to multiColsRow, multiColsRow.children().firstOrNull()),
                InExpectedValues(2 to multiColsRow, multiColsRow.children()[2]),
                InExpectedValues(multiColsRow.childrenSize() - 1 to multiColsRow, multiColsRow.children().lastOrNull()),
                InExpectedValues(multiColsRow.childrenSize() to multiColsRow, null),
            )
    }

    @Nested
    inner class GetLink {

        @ParameterizedTest
        @ArgumentsSource(GetLinkDataProvider::class)
        fun `valid data`(data: InExpectedValues<Element, Element?>) {

            data.input.getLink() shouldBe data.expected
        }
    }

    class GetLinkDataProvider : InExpectedArgumentsProvider<Element, Element?>() {

        private val noLinkElement = makeContainerElement()
        private val oneLinkElement = makeContainerElement(makeLinkElement())
        private val multiLinksElement = makeContainerElement()
            .apply { repeat(5) { makeLinkElement(text = "$it") } }

        override val arguments
            get() = sequenceOf(
                InExpectedValues(noLinkElement, null),
                InExpectedValues(oneLinkElement, oneLinkElement.children().first()),
                InExpectedValues(multiLinksElement, multiLinksElement.children().first()),
            )
    }

    @Nested
    inner class GetLinkAddress {

        @ParameterizedTest
        @ArgumentsSource(GetColumnsDataProvider::class)
        fun `valid data`(data: InExpectedValues<Element, Element?>) {

            data.input.getColumns() shouldBe data.expected
        }
    }
}
