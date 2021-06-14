package com.artemkaxboy.satparser.testtools

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer

private const val MYSQL_IMAGE_NAME = "mysql"
private const val MYSQL_DATABASE = "test_database"
private const val MYSQL_USERNAME = "test_username"
private const val MYSQL_PASSWORD = "test_password"

abstract class TestContainers {

    companion object {
        val MY_MYSQL_CONTAINER = MySQLContainer<Nothing>(MYSQL_IMAGE_NAME).apply {
            withDatabaseName(MYSQL_DATABASE)
            withUsername(MYSQL_USERNAME)
            withPassword(MYSQL_PASSWORD)
            start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", MY_MYSQL_CONTAINER::getJdbcUrl)
            registry.add("spring.datasource.password", MY_MYSQL_CONTAINER::getPassword)
            registry.add("spring.datasource.username", MY_MYSQL_CONTAINER::getUsername)
        }
    }
}
