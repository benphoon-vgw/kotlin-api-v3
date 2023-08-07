package co.vgw.webapi.domain

class QueryHandler(private val repository: TransactionRepository) {
    fun handleBalanceQuery(balanceQuery: BalanceQuery): WalletBalance {
        return repository.getWalletBalance(balanceQuery.walletId)
    }
}