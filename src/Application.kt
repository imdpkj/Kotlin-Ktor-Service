package com.github.imdpkj

import com.fasterxml.jackson.databind.SerializationFeature
import com.github.imdpkj.api.counter
import com.github.imdpkj.api.user
import com.github.imdpkj.helpers.configure
import com.github.imdpkj.helpers.status
import com.github.imdpkj.repository.CounterService
import com.github.imdpkj.repository.UserService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Beware of this risk, and try to limit it!
    }

    install(Authentication) {

        jwt { configure() }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {

        install(StatusPages) {
            status()
        }

        //Root status endpoint
        get("/") {
            call.respondText("UP", contentType = ContentType.Text.Plain)
        }

        //Version one of rest endpoints
        route("/api/v1/") {
            user()

            authenticate {
                counter()
            }
        }
    }
}



