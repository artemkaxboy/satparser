package com.artemkaxboy.satparser.alerting

const val CATEGORY_API = "api"
const val CATEGORY_CHANGES = "changes"

interface AlertGateway {

    fun alert(category: String): Long
    fun alert(category: String, message: () -> String): Long
}
