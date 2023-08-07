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

    override fun getLastTransaction(walletId: UUID): Transaction? {
        return transactionsPerWallet[walletId]?.last()
    }

    override fun saveTransactions(pendingTransactions: List<Transaction>) {
        var existingTransactions = transactionsPerWallet[pendingTransactions.first().walletId] ?: mutableListOf()
        pendingTransactions.forEach {
            val duplicateTransactionExists = existingTransactions.find { existingTransaction ->
                existingTransaction.transactionId == it.transactionId
            } != null

            if (duplicateTransactionExists) {
                throw DuplicateTransactionException(
                    transactionId = it.transactionId,
                    walletId = pendingTransactions.first().walletId
                )
            }

            val versionErrorExists = existingTransactions.find { existingTransaction ->
                existingTransaction.version == it.version
            } != null

            if (versionErrorExists) {
                throw VersionErrorException(version = it.version, walletId = pendingTransactions.first().walletId)
            }
        }
        existingTransactions.addAll(pendingTransactions)
        transactionsPerWallet[pendingTransactions.first().walletId] = existingTransactions
    }

}
