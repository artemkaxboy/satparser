package com.artemkaxboy.satparser.service

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

abstract class DataExtractor<T> {

    protected fun extractRows(document: Document): Elements =
        document.select("table[cellspacing=0][border='']").select("tr")

    abstract fun extractObjects(document: Document): Collection<T>
}

private const val COLUMN_TAG = "td"
private const val LINK_TAG = "a"
private const val LINK_ADDRESS_ATTRIBUTE = "href"

fun Element.getColumns(): Elements = this.select(COLUMN_TAG)

fun Element.getColumn(number: Int): Element? = this.getColumns().getOrNull(number)

fun Element.getLink(): Element? = this.select(LINK_TAG).firstOrNull()

fun Element.getLinkAddress(): String = this.attr(LINK_ADDRESS_ATTRIBUTE)
