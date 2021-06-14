package com.artemkaxboy.satparser.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "datasource")
class DatasourceProperties(

    val baseUrl: String = "",

    private val satellitePrefix: String = "",

    private val packsPrefix: String = "packages/",

    private val pageNames: List<String> = emptyList(),
) {

    fun satellitePageUrls() = pageNames.map { page -> baseUrl + satellitePrefix + page }

    fun packPageUrls() = pageNames.map { page -> baseUrl + packsPrefix + page }
}
