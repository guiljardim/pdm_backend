package org.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.LoginRequest
import models.LoginResponse
import models.User
import org.example.services.AuthService
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

private val authService = AuthService()

fun Route.authRoutes() {
    route("/auth") {
        post("/login") {
            val loginRequest = call.receive<LoginRequest>()
            val user = authService.getUser(loginRequest.username, loginRequest.password)

            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "Credenciais inválidas")
                return@post
            }

            val token = JWT.create()
                .withAudience("varzino")
                .withIssuer("varzinoApp")
                .withClaim("username", user.username)
                .withClaim("userId", user.id)
                .withExpiresAt(Date(System.currentTimeMillis() + 6000000)) // expira em ~100 minutos
                .sign(Algorithm.HMAC256("sua-chave-secreta"))

            call.respond(LoginResponse(token, user.copy(password = "")))
        }

        post("/register") {
            val user = call.receive<User>()
            val createdUser = authService.createUser(user)
            call.respond(HttpStatusCode.Created, createdUser.copy(password = ""))
        }
    }
}
