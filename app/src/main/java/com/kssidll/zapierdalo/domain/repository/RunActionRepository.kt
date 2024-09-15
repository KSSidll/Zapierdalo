package com.kssidll.zapierdalo.domain.repository

import com.kssidll.zapierdalo.data.data.GpsEntity
import com.kssidll.zapierdalo.data.data.RunActionEntity
import kotlinx.coroutines.flow.Flow

interface RunActionRepository {

    // Create

    /**
     * Inserts [GpsEntity] object into the database
     */
    suspend fun insert(entity: RunActionEntity): Long

    // Update

    /**
     * Update [RunActionEntity] object in the database
     * Matches by id
     */
    suspend fun update(entity: RunActionEntity)

    // Delete

    // Read

    /**
     * Returns a flow of [RunActionEntity] object matching [id]
     * @param id Id of the object to match with
     */
    fun get(id: Long): Flow<RunActionEntity?>

    /**
     * Returns a flow of all [RunActionEntity] objects
     */
    fun all(): Flow<List<RunActionEntity>>

    /**
     * Returns a flow of latest [RunActionEntity] object
     */
    fun latest(): Flow<RunActionEntity?>
}