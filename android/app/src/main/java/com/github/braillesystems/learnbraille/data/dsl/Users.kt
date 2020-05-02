package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.User


class _Users(block: _Users.() -> Unit) {

    private val _users = mutableListOf<User>()
    internal val users: List<User>
        get() = _users

    init {
        block()
    }

    fun user(login: String, name: String) {
        _users += User(
            login = login,
            name = name
        )
    }
}
