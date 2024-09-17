package com.kssidll.zapierdalo.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kssidll.zapierdalo.data.data.RunActionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunActionDao {
    // Create

    @Insert
    suspend fun insert(entity: RunActionEntity): Long

    // Update

    @Update
    suspend fun update(entity: RunActionEntity)

    // Delete

    @Delete
    suspend fun delete(entity: RunActionEntity)

    // Read

    @Query("SELECT RunActionEntity.* FROM RunActionEntity WHERE RunActionEntity.id = :id")
    fun get(id: Long): Flow<RunActionEntity?>

    @Query("SELECT RunActionEntity.* FROM RunActionEntity ORDER BY RunActionEntity.id DESC")
    fun all(): Flow<List<RunActionEntity>>

    @Query("SELECT RunActionEntity.* FROM RunActionEntity ORDER BY RunActionEntity.id DESC")
    fun allPaged(): PagingSource<Int, RunActionEntity>

    @Query("SELECT RunActionEntity.* FROM RunActionEntity ORDER BY RunActionEntity.id DESC LIMIT 1")
    fun latest(): Flow<RunActionEntity?>
}