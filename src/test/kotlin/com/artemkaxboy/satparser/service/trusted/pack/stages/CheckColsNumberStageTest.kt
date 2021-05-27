package com.artemkaxboy.satparser.service.trusted.pack.stages

import com.artemkaxboy.satparser.service.trusted.pack.makeTrElement
import com.artemkaxboy.satparser.testtools.InExpectedArgumentsProvider
import com.artemkaxboy.satparser.testtools.InExpectedValues
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beOfType
import org.jsoup.nodes.Element
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import kotlin.reflect.KClass

internal class CheckColsNumberStageTest {

    @Nested
    inner class Check {

        @ParameterizedTest
        @ArgumentsSource(ValidDataProvider::class)
        fun `valid data`(data: InExpectedValues<Element, KClass<*>?>) {

            val result = data.input.let { CheckColsNumberStage().check(it) }

            data.expected?.let { result should beOfType(it) }
                ?: result shouldBe null
        }
    }

    class ValidDataProvider : InExpectedArgumentsProvider<Element, KClass<*>?>() {

        private val validColsNumberElement = makeTrElement(EXPECTED_COLS_NUMBER)
        private val lessColsNumberElement = makeTrElement(EXPECTED_COLS_NUMBER - 1)
        private val moreColsNumberElement = makeTrElement(EXPECTED_COLS_NUMBER + 1)
        private val noColsElement = makeTrElement()

        override val arguments
            get() = sequenceOf(
                InExpectedValues(validColsNumberElement, null),
                InExpectedValues(lessColsNumberElement, IllegalArgumentException::class),
                InExpectedValues(moreColsNumberElement, IllegalArgumentException::class),
                InExpectedValues(noColsElement, IllegalArgumentException::class),
            )
    }
}
