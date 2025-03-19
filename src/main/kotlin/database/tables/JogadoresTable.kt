package database.tables

import models.TipoJogador
import org.jetbrains.exposed.sql.Table

object JogadoresTable : Table("jogadores") {
    val id = integer("id").autoIncrement()
    val nome = varchar("nome", 255)
    val telefone = varchar("telefone", 20)
    val email = varchar("email", 255).nullable()
    val tipo = enumerationByName("tipo", 20, TipoJogador::class)
    val ativo = bool("ativo").default(true)
    val dataCadastro = varchar("data_cadastro", 30)
    override val primaryKey = PrimaryKey(id)
}