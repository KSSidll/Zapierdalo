package com.kssidll.zapierdalo.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.SkipQueryVerification
import com.kssidll.zapierdalo.data.data.view.Gps
import com.kssidll.zapierdalo.data.data.view.RunAction
import com.kssidll.zapierdalo.domain.data.RunActionDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface RunActionDetailsDao {

    @SkipQueryVerification
    @Query("SELECT * FROM RunAction WHERE id = :id")
    fun getRunAction(id: Long): Flow<RunAction?>

    @Query("SELECT * FROM Gps WHERE runActionId = :runActionId")
    fun getGps(runActionId: Long): Flow<List<Gps>>

    fun get(runActionId: Long): Flow<RunActionDetails?> {
        return getRunAction(runActionId)
            .combine(getGps(runActionId)) { runActionP, gps ->
                runActionP?.let { runAction ->
                    RunActionDetails(
                        id = runAction.id,
                        totalDistance = runAction.totalDistance,
                        totalSteps = runAction.totalSteps,
                        startTimestamp = runAction.startTimestamp,
                        endTimestamp = runAction.endTimestamp,
                        gpsPoints = gps.map { it.location }
                    )
                }
            }
    }
}