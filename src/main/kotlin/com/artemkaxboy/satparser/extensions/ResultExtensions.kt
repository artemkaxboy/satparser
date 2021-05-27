package com.artemkaxboy.satparser.extensions

fun <T> T.asSuccess() = Result.success(this)

fun <T> Throwable.asFailure() = Result.failure<T>(this)

fun <T> Throwable.asFailure(message: String) = Result.failure<T>(Exception("$message: ${this.message}", this))
