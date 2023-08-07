package co.vgw.webapi.domain

import java.util.*

class DebitCommand (
    val walletId: UUID,
    val transactionId: String,
    val coins: Int,
) {
    init {
        require(transactionId.isNotBlank()) { "[${transactionId}] transactionId cannot be blank." }
        require(coins > 0) { "[${coins}] is invalid. Coins value must be greater than zero." }
    }
}
