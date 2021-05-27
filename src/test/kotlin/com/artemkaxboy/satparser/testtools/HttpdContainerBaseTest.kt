package com.artemkaxboy.satparser.testtools

import org.testcontainers.containers.GenericContainer

private const val HTTPD_IMAGE_NAME = "httpd"
private const val HTTPD_PORT = 80

abstract class HttpdContainerBaseTest {

    companion object {
        val MY_HTTPD_CONTAINER = GenericContainer<Nothing>(HTTPD_IMAGE_NAME).apply {
            withExposedPorts(HTTPD_PORT)
            start()
        }

        fun getURL(
            secure: Boolean = false,
            host: String = MY_HTTPD_CONTAINER.host,
            port: Int = MY_HTTPD_CONTAINER.getMappedPort(HTTPD_PORT),
        ): String {
            val protocol = if (secure) "https" else "http"
            return "$protocol://$host:$port"
        }
    }
}
