package com.artemkaxboy.satparser.service.trusted.pack.stages

import com.artemkaxboy.satparser.dto.PackDto
import com.artemkaxboy.satparser.service.trusted.pack.EXPECTED_PACK_NAME
import com.artemkaxboy.satparser.service.trusted.pack.createPackBuilder
import com.artemkaxboy.satparser.service.trusted.pack.createPackRowElement
import com.artemkaxboy.satparser.service.trusted.pack.makeTrElement
import com.artemkaxboy.satparser.testtools.InExpectedArgumentsProvider
import com.artemkaxboy.satparser.testtools.InExpectedValues
import io.kotest.matchers.equality.beEqualToUsingFields
import io.kotest.matchers.should
import org.jsoup.nodes.Element
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

internal class SetPackNameStageTest {

    @Nested
    inner class Process {

        @ParameterizedTest
        @ArgumentsSource(ValidDataProvider::class)
        fun `valid data`(data: InExpectedValues<Element, PackDto.Builder>) {

            val builder = PackDto.Builder()
            SetPackNameStage().process(data.input, builder)

            builder should beEqualToUsingFields(data.expected, PackDto.Builder::name)
        }
    }

    class ValidDataProvider : InExpectedArgumentsProvider<Element, PackDto.Builder>() {

        private val expectedName = "Package 333"
        private val nameWithDeniedChars = " $expectedName "

        override val arguments
            get() = sequenceOf(
                InExpectedValues(createPackRowElement(), createPackBuilder(name = EXPECTED_PACK_NAME)),
                InExpectedValues(createPackRowElement(packName = nameWithDeniedChars), createPackBuilder(name = expectedName)),
                InExpectedValues(makeTrElement(), createPackBuilder(name = null)),
                InExpectedValues(makeTrElement(EXPECTED_COLS_NUMBER), createPackBuilder(name = null)),
            )
    }
}
