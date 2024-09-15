package com.kssidll.zapierdalo.domain.repository

import com.kssidll.zapierdalo.data.data.RunActionEntity
import com.kssidll.zapierdalo.data.data.StepsEntity
import kotlinx.coroutines.flow.Flow

interface StepsRepository {

    // Create

    /**
     * Inserts [StepsEntity] object into the database
     */
    suspend fun insert(entity: StepsEntity): Long

    // Update

    /**
     * Update [StepsEntity] object in the database
     * Matches by id
     */
    suspend fun update(entity: StepsEntity)

    // Delete

    // Read

    /**
     * Returns a flow of [StepsEntity] matching [id]
     * @param id Id of the [StepsEntity] to match with
     */
    fun get(id: Long): Flow<StepsEntity?>

    /**
     * Returns a flow of all [StepsEntity] objects matching [runActionId]
     * @param runActionId Id of the [RunActionEntity] to match the [StepsEntity] object with
     */
    fun byRunActionId(runActionId: Long): Flow<List<StepsEntity>>

    /**
     * Returns a flow of all [StepsEntity] objects
     */
    fun all(): Flow<List<StepsEntity>>
}