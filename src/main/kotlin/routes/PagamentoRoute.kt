package org.example.routes


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.Pagamento
import org.example.services.PagamentoService

private val pagamentoService = PagamentoService()

fun Route.pagamentoRoutes() {
    route("/pagamentos") {
        get {
            call.respond(pagamentoService.getAllPagamentos())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }

            val pagamento = pagamentoService.getPagamentoById(id)
            if (pagamento == null) {
                call.respond(HttpStatusCode.NotFound, "Pagamento não encontrado")
                return@get
            }

            call.respond(pagamento)
        }

        post {
            val pagamento = call.receive<Pagamento>()
            call.respond(HttpStatusCode.Created, pagamentoService.addPagamento(pagamento))
        }

        put("/{id}/confirmar") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@put
            }

            val confirmado = pagamentoService.confirmPagamento(id)

            if (confirmado) {
                call.respond(HttpStatusCode.OK, "Pagamento confirmado com sucesso")
            } else {
                call.respond(HttpStatusCode.NotFound, "Pagamento não encontrado")
            }
        }

        put("/{id}/cancelar") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@put
            }

            val cancelado = pagamentoService.cancelPagamento(id)

            if (cancelado) {
                call.respond(HttpStatusCode.OK, "Pagamento cancelado com sucesso")
            } else {
                call.respond(HttpStatusCode.NotFound, "Pagamento não encontrado")
            }
        }

        get("/pendentes") {
            call.respond(pagamentoService.getPagamentosPendentes())
        }

        get("/jogador/{id}") {
            val jogadorId = call.parameters["id"]?.toIntOrNull()
            if (jogadorId == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de jogador inválido")
                return@get
            }

            call.respond(pagamentoService.getPagamentosByJogador(jogadorId))
        }
    }
}