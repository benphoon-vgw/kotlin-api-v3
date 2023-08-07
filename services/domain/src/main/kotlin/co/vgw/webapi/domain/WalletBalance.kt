package co.vgw.webapi.domain

data class WalletBalance(
    val version: Int,
    val transactionId: String,
    val closingBalance: Int,
) {
    init {
        require(version > 0) { "[${version}] is invalid. Version must be a positive number greater than zero." }
        require(transactionId.isNotBlank()) { "[${transactionId}] transactionId cannot be blank." }
        require(closingBalance >= 0) { "[${closingBalance}] is invalid. Closing balance value must not be negative." }
    }
}