package com.artemkaxboy.satparser.service.trusted.pack.stages

import com.artemkaxboy.satparser.service.getColumns
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

const val EXPECTED_COLS_NUMBER = 8

@Component
class CheckColsNumberStage : TrustedPackCheckStage {

    /* each row must contain [ _, position, satellite, name, freq, sid, lcn, date ] */

    override fun check(jsoupRow: Element): Throwable? {

        return jsoupRow.getColumns().size
            .takeUnless { it == EXPECTED_COLS_NUMBER }
            ?.let { IllegalArgumentException("Expected $EXPECTED_COLS_NUMBER cols, got $it") }
    }
}
