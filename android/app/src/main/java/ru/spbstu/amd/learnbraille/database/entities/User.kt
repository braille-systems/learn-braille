package ru.spbstu.amd.learnbraille.database.entities

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
    fun insertUsers(users: List<User>)

    @Query("SELECT * FROM user WHERE :login = login LIMIT 1")
    fun getUser(login: String): User?

    @Query("SELECT * FROM user WHERE :id = id LIMIT 1")
    fun getUser(id: Long): User?
}
