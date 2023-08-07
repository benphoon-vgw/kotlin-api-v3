package co.vgw.webapi.domain

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals
import java.util.*

class WalletBalanceTest {
    private val version = 1
    private val closingBalance = 100

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "\n", "\t"])
    fun `given blank transactionId, when creating wallet balance instance, then throw IllegalArgumentException`(transactionId: String) {
        val e = assertThrows<IllegalArgumentException> { WalletBalance(version, transactionId, closingBalance) }

        assertEquals("[${transactionId}] transactionId cannot be blank.", e.message)
    }
}

class TransactionTest {
    private val walletId = UUID.randomUUID()
    private val coins = 100
    private val transactionType = TransactionType.CREDIT
    private val version = 1
    private val closingBalance = 900

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "\n", "\t"])
    fun `given blank transactionId, when creating a Transaction, then throw IllegalArgumentException`(transactionId: String) {
        val e = assertThrows<IllegalArgumentException> { Transaction(walletId, transactionId, coins, transactionType, version, closingBalance) }

        assertEquals("[${transactionId}] transactionId cannot be blank.", e.message)
    }
}

class CreditCommandsTest {
    private val walletId = UUID.randomUUID()
    private val coins = 100

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "\n", "\t"])
    fun `given blank transactionId, when creating credit command, then throw IllegalArgumentException`(transactionId: String) {
        val e = assertThrows<IllegalArgumentException> { CreditCommand(walletId, transactionId, coins) }

        assertEquals("[${transactionId}] transactionId cannot be blank.", e.message)
    }
}

class DebitCommandsTest {
    private val walletId = UUID.randomUUID()
    private val coins = 100

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "\n", "\t"])
    fun `given blank transactionId, when creating debit command, then throw IllegalArgumentException`(transactionId: String) {
        val e = assertThrows<IllegalArgumentException> { DebitCommand(walletId, transactionId, coins) }

        assertEquals("[${transactionId}] transactionId cannot be blank.", e.message)
    }
}