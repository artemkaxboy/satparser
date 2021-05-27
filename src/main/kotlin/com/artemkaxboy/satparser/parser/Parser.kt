package com.artemkaxboy.satparser.parser

import com.artemkaxboy.satparser.domain.Satellite

interface Parser {

    fun parse(): Collection<Satellite>
}
