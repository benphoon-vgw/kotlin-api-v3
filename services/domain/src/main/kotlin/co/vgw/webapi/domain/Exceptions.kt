package co.vgw.webapi.domain

import java.util.*

data class WalletNotFoundException(
    val walletId: UUID
) : Exception("This action on wallet[${walletId}] cannot be processed as the wallet does not exist.")