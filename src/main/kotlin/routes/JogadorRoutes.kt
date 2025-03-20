package org.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import models.Jogador
import org.example.services.JogadorService

private val jogadorService = JogadorService()

fun Route.jogadorRoutes() {
    route("/jogadores") {
        authenticate("auth-jwt") {
            get {
                call.respond(jogadorService.getAllJogadores())
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@get
                }

                val jogador = jogadorService.getJogadorById(id)
                if (jogador == null) {
                    call.respond(HttpStatusCode.NotFound, "Jogador não encontrado")
                    return@get
                }

                call.respond(jogador)
            }

            post {
                val jogador = call.receive<Jogador>()
                call.respond(HttpStatusCode.Created, jogadorService.addJogador(jogador))
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@put
                }

                val jogador = call.receive<Jogador>()
                val atualizado = jogadorService.updateJogador(id, jogador)

                if (atualizado) {
                    call.respond(HttpStatusCode.OK, "Jogador atualizado com sucesso")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Jogador não encontrado")
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@delete
                }

                val removido = jogadorService.deleteJogador(id)

                if (removido) {
                    call.respond(HttpStatusCode.OK, "Jogador removido com sucesso")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Jogador não encontrado")
                }
            }

            get("/mensalistas") {
                call.respond(jogadorService.getMensalistas())
            }

            get("/usuario/{id}") {
                val userId = call.parameters["id"]?.toIntOrNull()
                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID de usuário inválido")
                    return@get
                }

                val jogador = jogadorService.getJogadorByUserId(userId)
                if (jogador == null) {
                    call.respond(HttpStatusCode.NotFound, "Jogador não encontrado para esse usuário")
                    return@get
                }

                call.respond(jogador)
            }
        }
    }
}
