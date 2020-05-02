package com.github.imdpkj.helpers

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

/**
 * DPKJ
 * 02/05/20
 */
data class SimpleError(val code: Int, val message: String)

fun getError(error: Error) = when (error) {
    is UserExists -> SimpleError(100, "User already exists")
    is UserDoesNotExists -> SimpleError(101, "User does not exists")
    is CounterNegativeValue -> SimpleError(200, "Negative value for counter not allowed")
}

fun StatusPages.Configuration.status() {

    exception<UserExists> {
        call.respond(HttpStatusCode.UnprocessableEntity, mapOf("error" to getError(UserExists())))
    }

    exception<UserDoesNotExists> {
        call.respond(HttpStatusCode.NotFound, mapOf("error" to getError(UserDoesNotExists())))
    }

    exception<CounterNegativeValue> {
        call.respond(HttpStatusCode.PreconditionFailed, mapOf("error" to getError(CounterNegativeValue())))
    }
}