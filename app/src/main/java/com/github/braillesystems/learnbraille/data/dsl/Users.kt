package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.User


@DataBuilderMarker
class UsersBuilder(block: UsersBuilder.() -> Unit) {

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
