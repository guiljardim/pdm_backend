package org.example.database.tables


import models.Role
import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50).uniqueIndex()
    val password = varchar("password", 255)
    val nome = varchar("nome", 255)
    val email = varchar("email", 255)
    val role = enumeration<Role>("role").default(Role.JOGADOR)

    override val primaryKey = PrimaryKey(id)
}