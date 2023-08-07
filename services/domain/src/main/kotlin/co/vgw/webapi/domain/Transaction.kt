package co.vgw.webapi.domain

import java.util.*

data class Transaction(
    val walletId: UUID,
    val transactionId: String,
    val coins: Int,
    val transactionType: TransactionType,
    val version: Int,
    val closingBalance: Int,
) {
    init {
        require(transactionId.isNotBlank()) { "[${transactionId}] transactionId cannot be blank." }
        require(coins > 0) { "[${coins}] is invalid. Coins value must be greater than zero." }
        require(version > 0) { "[${version}] is invalid. Version must be greater than zero." }
    }
}

enum class TransactionType {
    CREDIT,
    DEBIT
}