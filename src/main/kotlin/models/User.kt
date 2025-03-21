package models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val username: String,
    val password: String,
    val nome: String,
    val email: String,
    val role: Role = Role.JOGADOR,
    val jogadorId: Int? = null
)

enum class Role {
    ADMIN, JOGADOR
}

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val user: User,
    val token: String,
    val jogador: Jogador?
)