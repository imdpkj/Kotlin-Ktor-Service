package com.github.imdpkj.repository

import com.github.imdpkj.helpers.CounterNegativeValue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * DPKJ
 * 02/05/20
 */
data class Counter(val value: Int)

class UserCounter {
    var counter = 0
    val mutex = Mutex()

    fun peek() = counter

    suspend fun increment() = mutex.withLock {
        ++counter
    }

    suspend fun reset(value: Int) = mutex.withLock {
        counter = value

        counter
    }
}

object CounterService {

    val counters = ConcurrentHashMap<UUID, UserCounter>()

    fun peek(id: UUID): Counter {
        return Counter(read(id).peek())
    }

    fun next(id: UUID): Counter {
        return runBlocking {
            Counter(read(id).increment())
        }
    }

    fun reset(id: UUID, receive: Counter): Counter {
        if (receive.value < 0) throw CounterNegativeValue()
        return runBlocking {
            Counter(read(id).reset(receive.value))
        }
    }

    fun read(uuid: UUID): UserCounter {
        return counters.getOrPut(uuid) { UserCounter() }
    }
}

