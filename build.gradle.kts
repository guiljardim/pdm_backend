val ktor_version = "2.2.3"
val kotlin_version = "1.8.0"
val logback_version = "1.4.5"
val exposed_version = "0.41.1"
val postgresql_version = "42.5.1"
val hikari_version = "5.0.1"

plugins {
    kotlin("jvm") version "1.8.0"
    id("io.ktor.plugin") version "2.2.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    id("org.flywaydb.flyway") version "9.22.3"
    application
}

group = "com.peladamanager"
version = "0.0.1"

application {
    mainClass.set("org.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = "org.example.ApplicationKt"
    }

    from({
        configurations.runtimeClasspath.get().filter { it.exists() }.map { if (it.isDirectory) it else zipTree(it) }
    })
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Ktor Core
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    // Autenticação
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")

    // CORS
    implementation("io.ktor:ktor-server-cors:$ktor_version")

    // Banco de dados
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.postgresql:postgresql:$postgresql_version")
    implementation("com.zaxxer:HikariCP:$hikari_version")
    implementation("org.flywaydb:flyway-core:10.0.0")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Testes
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("com.auth0:java-jwt:4.4.0")

}

flyway {
    url = "jdbc:postgresql://dpg-cvd3lc3v2p9s73cb5hl0-a.oregon-postgres.render.com:5432/fdm_db"
    user = "fdm_db_user"
    password = "4vRl9uTv7PeJlcGTJVTV3A9kkjJA7TNC"
    schemas = arrayOf("public")
    locations = arrayOf("filesystem:src/main/resources/db/migration")
    baselineOnMigrate = true
}