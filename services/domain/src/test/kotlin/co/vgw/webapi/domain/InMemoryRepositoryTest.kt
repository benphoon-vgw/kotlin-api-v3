package co.vgw.webapi.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

// Test cases
    // Concurrent transaction -> throw version error

private val walletId = UUID.randomUUID()
class InMemoryRepositoryTest {
    private lateinit var transactionRepository: InMemoryRepository
    @BeforeEach
    fun setup() {
        transactionRepository = InMemoryRepository()
    }

    @Test
    fun `Given an existing wallet with one transaction, When a concurrent CREDIT transaction has save attempt, Then throws VersionErrorException`() {
        val initialTransaction = Transaction(
            walletId = walletId,
            transactionType = TransactionType.CREDIT,
            transactionId = "tx001",
            coins = 100,
            version = 1,
            closingBalance = 100
        )
        transactionRepository.saveTransactions(listOf(initialTransaction))

        val concurrentCreditTransaction = Transaction(
            walletId = walletId,
            transactionType = TransactionType.CREDIT,
            transactionId = "tx002",
            coins = 100,
            version = 1,
            closingBalance = 100
        )

        val e = assertThrows<VersionErrorException> {
            transactionRepository.saveTransactions(listOf(concurrentCreditTransaction))
        }

        assertEquals("Version [${concurrentCreditTransaction.version}] of wallet [${concurrentCreditTransaction.walletId}] has already been processed.", e.message)
    }
}
