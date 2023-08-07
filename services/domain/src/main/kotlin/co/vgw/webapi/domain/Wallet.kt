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

    internal fun apply(balance: Int, version: Int){
        this.balance = balance
        this.version = version
    }

    private fun add(transaction: Transaction){
        pendingTransactions.add(transaction)
        apply(transaction.closingBalance, transaction.version)
    }
}
