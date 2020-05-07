package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.User
import com.github.braillesystems.learnbraille.data.entities.UserDao


interface UserRepository {

    suspend fun insert(user: User)
    suspend fun getUser(id: Long): User?
    suspend fun getUser(login: String): User?
}

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {

    override suspend fun insert(user: User): Unit = userDao.insert(user)
    override suspend fun getUser(id: Long): User? = userDao.getUser(id)
    override suspend fun getUser(login: String): User? = userDao.getUser(login)
}
