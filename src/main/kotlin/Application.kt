package org.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import org.example.database.DatabaseFactory
import org.example.routes.authRoutes
import org.example.routes.jogadorRoutes
import org.example.routes.pagamentoRoutes

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()

    install(ContentNegotiation) {
        json()
    }

    install(CORS) {
        anyHost()
        allowHeader("Content-Type")
        allowMethod(io.ktor.http.HttpMethod.Options)
        allowMethod(io.ktor.http.HttpMethod.Put)
        allowMethod(io.ktor.http.HttpMethod.Delete)
        allowMethod(io.ktor.http.HttpMethod.Patch)
    }

    routing {
        authRoutes()
        jogadorRoutes()
        pagamentoRoutes()
    }
}