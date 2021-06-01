package com.artemkaxboy.satparser.extensions

/**
 * Returns `true` if this character is digit, point or minus.
 */
fun Char.isDecimal() = this.isDigit() || this == '.' || this == '-'

/**
 * Takes the beginning of the string which represents decimal number and converts it to double.
 */
fun String.toDoubleOrNullBeginning() = this.takeWhile { it.isDecimal() }.toDoubleOrNull()
