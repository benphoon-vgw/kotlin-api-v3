package co.vgw.webapi.http

import kotlinx.serialization.Serializable

@Serializable
data class BalanceResponse(
    val version: Int,
    val coins: Int,
    val transactionId: String
)