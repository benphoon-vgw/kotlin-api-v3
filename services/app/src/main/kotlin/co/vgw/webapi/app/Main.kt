package co.vgw.webapi.app

import co.vgw.webapi.domain.CommandHandler
import co.vgw.webapi.domain.InMemoryRepository
import co.vgw.webapi.domain.QueryHandler
import co.vgw.webapi.http.Server
import co.vgw.webapi.http.WalletController


fun main() {
    val inMemoryRepository = InMemoryRepository()
    val walletController = WalletController(QueryHandler(inMemoryRepository), CommandHandler(inMemoryRepository))
    val server = Server(Environment.serverPort, listOf(walletController))
    println("Server starting...")
    server.startup()
}
