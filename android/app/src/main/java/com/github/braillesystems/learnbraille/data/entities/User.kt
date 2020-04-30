package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*

@Entity(tableName = "user")
data class User(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    val login: String,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "second_name")
    val secondName: String
)

@Dao
interface UserDao {

    @Insert
    suspend fun insertUsers(users: List<User>)

    @Query("SELECT * FROM user WHERE :login = login LIMIT 1")
    suspend fun getUser(login: String): User?

    @Query("SELECT * FROM user WHERE :id = id LIMIT 1")
    suspend fun getUser(id: Long): User?
}
