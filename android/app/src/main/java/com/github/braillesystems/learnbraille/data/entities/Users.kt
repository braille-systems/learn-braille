package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*

@Entity(tableName = "users", primaryKeys = ["id", "login"])
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val login: String,
    val name: String
)

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(users: List<User>)

    @Query("select * from users where :login = login limit 1")
    suspend fun getUser(login: String): User?

    @Query("select * from users where :id = id limit 1")
    suspend fun getUser(id: Long): User?
}
