package com.artemkaxboy.satparser.dto.parser

import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal fun parseDate(string: String?): LocalDate? {
    return string?.runCatching {
        LocalDate.parse(this, DateTimeFormatter.ofPattern("yyMMdd"))
    }?.getOrNull()
}
