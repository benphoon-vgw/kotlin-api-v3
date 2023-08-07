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

    fun handleDebit(debitCommand: DebitCommand){
        val lastTransaction = repository.getLastTransaction(debitCommand.walletId)
        val wallet = Wallet(debitCommand.walletId)

        if(lastTransaction != null) {
            wallet.apply(lastTransaction.closingBalance, lastTransaction.version)
        }
        wallet.debit(debitCommand)
        repository.saveTransactions(wallet.pendingTransactions)
        wallet.pendingTransactions.clear()
    }
}
