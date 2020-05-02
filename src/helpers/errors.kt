package com.github.imdpkj.helpers

/**
 * DPKJ
 * 02/05/20
 */

sealed class Error : RuntimeException()

class UserExists : Error()

class UserDoesNotExists : Error()

class CounterNegativeValue : Error()