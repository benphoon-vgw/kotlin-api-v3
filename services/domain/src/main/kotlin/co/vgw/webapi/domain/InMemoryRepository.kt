package co.vgw.webapi.domain

import java.util.*

class InMemoryRepository: TransactionRepository {
    override fun getWalletBalance(walletId: UUID): WalletBalance {
        return TODO("Provide the return value")
    }
}
