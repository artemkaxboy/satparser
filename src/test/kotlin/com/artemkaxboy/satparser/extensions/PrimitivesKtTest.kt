package com.artemkaxboy.satparser.extensions

import com.artemkaxboy.satparser.testtools.InExpectedArgumentsProvider
import com.artemkaxboy.satparser.testtools.InExpectedValues
import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

internal class PrimitivesKtTest {

    @ParameterizedTest
    @ArgumentsSource(IsDecimalDataProvider::class)
    fun isDecimal(data: InExpectedValues<Char, Boolean>) {

        data.input.isDecimal() shouldBe data.expected
    }

    @ParameterizedTest
    @ArgumentsSource(ToDoubleOrNullBeginningDataProvider::class)
    fun toDoubleOrNullBeginning(data: InExpectedValues<String, Double?>) {

        data.input.toDoubleOrNullBeginning() shouldBe data.expected
    }

    class IsDecimalDataProvider : InExpectedArgumentsProvider<Char, Boolean>() {

        override val arguments
            get() = sequenceOf(
                InExpectedValues('1', true),
                InExpectedValues('0', true),
                InExpectedValues('.', true),
                InExpectedValues(',', false),
                InExpectedValues(' ', false),
                InExpectedValues('-', true),
            )
    }

    class ToDoubleOrNullBeginningDataProvider : InExpectedArgumentsProvider<String, Double?>() {

        private val expectedName = "Package 333"
        private val nameWithDeniedChars = " $expectedName "

        override val arguments
            get() = sequenceOf(
                InExpectedValues("", null),
                InExpectedValues("", null),
                InExpectedValues("", null),
                InExpectedValues("", null),
                InExpectedValues("", null),
            )
    }


}
