package com.kssidll.zapierdalo.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.SkipQueryVerification
import androidx.room.Transaction
import com.kssidll.zapierdalo.data.data.GpsEntity
import com.kssidll.zapierdalo.data.data.RunActionEntity
import com.kssidll.zapierdalo.data.data.shadow.GpsLengthShadow
import com.kssidll.zapierdalo.data.data.shadow.StepsAmountShadow
import com.kssidll.zapierdalo.data.data.view.RunAction
import com.kssidll.zapierdalo.data.data.view.RunActionSummary
import com.kssidll.zapierdalo.helper.toGeoPoint
import kotlinx.coroutines.flow.Flow

@Dao
interface RunActionDao {
    // Shadow
    @Query(StepsAmountShadow.DELETE_QUERY)
    suspend fun mDeleteStepsShadow()

    @Query(StepsAmountShadow.REINSERT_QUERY)
    suspend fun mReinsertStepsShadow()

    @Transaction
    suspend fun refreshStepsShadow() {
        mDeleteStepsShadow()
        mReinsertStepsShadow()
    }

    @Query(GpsLengthShadow.DELETE_QUERY)
    suspend fun mDeleteGpsShadow()

    @Insert
    suspend fun mInsertGpsShadow(shadow: List<GpsLengthShadow>)

    @Query("SELECT * FROM GpsEntity")
    suspend fun mAllGps(): List<GpsEntity>

    @Transaction
    suspend fun mInsertGpsShadow() {
        val data = mutableListOf<GpsLengthShadow>()

        mAllGps().filter { it.accuracy < 9f }.groupBy { it.runActionId }.forEach {
            data.add(
                GpsLengthShadow(
                    runActionId = it.key,
                    geoPointList = it.value.toGeoPoint()
                )
            )
        }

        mInsertGpsShadow(data)
    }

    @Transaction
    suspend fun refreshGpsShadow() {
        mDeleteGpsShadow()
        mInsertGpsShadow()
    }

    @Transaction
    suspend fun refreshShadow() {
        refreshStepsShadow()
        refreshGpsShadow()
    }

    // Create

    @Insert
    suspend fun insert(entity: RunActionEntity): Long

    // Update

    @Query("UPDATE RunActionEntity SET endTimestamp = :endTimestamp WHERE id = :entityId")
    suspend fun setEndTimestamp(entityId: Long, endTimestamp: Long?)

    // Delete

    @Delete
    suspend fun mDelete(entity: RunActionEntity)

    @Query("DELETE FROM RunActionEntity WHERE id = :entityId")
    suspend fun mDelete(entityId: Long)

    @Transaction
    suspend fun delete(entity: RunActionEntity) {
        mDelete(entity)

        refreshShadow()
    }

    @Transaction
    suspend fun delete(entityId: Long) {
        mDelete(entityId)

        refreshShadow()
    }

    // Read

    @SkipQueryVerification
    @Query("SELECT * FROM RunAction WHERE id = :id")
    fun get(id: Long): Flow<RunAction?>

    @SkipQueryVerification
    @Query("SELECT * FROM RunActionSummary WHERE id = :id")
    fun getSummary(id: Long): Flow<RunActionSummary?>

    @SkipQueryVerification
    @Query("SELECT * FROM RunActionSummary ORDER BY id DESC")
    fun allPagedSummary(): PagingSource<Int, RunActionSummary>
}