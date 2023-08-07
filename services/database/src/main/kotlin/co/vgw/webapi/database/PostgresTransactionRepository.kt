package co.vgw.webapi.database

import co.vgw.webapi.domain.DuplicateTransactionException
import co.vgw.webapi.domain.Transaction
import co.vgw.webapi.domain.TransactionType
import co.vgw.webapi.domain.VersionErrorException
import co.vgw.webapi.domain.WalletBalance
import co.vgw.webapi.domain.WalletNotFoundException
import co.vgw.webapi.domain.TransactionRepository
import java.sql.BatchUpdateException
import java.sql.Statement
import java.util.*
import javax.sql.DataSource

private val GET_WALLET_BALANCE_QUERY =
    """
    SELECT closing_balance, wallet_version, transaction_id
    FROM transaction_ledger
    WHERE wallet_id = ?
    ORDER BY wallet_version DESC
    LIMIT 1
    """.trimIndent()

private val GET_LAST_TRANSACTION_QUERY =
    """
    SELECT closing_balance, wallet_version, transaction_id, coins, type
    FROM transaction_ledger
    WHERE wallet_id = ?
    ORDER BY wallet_version DESC
    LIMIT 1
    """.trimIndent()

private val INSERT_TRANSACTION_COMMAND =
    """
    INSERT INTO transaction_ledger (wallet_id, wallet_version, transaction_Id, type, coins, closing_balance) 
    VALUES (?, ?, ?, ?::transaction_type, ?, ?)       
    """.trimIndent()

private const val PRIMARY_KEY_NAME = "transactions_pkey"
private const val UNIQUE_CONSTRAINT_NAME = "wallet_transaction_combo"

class PostgresTransactionRepository (private val datasource: DataSource) : TransactionRepository {

    override fun getWalletBalance(walletId: UUID): WalletBalance {
        datasource.connection.use { connection ->
            connection.prepareStatement(GET_WALLET_BALANCE_QUERY).use { preparedStatement ->
                preparedStatement.setObject(1, walletId)
                preparedStatement.executeQuery().use { resultSet ->
                    if (!resultSet.next()) {
                        throw WalletNotFoundException(walletId)
                    }
                    return WalletBalance(
                        version = resultSet.getInt("wallet_version").toInt(),
                        transactionId = resultSet.getString("transaction_id"),
                        closingBalance = resultSet.getInt("closing_balance"),
                    )
                }
            }
        }
    }

    override fun getLastTransaction(walletId: UUID): Transaction? {
        datasource.connection.use { connection ->
            connection.prepareStatement(GET_LAST_TRANSACTION_QUERY).use { preparedStatement ->
                preparedStatement.setObject(1, walletId)
                preparedStatement.executeQuery().use { resultSet ->
                    if (resultSet.next()) {
                        return Transaction(
                            walletId = walletId,
                            transactionId = resultSet.getString("transaction_id"),
                            closingBalance = resultSet.getInt("closing_balance"),
                            coins = resultSet.getInt("coins"),
                            version = resultSet.getInt("wallet_version"),
                            transactionType = TransactionType.valueOf(resultSet.getString("type"))
                        )
                    } else {
                        return null
                    }
                }
            }
        }
    }

    override fun saveTransactions(transactions: List<Transaction>) {
        if (transactions.isEmpty()) {
            return
        }

        datasource.connection.use { connection ->
            try {
                connection.autoCommit = false
                val statement = connection.prepareStatement(INSERT_TRANSACTION_COMMAND)
                transactions.forEach {
                    statement.setObject(1, it.walletId)
                    statement.setInt(2, it.version.toInt())
                    statement.setString(3, it.transactionId)
                    statement.setString(4, it.transactionType.name)
                    statement.setInt(5, it.coins.toInt())
                    statement.setLong(6, it.closingBalance.toLong())
                    statement.addBatch()
                }
                statement.executeBatch()
                connection.commit()
            } catch (e: BatchUpdateException) {
                connection.rollback()
                val firstFailedTransactionIndex = e.updateCounts.indexOfFirst { it == Statement.EXECUTE_FAILED }
                val failedTransaction = transactions[firstFailedTransactionIndex]
                if (e.message!!.contains(UNIQUE_CONSTRAINT_NAME)) {
                    throw DuplicateTransactionException(failedTransaction.transactionId, failedTransaction.walletId)
                }
                if (e.message!!.contains(PRIMARY_KEY_NAME)) {
                    throw VersionErrorException(failedTransaction.version, failedTransaction.walletId)
                }
                throw e
            } catch (e: Exception) {
                connection.rollback()
                throw e
            }
        }
    }
}
