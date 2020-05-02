package com.github.imdpkj.api

import com.github.imdpkj.helpers.user
import com.github.imdpkj.repository.CounterService
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route

/**
 * DPKJ
 * 02/05/20
 */
fun Route.counter() {
    route("/counter") {
        get("/current") {
            val user = call.user!!

            call.respond(CounterService.peek(user.id))
        }

        put("/next") {
            val user = call.user!!
            call.respond(CounterService.next(user.id))
        }

        put("/reset") {
            val user = call.user!!

            call.respond(CounterService.reset(user.id, call.receive()))
        }
    }
}