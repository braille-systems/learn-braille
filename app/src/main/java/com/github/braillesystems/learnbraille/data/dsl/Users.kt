package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.User
import com.github.braillesystems.learnbraille.data.entities.UserLogin
import com.github.braillesystems.learnbraille.data.entities.UserName

@DataBuilderMarker
class UsersBuilder(block: UsersBuilder.() -> Unit) {

    private val _users = mutableListOf<User>()
    internal val users: List<User>
        get() = _users

    init {
        block()
    }

    fun user(login: UserLogin, name: UserName) {
        _users += User(
            login = login,
            name = name
        )
    }
}
