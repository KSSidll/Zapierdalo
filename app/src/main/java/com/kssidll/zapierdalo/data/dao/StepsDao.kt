package com.kssidll.zapierdalo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kssidll.zapierdalo.data.data.StepsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StepsDao {
    // Create

    @Insert
    suspend fun insert(entity: StepsEntity): Long

    // Update

    @Update
    suspend fun update(entity: StepsEntity)

    // Delete

    @Delete
    suspend fun delete(entity: StepsEntity)

    // Read

    @Query("SELECT StepsEntity.* FROM StepsEntity WHERE StepsEntity.id = :id")
    fun get(id: Long): Flow<StepsEntity?>

    @Query("SELECT StepsEntity.* FROM StepsEntity WHERE StepsEntity.runActionId = :runActionId")
    fun byRunActionId(runActionId: Long): Flow<List<StepsEntity>>

    @Query("SELECT StepsEntity.* FROM StepsEntity")
    fun all(): Flow<List<StepsEntity>>
}