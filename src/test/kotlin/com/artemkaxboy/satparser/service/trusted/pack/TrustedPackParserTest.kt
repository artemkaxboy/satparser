package com.artemkaxboy.satparser.service.trusted.pack

import com.artemkaxboy.satparser.testtools.TestContainers
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.longs.shouldBeLessThan
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import kotlin.system.measureTimeMillis


// todo move to integrationTest directory

@SpringBootTest
@ActiveProfiles("test")
internal class TrustedPackParserTest : TestContainers() {

//    companion object {
//
//        @JvmStatic
//        @DynamicPropertySource
//        fun properties(registry: DynamicPropertyRegistry) {
//            registry.add("spring.datasource.url", MY_MYSQL_CONTAINER::getJdbcUrl)
//            registry.add("spring.datasource.password", MY_MYSQL_CONTAINER::getPassword)
//            registry.add("spring.datasource.username", MY_MYSQL_CONTAINER::getUsername)
//
//            registry.add("spring.data.mongodb.host", MONGODB_CONTAINER::getHost)
//            registry.add("spring.data.mongodb.port", MONGODB_CONTAINER::getFirstMappedPort)
//        }
//    }

    @Autowired
    lateinit var trustedPackParser: TrustedPackParser

    @Test
    fun `coroutine understanding`(): Unit = runBlocking {
        measureTimeMillis {
            delay(200)
        } shouldBeGreaterThan 200

        measureTimeMillis {
            async { delay(200) }
        } shouldBeLessThan 200

        measureTimeMillis {
            async { delay(200) }.await()
        } shouldBeGreaterThan 200

        measureTimeMillis {
            async { delay(200) }.await()
            async { delay(200) }.await()
        } shouldBeGreaterThan 400

        measureTimeMillis {
            val first = async { delay(200) }
            val second = async { delay(200) }
            first.await()
            second.await()
        } shouldBeLessThan 400

        measureTimeMillis {
            val first = coroutineScope {
                async { delay(200) }
            }
            val second = coroutineScope {
                async { delay(200) }
            }
            first.await()
            second.await()
        } shouldBeGreaterThan 400
    }

    @Test
    fun parse() {


        runBlocking {
            trustedPackParser.parse("https://google.com/")
        }
    }
}
