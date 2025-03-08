package com.kssidll.zapierdalo.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.SkipQueryVerification
import com.kssidll.zapierdalo.data.data.RunActionEntity
import com.kssidll.zapierdalo.data.data.view.RunAction
import com.kssidll.zapierdalo.data.data.view.RunActionSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface RunActionDao {
    // Create

    @Insert
    suspend fun insert(entity: RunActionEntity): Long

    // Update

    @Query("UPDATE RunActionEntity SET endTimestamp = :endTimestamp WHERE id = :entityId")
    suspend fun setEndTimestamp(entityId: Long, endTimestamp: Long?)

    // Delete

    @Delete
    suspend fun delete(entity: RunActionEntity)

    @Query("DELETE FROM RunActionEntity WHERE id = :entityId")
    suspend fun delete(entityId: Long)

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