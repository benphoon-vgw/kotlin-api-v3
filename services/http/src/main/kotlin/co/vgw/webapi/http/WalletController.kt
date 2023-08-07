package co.vgw.webapi.http

import co.vgw.webapi.domain.BalanceQuery
import co.vgw.webapi.domain.CommandHandler
import co.vgw.webapi.domain.CreditCommand
import co.vgw.webapi.domain.DebitCommand
import co.vgw.webapi.domain.DuplicateTransactionException
import co.vgw.webapi.domain.InsufficientBalanceException
import co.vgw.webapi.domain.QueryHandler
import co.vgw.webapi.domain.VersionErrorException
import co.vgw.webapi.domain.WalletNotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

class WalletController (
    private val queryHandler: QueryHandler,
    private val commandHandler: CommandHandler
) : Controller {
    override fun setupErrorHandler(statusPagesConfig: StatusPagesConfig) {
        statusPagesConfig.exception<WalletNotFoundException> { call, _ ->
            call.respond(HttpStatusCode.NotFound)
        }
        statusPagesConfig.exception<DuplicateTransactionException> { call, cause ->
            val walletBalance = queryHandler.handleBalanceQuery(BalanceQuery(cause.walletId))
            call.respond(
                HttpStatusCode.Accepted,
                BalanceResponse(walletBalance.version, walletBalance.closingBalance, walletBalance.transactionId),
            )
        }
        statusPagesConfig.exception<VersionErrorException> { call, _ ->
            call.respond(HttpStatusCode.InternalServerError)
        }
        statusPagesConfig.exception<InsufficientBalanceException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest)
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

                post("/credit") {
                    val walletId = call.parameters["walletId"]?.let { UUID.fromString(it) }
                        ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val creditRequest = call.receive<CreditRequest>()
                    commandHandler.handleCredit(
                        CreditCommand(
                            coins = creditRequest.coins,
                            walletId = walletId,
                            transactionId = creditRequest.transactionId,
                        ),
                    )
                    call.respond(HttpStatusCode.Created)
                }

                post("/debit") {
                    val walletId = call.parameters["walletId"]?.let { UUID.fromString(it) }
                        ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val debitRequest = call.receive<DebitRequest>()
                    commandHandler.handleDebit(
                        DebitCommand(
                            coins = debitRequest.coins,
                            walletId = walletId,
                            transactionId = debitRequest.transactionId,
                        ),
                    )
                    call.respond(HttpStatusCode.Created)
                }
            }
        }
    }
}
