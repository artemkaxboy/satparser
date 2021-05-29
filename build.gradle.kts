import org.ajoberstar.grgit.Commit
import org.ajoberstar.grgit.Grgit
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.spring") version "1.5.0"
    kotlin("plugin.jpa") version "1.5.0"
    kotlin("kapt") version "1.5.0"

/*-------------------------------- JIB -----------------------------------------------*/
    id("com.google.cloud.tools.jib") version "3.0.0"
    id("org.ajoberstar.grgit") version "4.1.0"
/*-------------------------------- JIB -----------------------------------------------*/

}

java.sourceCompatibility = JavaVersion.VERSION_11
group = "com.artemkaxboy"
version = project.property("applicationVersion") as String
val minorVersion = "$version".replace("^(\\d+\\.\\d+).*$".toRegex(), "$1")
val majorVersion = "$version".replace("^(\\d+).*$".toRegex(), "$1")

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["testcontainersVersion"] = "1.15.3"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")

    implementation("org.jsoup:jsoup:1.13.1")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("mysql:mysql-connector-java")
//	runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // validation
    implementation("org.hibernate:hibernate-validator:7.0.1.Final")

    // metrics
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // logging
    implementation("io.github.microutils:kotlin-logging:1.5.9")

    // mocking
    testImplementation("io.mockk:mockk:1.11.0")
    // https://phauer.com/2018/best-practices-unit-testing-kotlin/
    testImplementation("io.kotest:kotest-runner-junit5:4.4.3")
    testImplementation("io.kotest:kotest-assertions-core:4.4.3")

    // testcontainers
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:mongodb")
    testImplementation("org.testcontainers:junit-jupiter")

    kapt("org.springframework.boot:spring-boot-configuration-processor")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xallow-result-return-type")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

/*-------------------------------- JIB -----------------------------------------------*/
val dockerRegistry by extra { properties["dockerRegistry"] as String }

// https://stackoverflow.com/questions/55749856/gradle-dsl-method-not-found-versioncode
val commit: Commit = Grgit.open { currentDir = projectDir }.head()
val commitTime = "${commit.dateTime}"
val commitHash = commit.id.take(8) // short commit id contains 8 chars

val user = System.getenv("CONTAINER_REGISTRY_USERNAME") ?: ""
val token = System.getenv("CONTAINER_REGISTRY_TOKEN") ?: ""

jib {
    to {
        auth {
            username = user
            password = token
        }
        image = "${dockerRegistry}/${project.name}"
        tags = setOf("$version", minorVersion, majorVersion)
    }

    val author: String by project
    val sourceUrl: String by project

    container {
        user = "999:999"
        creationTime = commitTime
        ports = listOf("8080")

        environment = mapOf(
            "APPLICATION_NAME" to name,
            "APPLICATION_VERSION" to "$version",
            "APPLICATION_REVISION" to commitHash
        )

        labels = mapOf(
            "maintainer" to author,
            "org.opencontainers.image.created" to commitTime,
            "org.opencontainers.image.authors" to author,
            "org.opencontainers.image.url" to sourceUrl,
            "org.opencontainers.image.documentation" to sourceUrl,
            "org.opencontainers.image.source" to sourceUrl,
            "org.opencontainers.image.version" to "$version",
            "org.opencontainers.image.revision" to commitHash,
            "org.opencontainers.image.vendor" to author,
            "org.opencontainers.image.title" to name
        )
    }

}
/*-------------------------------- JIB -----------------------------------------------*/
