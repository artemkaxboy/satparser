package com.artemkaxboy.satparser.service.trusted.pack

import com.artemkaxboy.satparser.dto.PackDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val EXPECTED_PACK_NAME = "Trikolor Sibir"
val expected_date: LocalDate = LocalDate.now()

private val formatter = DateTimeFormatter.ofPattern("yyMMdd")

fun createPackRowElement(
    packName: String = EXPECTED_PACK_NAME,
    date: LocalDate = expected_date,
    dateString: String = dateToString(date),
): Document =
    Jsoup.parseBodyFragment(
        "<table><tr>\n" +
                "<td width=70 style=\"background:#bbffbb\" align=\"center\"><font face=\"Verdana\"><font size=2><a href=\"https://www.satbase.com/Express-AT1.html\">56.</font><font size=1>0</font><font size=2>&#176;E</font></a></td>\n" +
                "<td width=220 style=\"background:#bbffbb\"><font face=\"Arial\"><font size=2><a href=\"https://www.satbase.com/Express-AT1.html\">Express AT1</a></td>\n" +
                "<td width=20 align=\"center\" style=\"background:#bbffbb\"><font face=\"Arial\" size=1>Ku</font></td>\n" +
                "<td width=200 style=\"background:#ffb6c1\"><font face=\"Arial\"><font size=2><a href=\"https://www.satbase.com/packages/Trikolor-Sibir-56E.html\">$packName</a></td>\n" +
                "<td style=\"background:#ffb6c1\"><font face=\"Arial\"><font size=2><a href=\"https://www.satbase.com/packages/Trikolor-Sibir-56E.html\">Freq.</a></td>\n" +
                "<td style=\"background:#ffb6c1\"><font face=\"Arial\"><font size=2><a href=\"https://www.satbase.com/packages/Trikolor-Sibir-56E_sid.html\">SID</a></td>\n" +
                "<td style=\"background:#ffb6c1\"><font face=\"Arial\" size=2><br></td>\n" +
                "<td width=50 bgcolor=#d0ffff align=center><font face=\"Verdana\" size=1>$dateString</td>\n" +
                "</tr></table>"
    )

fun createPackBuilder(
    possibleSatelliteNames: Collection<String>? = listOf("Express AT1"),
    name: String? = "No",
    url: String? = "https://www.satbase.com/packages/Trikolor-Sibir-56E.html",
    date: LocalDate? = LocalDate.now(),
) =
    PackDto.Builder().apply {
        possibleSatelliteNames?.also { this.possibleSatelliteNames = it }
        name?.also { this.name = it }
        url?.also { this.url = it }
        date?.also { this.date = it }
    }

fun makeContainerElement(child: Element? = null) = Element("div").apply { child?.let { appendChild(it) } }

fun makeTrElement(tds: Int = 0) = Element("tr").apply { repeat(tds) { i -> appendChild(makeTdElement("$i"))} }

fun makeTdElement(text: String = ""): Element = Element("td").text(text)

fun makeLinkElement(href: String = "http://link.url/", text: String = "link") = Element("a").apply {
    attributes().add("href", href)
    text(text)
}

private fun dateToString(date: LocalDate) = formatter.format(date)
