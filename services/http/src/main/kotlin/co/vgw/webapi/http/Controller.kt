package co.vgw.webapi.http

import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*

interface Controller {
    fun setupErrorHandler(statusPagesConfig: StatusPagesConfig)
    fun setupRoutes(routing: Routing)
}