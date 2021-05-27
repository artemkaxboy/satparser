package com.artemkaxboy.satparser.service.trusted.pack.stages

import com.artemkaxboy.satparser.dto.PackDto
import com.artemkaxboy.satparser.service.getColumn
import com.artemkaxboy.satparser.service.getLink
import mu.KotlinLogging
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

private const val SATELLITE_COL_INDEX = 1

/** Divides multi names "Astra 4A & SES 5" */
private const val MULTINAME_DELIMITER = " & "

/**
 *  Divides codes of satellites with the same main name.
 *
 *  Example: Thor 5/6/7 ->
 *  main name: Thor
 *  codes: [5, 6, 7]
 *
 *  T10/T12 ->
 *  main name: ""
 *  codes: [T10, T12]
 */
private const val CODE_DELIMITER = "/"

/**
 * Divides the main name and the code of satellite.
 *
 * Example: Eutelsat 7 West A/8 West B ->
 * main name: Eutelsat
 * codes: [7 West A, 8 West B]
 */
private const val MAIN_NAME_DELIMITER = " "

private val logger = KotlinLogging.logger {}

@Component
class SetPossibleSatelliteNamesStage : TrustedPackBuildStage {

    override fun process(jsoupRow: Element, builder: PackDto.Builder) {

        val possibleSatelliteMultiname = jsoupRow.getColumn(SATELLITE_COL_INDEX)?.getLink()?.text()

        possibleSatelliteMultiname
            ?.let(::breakDifferentNamesDifferentCodes)
            ?.flatMap(::breakSameNameDifferentCodes)
            ?.flatMap(::breakNoNameDifferentCodes)
            ?.distinct()
            ?.also { logNamesIfNeeded(possibleSatelliteMultiname, it) }
            ?.let { builder.setPossibleSatelliteNames(it) }
    }

    /**
     * Breaks apart multiname with different names and codes (e.g. "Astra 4A & SES 5" -> [Astra 4A, SES 5]).
     */
    private fun breakDifferentNamesDifferentCodes(multiname: String) =
        multiname.split(MULTINAME_DELIMITER)

    /**
     * Breaks apart multiname with same name and different codes
     * (e.g. "Thor 5/6/7" -> Thor 5, Thor 6, Thor 7, Thor 5/6/7])
     */
    private fun breakSameNameDifferentCodes(multiname: String): List<String> {

        if (!hasMainNameAndCodes(multiname)) return listOf(multiname)

        val name = multiname.substringBefore(MAIN_NAME_DELIMITER)

        return multiname
            .substringAfter(MAIN_NAME_DELIMITER) // drop name leave codes
            .split(CODE_DELIMITER)
            .map(String::trim)
            .map { "$name $it" }
            .plus(multiname) // add initial multiname as variant
    }

    /**
     * Breaks apart multiname with no name and different codes
     * (e.g. "T11/T14" -> [T11, T14, T11/T14])
     */
    private fun breakNoNameDifferentCodes(multiname: String): Collection<String> {

        if (hasMainName(multiname) || !hasCodes(multiname)) return listOf(multiname)

        return multiname
            .split(CODE_DELIMITER)
            .map(String::trim)
            .plus(multiname)
    }

    private fun logNamesIfNeeded(multiname: String, names: Collection<String>) {
        if (logger.isTraceEnabled && names.size > 1) {
            logger.trace { names.joinToString(prefix = "Multiname divided: $multiname -> ") }
        }
    }

    private fun hasMainNameAndCodes(name: String) =
        hasMainName(name) && hasCodes(name)

    private fun hasCodes(name: String) =
        name.contains(CODE_DELIMITER)

    private fun hasMainName(name: String) =
        name.contains(MAIN_NAME_DELIMITER)
}
