package com.github.imdpkj.helpers

import com.github.imdpkj.repository.User
import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication

/**
 * DPKJ
 * 02/05/20
 */

val ApplicationCall.user get() = authentication.principal<User>()