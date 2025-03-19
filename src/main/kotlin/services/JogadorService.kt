package org.example.services

import database.tables.JogadoresTable
import models.Jogador
import models.TipoJogador
import org.example.database.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime

class JogadorService {

    suspend fun getAllJogadores(): List<Jogador> = dbQuery {
        JogadoresTable.selectAll()
            .map { it.toJogador() }
    }

    suspend fun getJogadorById(id: Int): Jogador? = dbQuery {
        JogadoresTable.select { JogadoresTable.id eq id }
            .map { it.toJogador() }
            .singleOrNull()
    }

    suspend fun addJogador(jogador: Jogador): Jogador = dbQuery {
        val insertStatement = JogadoresTable.insert {
            it[nome] = jogador.nome
            it[telefone] = jogador.telefone
            it[email] = jogador.email
            it[tipo] = jogador.tipo
            it[ativo] = jogador.ativo
            it[dataCadastro] = LocalDateTime.now().toString()
        }

        insertStatement.resultedValues?.single()?.toJogador()
            ?: throw IllegalStateException("Erro ao inserir jogador")
    }

    suspend fun updateJogador(id: Int, jogador: Jogador): Boolean = dbQuery {
        JogadoresTable.update({ JogadoresTable.id eq id }) {
            it[nome] = jogador.nome
            it[telefone] = jogador.telefone
            it[email] = jogador.email
            it[tipo] = jogador.tipo
            it[ativo] = jogador.ativo
        } > 0
    }

    suspend fun deleteJogador(id: Int): Boolean = dbQuery {
        JogadoresTable.update({ JogadoresTable.id eq id }) {
            it[ativo] = false
        } > 0
    }

    suspend fun getMensalistas(): List<Jogador> = dbQuery {
        JogadoresTable.select {
            (JogadoresTable.tipo eq TipoJogador.MENSALISTA) and
                    (JogadoresTable.ativo eq true)
        }.map { it.toJogador() }
    }

    private fun ResultRow.toJogador() = Jogador(
        id = this[JogadoresTable.id],
        nome = this[JogadoresTable.nome],
        telefone = this[JogadoresTable.telefone],
        email = this[JogadoresTable.email],
        tipo = this[JogadoresTable.tipo],
        ativo = this[JogadoresTable.ativo]
    )
}