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

            val token = "fake-jwt-token"

            call.respond(LoginResponse(token, user))
        }

        post("/register") {
            val user = call.receive<User>()
            call.respond(HttpStatusCode.Created, authService.createUser(user))
        }
    }
}