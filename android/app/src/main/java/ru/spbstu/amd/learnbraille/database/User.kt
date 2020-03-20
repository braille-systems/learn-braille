package ru.spbstu.amd.learnbraille.database

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
    fun insertUser(users: User)

    @Query("SELECT id FROM user WHERE :login = login")
    fun getId(login: String): Long?

    @Query("SELECT first_name FROM user WHERE :id = id")
    fun getFirstName(id: Long): String?

    @Query("SELECT second_name FROM user WHERE :id = id")
    fun getSecondName(id: Long): String?
}

// TODO load from resources
val DEFAULT_USER = User(
    login = "default",
    firstName = "John",
    secondName = "Smith"
)
