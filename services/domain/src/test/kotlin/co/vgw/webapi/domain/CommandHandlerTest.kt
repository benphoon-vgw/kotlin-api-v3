package co.vgw.webapi.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

// Test cases
    // Credit empty wallet (Postman)
    // Credit existing wallet (Postman)
    // Duplicate credit (Postman)
    // Concurrent credit (TODO - in repo test)
    // Debit empty wallet (TODO)
    // Debit existing wallet w less than balance (Postman)
    // Debit existing wallet w more than balance (Postman)
    // Duplicate debit (Postman)
    // Concurrent debit (TODO - to do in repo test)

private val walletId = UUID.randomUUID()
class CommandHandlerTest {
    private lateinit var commandHandler: CommandHandler
    private lateinit var transactionRepository: InMemoryRepository
    @BeforeEach
    fun setup() {
        transactionRepository = InMemoryRepository()
        commandHandler = CommandHandler(transactionRepository)
    }

    @Test
    fun `Given an empty Wallet with no transaction history, When a DEBIT command is attempted, Then should throw WalletNotFoundException`() {
        val debitCommand = DebitCommand(walletId, "tx01", 100)

        val e = assertThrows<WalletNotFoundException> {
            commandHandler.handleDebit(debitCommand)
        }

        assertEquals("This action on wallet[$walletId] cannot be processed as the wallet does not exist.", e.message)
    }
}
