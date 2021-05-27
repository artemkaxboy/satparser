package com.artemkaxboy.satparser.service.trusted.pack.stages

import com.artemkaxboy.satparser.dto.PackDto
import com.artemkaxboy.satparser.service.trusted.pack.createPackBuilder
import com.artemkaxboy.satparser.service.trusted.pack.createPackRowElement
import com.artemkaxboy.satparser.service.trusted.pack.makeContainerElement
import com.artemkaxboy.satparser.testtools.InExpectedArgumentsProvider
import com.artemkaxboy.satparser.testtools.InExpectedValues
import io.kotest.matchers.equality.beEqualToUsingFields
import io.kotest.matchers.should
import org.jsoup.nodes.Element
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.time.LocalDate

internal class SetPackDateStageTest {

    @Nested
    inner class Process {

        @ParameterizedTest
        @ArgumentsSource(ValidDataProvider::class)
        fun `valid data`(data: InExpectedValues<Element, PackDto.Builder>) {

            val builder = PackDto.Builder()
            SetPackDateStage().process(data.input, builder)

            builder should beEqualToUsingFields(data.expected, PackDto.Builder::date)
        }
    }

    class ValidDataProvider : InExpectedArgumentsProvider<Element, PackDto.Builder>() {

        private val dateNow = LocalDate.now()

        override val arguments
            get() = sequenceOf(
                InExpectedValues(createPackRowElement(date = dateNow), createPackBuilder(date = dateNow)),
                InExpectedValues(createPackRowElement(dateString = ""), createPackBuilder(date = null)),
                InExpectedValues(makeContainerElement(), createPackBuilder(date = null)),
            )
    }

    data class InOutValues(val input: Element, val expected: PackDto.Builder)
}
