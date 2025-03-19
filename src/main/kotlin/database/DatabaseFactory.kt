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

        val databaseUrl = System.getenv("DATABASE_URL")
            ?: "postgresql://fdm_db_user:4vRl9uTv7PeJlcGTJVTV3A9kkjJA7TNC@dpg-cvd3lc3v2p9s73cb5hl0-a.oregon-postgres.render.com/fdm_db"

        if (System.getenv("RENDER") != null || databaseUrl.contains("render.com")) {
            val regex = Regex("postgres(?:ql)?:\\/\\/(?<user>[^:]+):(?<password>[^@]+)@(?<host>[^:/]+)(:(?<port>\\d+))?/(?<database>.+)")
            val matchResult = regex.matchEntire(databaseUrl)

            if (matchResult != null) {
                val groups = matchResult.groups
                val user = groups["user"]?.value ?: ""
                val password = groups["password"]?.value ?: ""
                val host = groups["host"]?.value ?: ""
                val port = groups["port"]?.value ?: "5432"  // Assume 5432 se não tiver
                val database = groups["database"]?.value ?: ""

                config.jdbcUrl = "jdbc:postgresql://$host:$port/$database"
                config.username = user
                config.password = password
            } else {
                throw RuntimeException("DATABASE_URL format is invalid")
            }
        } else {
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