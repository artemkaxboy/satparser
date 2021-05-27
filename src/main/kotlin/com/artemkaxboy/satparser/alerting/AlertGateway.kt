package com.artemkaxboy.satparser.alerting

const val CATEGORY_API = "api"

interface AlertGateway {

    fun alert(category: String): Long
    fun alert(category: String, message: () -> String): Long
}
