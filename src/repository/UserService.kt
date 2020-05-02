package com.github.imdpkj.repository

import com.github.imdpkj.helpers.UserDoesNotExists
import com.github.imdpkj.helpers.UserExists
import io.ktor.auth.Principal
import org.mindrot.jbcrypt.BCrypt
import java.util.*


/**
 * DPKJ
 * 02/05/20
 */

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val password: String
) : Principal {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }
}

data class RegisterUser(
    val name: String,
    val email: String,
    val password: String
)

data class LoginUser(
    val email: String,
    val password: String
)

object UserService {
    private val users = mutableSetOf<User>()

    fun findById(id: String): User? = users.firstOrNull { it.id.toString() == id }

    private fun findByEmail(email: String): User? = users.firstOrNull { it.email == email }

    fun upsert(user: RegisterUser): User {
        synchronized(users) {
            val stored = findByEmail(user.email)

            if (stored != null) throw UserExists()

            return create(user)
        }
    }

    private fun create(input: RegisterUser): User {
        val user = User(
            id = UUID.randomUUID(),
            name = input.name,
            email = input.email,
            password = input.password.hash()
        )

        users.add(user)

        return user
    }

    fun findByEmailAndPassword(login: LoginUser): User {
        return users.firstOrNull { it.email == login.email && login.password.match(it.password) }
            ?: throw UserDoesNotExists()
    }


}

fun String.hash(): String = BCrypt.hashpw(this, BCrypt.gensalt())
fun String.match(hash: String?) = !hash.isNullOrBlank() && BCrypt.checkpw(this, hash)
