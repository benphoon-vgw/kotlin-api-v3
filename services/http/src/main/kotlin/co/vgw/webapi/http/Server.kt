package co.vgw.webapi.http

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class Server(
    port: Int,
    private val controllers: List<Controller>,
) {
    private val server = embeddedServer(
        factory = Jetty,
        port = port,
    ) {
        install(ContentNegotiation) { json() }
        install(StatusPages) {
            controllers.forEach { it.setupErrorHandler(this) }
            exception<BadRequestException> { call: ApplicationCall, _ ->
                call.respond(HttpStatusCode.BadRequest)
            }
            exception<IllegalStateException> { call: ApplicationCall, _ ->
                call.respond(HttpStatusCode.BadRequest)
            }
            exception<Exception> { call: ApplicationCall, _ ->
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
        routing {
            controllers.forEach { it.setupRoutes(this) }
        }
    }

    fun startup() {
        server.start()
    }
}
