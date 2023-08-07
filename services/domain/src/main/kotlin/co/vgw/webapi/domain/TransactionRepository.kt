package co.vgw.webapi.domain

import java.util.*

interface TransactionRepository {
    fun getWalletBalance(walletId: UUID): WalletBalance
}