package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*

@Entity(tableName = "users", indices = [Index(value = ["login"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val login: String,
    val name: String
)

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(users: List<User>)

    @Query("select * from users where :login = login limit 1")
    suspend fun getUser(login: String): User?

    @Query("select * from users where :id = id limit 1")
    suspend fun getUser(id: Long): User?
}
