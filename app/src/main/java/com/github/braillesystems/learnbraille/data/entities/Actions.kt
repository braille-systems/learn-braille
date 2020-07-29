package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import java.util.*

@Entity(tableName = "actions")
data class Action(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val type: ActionType,
    val date: Date = Date()
)

@Dao
interface ActionDao {

    @Insert
    suspend fun insert(vararg actions: Action)

    @Query("select * from actions where date >= :date")
    suspend fun getAllActionsSince(date: Long): List<Action>

    @Query("delete from actions")
    suspend fun clear()

    @Query("delete from actions where date < :date")
    suspend fun removeAllActionsBefore(date: Long)
}

@Serializable
sealed class ActionType

@Serializable
sealed class PracticeAction : ActionType()

@Serializable
object PracticeHintAction : PracticeAction()

@Serializable
data class PracticeSubmission(val isCorrect: Boolean) : ActionType()

@Serializable
sealed class TheoryActions : ActionType()

@Serializable
data class TheoryPassStep(val isInput: Boolean) : TheoryActions()

class ActionTypeConverters {

    @UnstableDefault
    @TypeConverter
    fun to(t: ActionType) = Json.stringify(ActionType.serializer(), t)

    @UnstableDefault
    @TypeConverter
    fun from(s: String) = Json.parse(ActionType.serializer(), s)
}
