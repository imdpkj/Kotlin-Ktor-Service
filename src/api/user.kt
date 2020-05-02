package com.github.imdpkj.api

import com.github.imdpkj.helpers.JWTConfig
import com.github.imdpkj.repository.UserService
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

/**
 * DPKJ
 * 02/05/20
 */

fun Route.user() {

    route("/user") {
        post("/register") {
            call.respond(UserService.upsert(call.receive()))
        }

        post("/login") {
            call.respond(JWTConfig.makeToken(UserService.findByEmailAndPassword(call.receive())))
        }
    }
}