package org.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
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
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
    }

    routing {
        get("/") {
            call.respondText(
                """
            API da Pelada dos Membros
            
            API está funcionando corretamente!
            
            Endpoints disponíveis:
            - /jogadores
            - /pagamentos
            - /auth/login
            """.trimIndent(),
                ContentType.Text.Plain
            )
        }
        authRoutes()
        jogadorRoutes()
        pagamentoRoutes()
    }
}

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "varzinoApp"
            verifier(
                JWT
                    .require(Algorithm.HMAC256("JV8g6wBtzPbkMk3yQZX0JbB4UCRidMDoL98PtSrmwNctDRqP6wvLjHhNL24AvTz7"))
                    .withAudience("varzino")
                    .withIssuer("varzinoApp")
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") JWTPrincipal(credential.payload) else null
            }
        }
    }
}