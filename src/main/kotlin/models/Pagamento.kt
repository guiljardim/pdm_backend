package models

import kotlinx.serialization.Serializable

@Serializable
data class Pagamento(
    val id: Int? = null,
    val jogadorId: Int,
    val valor: Double,
    val dataPagamento: String,
    val tipo: TipoPagamento,
    val mesReferencia: String? = null,
    val status: StatusPagamento = StatusPagamento.PENDENTE
)

enum class TipoPagamento {
    MENSALIDADE, DIARIA
}

enum class StatusPagamento {
    PENDENTE, CONFIRMADO, CANCELADO
}