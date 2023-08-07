package co.vgw.webapi.app

import co.vgw.webapi.database.PostgresTransactionRepository
import co.vgw.webapi.domain.CommandHandler
import co.vgw.webapi.domain.QueryHandler
import co.vgw.webapi.http.Server
import co.vgw.webapi.http.WalletController
import org.postgresql.ds.PGSimpleDataSource

fun main() {
    val dataSource = PGSimpleDataSource()
    dataSource.serverNames = arrayOf(Environment.dbHost)
    dataSource.databaseName = Environment.dbName
    dataSource.user = Environment.dbUser
    dataSource.password = Environment.dbPassword

    val repository = PostgresTransactionRepository(dataSource)
    val walletController = WalletController(QueryHandler(repository), CommandHandler(repository))
    val server = Server(Environment.serverPort, listOf(walletController))
    println("Server starting...")
    server.startup()
}
