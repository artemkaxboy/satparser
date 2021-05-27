package com.artemkaxboy.satparser.service.trusted.pack.stages

import org.jsoup.nodes.Element

interface TrustedPackCheckStage {

    fun check(jsoupRow: Element): Throwable?
}
