package com.kssidll.zapierdalo.domain.repository

import androidx.paging.PagingSource
import com.kssidll.zapierdalo.data.data.RunActionEntity
import com.kssidll.zapierdalo.data.data.view.RunAction
import com.kssidll.zapierdalo.data.data.view.RunActionSummary
import kotlinx.coroutines.flow.Flow

interface RunActionRepository {

    // Create

    /**
     * Inserts [RunActionEntity] object into the database
     */
    suspend fun insert(entity: RunActionEntity): Long

    // Update

    /**
     * Set end timestamp of entity matching id = [entityId] to [endTimestamp]
     */
    suspend fun setEndTimestamp(entityId: Long, endTimestamp: Long?)

    // Delete

    /**
     * Delete [RunActionEntity] object from the database
     * Matches by id
     */
    suspend fun delete(entity: RunActionEntity)

    /**
     * Delete [RunActionEntity] object from the database
     * Matches by id
     */
    suspend fun delete(entityId: Long)

    // Read

    /**
     * Returns a flow of [RunAction] object matching [id]
     * @param id Id of the object to match with
     */
    fun get(id: Long): Flow<RunAction?>

    /**
     * Returns a flow of [RunActionSummary] object matching [id]
     * @param id Id of the object to match with
     */
    fun getSummary(id: Long): Flow<RunActionSummary?>

    /**
     * Returns [PagingSource] of [RunActionSummary] objects
     */
    fun allSummaryPaged(): PagingSource<Int, RunActionSummary>
}