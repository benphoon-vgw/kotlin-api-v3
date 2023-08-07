package co.vgw.webapi.domain

import java.util.*

interface TransactionRepository {
    fun getWalletBalance(walletId: UUID): WalletBalance
    fun getLastTransaction(walletId: UUID): Transaction?
    fun saveTransactions(pendingTransactions: List<Transaction>)
}