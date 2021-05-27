package com.artemkaxboy.satparser.parser

import com.artemkaxboy.satparser.testtools.TestContainers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension


// todo move to integrationTest

@ExtendWith(SpringExtension::class)
@EnableAutoConfiguration(
    exclude = [
        MongoAutoConfiguration::class,
        MongoDataAutoConfiguration::class,
        DataSourceAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class,
    ]
)
internal class MainParserTest : TestContainers() {

    companion object {

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", MY_MYSQL_CONTAINER::getJdbcUrl)
            registry.add("spring.datasource.password", MY_MYSQL_CONTAINER::getPassword)
            registry.add("spring.datasource.username", MY_MYSQL_CONTAINER::getUsername)
        }
    }

    @Autowired
    lateinit var mainParser: MainParser

    @Autowired
    lateinit var applicationContext: ConfigurableApplicationContext

    @Test
    fun parse() {

        val allBeanNames: Array<String> = applicationContext.beanDefinitionNames
        for (beanName in allBeanNames) {
            println(beanName)
        }
        println("SIZE: " + allBeanNames.size)

        val satellites = mainParser.parse()
        println(satellites)
//        satellites.size shouldBeGreaterThan 200
    }
}
