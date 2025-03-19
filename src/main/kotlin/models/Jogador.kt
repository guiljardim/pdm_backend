package models

import kotlinx.serialization.Serializable

@Serializable
data class Jogador(
    val id: Int? = null,
    val nome: String,
    val telefone: String,
    val email: String? = null,
    val tipo: TipoJogador = TipoJogador.DIARISTA,
    val ativo: Boolean = true
)

enum class TipoJogador {
    MENSALISTA, DIARISTA
}