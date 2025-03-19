package org.example.database.tables

import database.tables.JogadoresTable
import models.StatusPagamento
import models.TipoPagamento
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object PagamentosTable : Table("pagamentos") {
    val id = integer("id").autoIncrement()
    val jogadorId = integer("jogador_id").references(JogadoresTable.id)
    val valor = double("valor")
    val dataPagamento = datetime("data_pagamento")
    val tipo = enumeration<TipoPagamento>("tipo")
    val mesReferencia = varchar("mes_referencia", 7).nullable()
    val status = enumeration<StatusPagamento>("status").default(StatusPagamento.PENDENTE)

    override val primaryKey = PrimaryKey(id)
}