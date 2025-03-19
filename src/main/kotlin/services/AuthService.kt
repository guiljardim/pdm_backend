package org.example.services


import models.User
import org.example.database.DatabaseFactory.dbQuery
import org.example.database.tables.UsersTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class AuthService {
    suspend fun getUser(username: String, password: String): User? = dbQuery {
        UsersTable.select {
            (UsersTable.username eq username) and (UsersTable.password eq password)
        }.map { it.toUser() }
            .singleOrNull()
    }

    suspend fun getUserById(id: Int): User? = dbQuery {
        UsersTable.select { UsersTable.id eq id }
            .map { it.toUser() }
            .singleOrNull()
    }

    suspend fun createUser(user: User): User = dbQuery {
        val insertStatement = UsersTable.insert {
            it[username] = user.username
            it[password] = user.password // Em produção, use hash
            it[nome] = user.nome
            it[email] = user.email
            it[role] = user.role
        }

        insertStatement.resultedValues?.single()?.toUser()
            ?: throw IllegalStateException("Erro ao criar usuário")
    }

    private fun ResultRow.toUser() = User(
        id = this[UsersTable.id],
        username = this[UsersTable.username],
        password = "", // Não retornamos senha
        nome = this[UsersTable.nome],
        email = this[UsersTable.email],
        role = this[UsersTable.role]
    )
}