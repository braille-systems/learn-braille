package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*

typealias UserLogin = String
typealias UserName = String

@Entity(tableName = "users", indices = [Index(value = ["login"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: DBid = 0,
    val login: UserLogin,
    val name: UserName
)

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(users: List<User>)

    @Query("select * from users where :login = login limit 1")
    suspend fun getUser(login: UserLogin): User?

    @Query("select * from users where :id = id limit 1")
    suspend fun getUser(id: DBid): User?

    @Query("delete from users")
    suspend fun clear()
}
