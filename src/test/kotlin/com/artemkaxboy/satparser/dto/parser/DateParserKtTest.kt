package com.artemkaxboy.satparser.dto.parser

import com.artemkaxboy.satparser.testtools.InExpectedArgumentsProvider
import com.artemkaxboy.satparser.testtools.InExpectedValues
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.time.LocalDate

internal class DateParserKtTest {

    @Nested
    inner class ParseDate {

        @ParameterizedTest
        @ArgumentsSource(TestValuesProvider::class)
        fun check(data: InExpectedValues<String?, LocalDate?>) {
            data.input.let(::parseDate) shouldBe data.expected
        }
    }

    internal class TestValuesProvider : InExpectedArgumentsProvider<String?, LocalDate?>() {

        override val arguments
            get() = sequenceOf(
                InExpectedValues("010101", LocalDate.of(2001, 1, 1)),
                InExpectedValues("991231", LocalDate.of(2099, 12, 31)),
                InExpectedValues("9912310", null),
                InExpectedValues("99123", null),
                InExpectedValues("991331", null),
                InExpectedValues(null, null),
            )
    }
}
