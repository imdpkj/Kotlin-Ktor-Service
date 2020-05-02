package com.github.imdpkj.helpers

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.github.imdpkj.repository.User
import com.github.imdpkj.repository.UserService
import io.ktor.auth.jwt.JWTAuthenticationProvider
import java.util.*

/**
 * DPKJ
 * 02/05/20
 */
data class JWTToken(val token: String)

object JWTConfig {
    const val ISSUER = "imdpkj.github.com"
    private const val SECRET = "random_secret" // has to come from environment
    private const val VALIDITY = 3_600_000 //1 Hour

    private val algorithm = Algorithm.HMAC512(SECRET)

    val verifier: JWTVerifier = JWT.require(algorithm).withIssuer(ISSUER).build()

    fun makeToken(user: User): JWTToken = JWTToken(JWT.create()
        .withSubject("Auth")
        .withIssuer(ISSUER)
        .withClaim("id", user.id.toString())
        .withClaim("email", user.email)
        .withExpiresAt(Date(System.currentTimeMillis() + VALIDITY))
        .sign(algorithm))

}

fun JWTAuthenticationProvider.Configuration.configure() {
    verifier(JWTConfig.verifier)
    realm = JWTConfig.ISSUER

    validate {
        it.payload.getClaim("id").asString()?.let(UserService::findById)
    }
}