package co.vgw.webapi.domain

import java.util.*

class InMemoryRepository: TransactionRepository {
    private val transactionsPerWallet = mutableMapOf<UUID, MutableList<Transaction>>()

    override fun getWalletBalance(walletId: UUID): WalletBalance {
        val lastTransaction = transactionsPerWallet[walletId]?.last() ?: throw WalletNotFoundException(walletId)
        return WalletBalance(
            version = lastTransaction.version,
            closingBalance = lastTransaction.closingBalance,
            transactionId = lastTransaction.transactionId
        )
    }
}
