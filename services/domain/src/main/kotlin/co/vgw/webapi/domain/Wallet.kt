package co.vgw.webapi.domain

import java.util.*

class Wallet(
    val walletId: UUID,
) {
    private var balance: Int = 0
    private var version: Int = 0
    internal val pendingTransactions = mutableListOf<Transaction>()

    fun credit(creditCommand: CreditCommand) {
        add(
            Transaction(
                walletId = creditCommand.walletId,
                transactionId = creditCommand.transactionId,
                version = version + 1,
                coins = creditCommand.coins,
                closingBalance = balance + creditCommand.coins,
                transactionType = TransactionType.CREDIT
            )
        )
    }

    fun debit(debitCommand: DebitCommand) {
        if (version == 0) {
            throw WalletNotFoundException(debitCommand.walletId)
        }
        if (balance < debitCommand.coins) {
            throw InsufficientBalanceException(transactionId = debitCommand.transactionId, walletId = debitCommand.walletId)
        }
        add(
            Transaction(
                walletId = debitCommand.walletId,
                transactionId = debitCommand.transactionId,
                version = version + 1,
                coins = debitCommand.coins,
                closingBalance = balance - debitCommand.coins,
                transactionType = TransactionType.DEBIT
            )
        )
    }

    internal fun apply(balance: Int, version: Int){
        this.balance = balance
        this.version = version
    }

    private fun add(transaction: Transaction){
        pendingTransactions.add(transaction)
        apply(transaction.closingBalance, transaction.version)
    }
}
