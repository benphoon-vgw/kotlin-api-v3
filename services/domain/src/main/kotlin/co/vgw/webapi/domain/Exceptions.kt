package co.vgw.webapi.domain

import java.util.*

data class WalletNotFoundException(
    val walletId: UUID
) : Exception("This action on wallet[${walletId}] cannot be processed as the wallet does not exist.")

data class DuplicateTransactionException(
    val transactionId: String,
    val walletId: UUID
) : Exception("Transaction [${transactionId}] already exists for wallet [$walletId}].")

data class VersionErrorException(
    val version: Int,
    val walletId: UUID
) : Exception("Version [${version}] of wallet [${walletId}] has already been processed.")

data class InsufficientBalanceException(
    val transactionId: String,
    val walletId: UUID
) : Exception("Insufficient balance in wallet [${walletId}] to complete transaction [${transactionId}].")
