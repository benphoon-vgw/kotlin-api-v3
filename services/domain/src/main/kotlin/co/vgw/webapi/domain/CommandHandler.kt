package co.vgw.webapi.domain

class CommandHandler(private val repository: TransactionRepository) {
    fun handleCredit(creditCommand: CreditCommand){
        val lastTransaction = repository.getLastTransaction(creditCommand.walletId)
        val wallet = Wallet(creditCommand.walletId)

        if(lastTransaction != null) {
            wallet.apply(lastTransaction.closingBalance, lastTransaction.version)
        }
        wallet.credit(creditCommand)
        repository.saveTransactions(wallet.pendingTransactions)
        wallet.pendingTransactions.clear()
    }
}
