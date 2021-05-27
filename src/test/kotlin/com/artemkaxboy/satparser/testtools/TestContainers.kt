package com.artemkaxboy.satparser.testtools

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.MySQLContainer

private const val MYSQL_IMAGE_NAME = "mysql"
private const val MYSQL_DATABASE = "test_database"
private const val MYSQL_USERNAME = "test_username"
private const val MYSQL_PASSWORD = "test_password"

private const val MONGODB_IMAGE_NAME = "mongo:4"

abstract class TestContainers {

    companion object {
        val MY_MYSQL_CONTAINER = MySQLContainer<Nothing>(MYSQL_IMAGE_NAME).apply {
            withDatabaseName(MYSQL_DATABASE)
            withUsername(MYSQL_USERNAME)
            withPassword(MYSQL_PASSWORD)
            start()
        }

        val MONGODB_CONTAINER = MongoDBContainer(MONGODB_IMAGE_NAME).apply {
            start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", Companion.MY_MYSQL_CONTAINER::getJdbcUrl)
            registry.add("spring.datasource.password", Companion.MY_MYSQL_CONTAINER::getPassword)
            registry.add("spring.datasource.username", Companion.MY_MYSQL_CONTAINER::getUsername)

            registry.add("spring.data.mongodb.host", Companion.MONGODB_CONTAINER::getHost)
            registry.add("spring.data.mongodb.port", Companion.MONGODB_CONTAINER::getFirstMappedPort)
        }
    }
}
