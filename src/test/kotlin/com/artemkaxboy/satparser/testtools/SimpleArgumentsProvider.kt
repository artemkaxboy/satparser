package com.artemkaxboy.satparser.testtools

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream
import kotlin.streams.asStream

abstract class SimpleArgumentsProvider : ArgumentsProvider {

    abstract val arguments: Sequence<Any>

    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments>? {
        return arguments
            .map { Arguments.of(it) }
            .asStream()
    }
}

abstract class InExpectedArgumentsProvider<I, E> : ArgumentsProvider {

    abstract val arguments: Sequence<InExpectedValues<out I, out E>>

    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments>? {
        return arguments
            .map { Arguments.of(it) }
            .asStream()
    }
}

class InExpectedValues<I, E>(val input: I, val expected: E) {

    private val toStringValuesLength = 30

    override fun toString(): String =
        "input=\"${input.toString().take(toStringValuesLength)}\" " +
                "expected=\"${expected.toString().take(toStringValuesLength)}\""
}
