package org.example.services


import models.Pagamento
import models.StatusPagamento
import org.example.database.DatabaseFactory.dbQuery
import org.example.database.tables.PagamentosTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PagamentoService {
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    suspend fun getAllPagamentos(): List<Pagamento> = dbQuery {
        PagamentosTable.selectAll()
            .map { it.toPagamento() }
    }

    suspend fun getPagamentoById(id: Int): Pagamento? = dbQuery {
        PagamentosTable.select { PagamentosTable.id eq id }
            .map { it.toPagamento() }
            .singleOrNull()
    }

    suspend fun addPagamento(pagamento: Pagamento): Pagamento = dbQuery {
        val dataPagamento = if (pagamento.dataPagamento.isNotEmpty()) {
            LocalDateTime.parse(pagamento.dataPagamento, dateTimeFormatter)
        } else {
            LocalDateTime.now()
        }

        val insertStatement = PagamentosTable.insert {
            it[jogadorId] = pagamento.jogadorId
            it[valor] = pagamento.valor
            it[PagamentosTable.dataPagamento] = dataPagamento
            it[tipo] = pagamento.tipo
            it[mesReferencia] = pagamento.mesReferencia
            it[status] = pagamento.status
        }

        insertStatement.resultedValues?.single()?.toPagamento()
            ?: throw IllegalStateException("Erro ao inserir pagamento")
    }

    suspend fun confirmPagamento(id: Int): Boolean = dbQuery {
        PagamentosTable.update({ PagamentosTable.id eq id }) {
            it[status] = StatusPagamento.CONFIRMADO
        } > 0
    }

    suspend fun cancelPagamento(id: Int): Boolean = dbQuery {
        PagamentosTable.update({ PagamentosTable.id eq id }) {
            it[status] = StatusPagamento.CANCELADO
        } > 0
    }

    suspend fun getPagamentosPendentes(): List<Pagamento> = dbQuery {
        PagamentosTable.select { PagamentosTable.status eq StatusPagamento.PENDENTE }
            .map { it.toPagamento() }
    }

    suspend fun getPagamentosByJogador(jogadorId: Int): List<Pagamento> = dbQuery {
        PagamentosTable.select { PagamentosTable.jogadorId eq jogadorId }
            .map { it.toPagamento() }
    }

    private fun ResultRow.toPagamento() = Pagamento(
        id = this[PagamentosTable.id],
        jogadorId = this[PagamentosTable.jogadorId],
        valor = this[PagamentosTable.valor],
        dataPagamento = this[PagamentosTable.dataPagamento].format(dateTimeFormatter),
        tipo = this[PagamentosTable.tipo],
        mesReferencia = this[PagamentosTable.mesReferencia],
        status = this[PagamentosTable.status]
    )
}