package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.Action
import com.github.braillesystems.learnbraille.data.entities.ActionDao
import com.github.braillesystems.learnbraille.data.entities.ActionType
import com.github.braillesystems.learnbraille.utils.Days
import com.github.braillesystems.learnbraille.utils.minus
import com.github.braillesystems.learnbraille.utils.scope
import kotlinx.coroutines.launch
import java.util.*

typealias Actions = List<Action>

interface ActionsRepository {
    suspend fun actionsFrom(days: Days): Actions
}

interface MutableActionsRepository : ActionsRepository {
    suspend fun addAction(type: ActionType)
    suspend fun clearAllStats()
}

class ActionsRepositoryImpl(
    private val actionsDao: ActionDao,
    private val getCurrDate: () -> Date = { Date() },
    private val keepActionsTime: Days = Days(30)
) : MutableActionsRepository {

    override suspend fun addAction(type: ActionType) =
        actionsDao.insert(
            Action(type = type, date = getCurrDate())
        )

    override suspend fun clearAllStats() = actionsDao.clear()

    override suspend fun actionsFrom(days: Days): Actions =
        actionsDao
            .allActionsSince((getCurrDate() - days).time)
            .also {
                scope().launch {
                    actionsDao.removeAllActionsBefore((getCurrDate() - keepActionsTime).time)
                }
            }
}
