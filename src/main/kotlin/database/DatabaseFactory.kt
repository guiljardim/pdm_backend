package org.example.database

import database.tables.JogadoresTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.example.database.tables.PagamentosTable
import org.example.database.tables.UsersTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        // Conectar ao banco de dados
        val database = Database.connect(hikari())

        // Criar tabelas
        transaction(database) {
            SchemaUtils.create(JogadoresTable)
            SchemaUtils.create(PagamentosTable)
            SchemaUtils.create(UsersTable)
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"

        // Veja se estamos rodando no Render (verificando a existência de uma variável de ambiente específica)
        val isRender = System.getenv("RENDER") != null

        if (isRender) {
            // Configuração para o ambiente Render
            val host = "dpg-cvd31c3v2p9s73cb5hl0-a"
            val port = "5432"
            val database = "fdm_db"
            val username = "fdm_db_user"
            val password = System.getenv("POSTGRES_PASSWORD") ?: "4vRl9uTv7PeJlcGTJVTV3A9kkjJA7TNC"

            config.jdbcUrl = "jdbc:postgresql://$host:$port/$database"
            config.username = username
            config.password = password
        } else {
            // Configuração para ambiente local
            config.jdbcUrl = "jdbc:postgresql://localhost:5432/peladamanager"
            config.username = "fdm_db_user"
            config.password = "4vRl9uTv7PeJlcGTJVTV3A9kkjJA7TNC"
        }

        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}