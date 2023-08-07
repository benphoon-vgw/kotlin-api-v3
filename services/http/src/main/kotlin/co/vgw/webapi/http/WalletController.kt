package co.vgw.webapi.http

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

class WalletController (
    private val queryHandler: QueryHandler,
) : Controller {
    override fun setupErrorHandler(statusPagesConfig: StatusPagesConfig) {
        statusPagesConfig.exception<WalletNotFoundException> { call, _ ->
            call.respond(HttpStatusCode.NotFound)
        }
    }

    override fun setupRoutes(routing: Routing) {
        routing {
            route("/wallets/{walletId}") {
                get {
                    val walletId = call.parameters["walletId"]?.let { UUID.fromString(it) }
                        ?: return@get call.respond(HttpStatusCode.BadRequest)
                    val walletBalance = queryHandler.handleBalanceQuery(BalanceQuery(walletId))
                    call.respond(
                        HttpStatusCode.OK,
                        BalanceResponse(
                            transactionId = walletBalance.transactionId,
                            coins = walletBalance.closingBalance,
                            version = walletBalance.version,
                        ),
                    )
                }
            }
        }
    }
}