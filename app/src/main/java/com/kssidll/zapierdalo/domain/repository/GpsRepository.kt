package com.kssidll.zapierdalo.domain.repository

import com.kssidll.zapierdalo.data.data.GpsEntity
import com.kssidll.zapierdalo.data.data.RunActionEntity
import kotlinx.coroutines.flow.Flow

interface GpsRepository {

    // Create

    /**
     * Inserts [GpsEntity] object into the database
     */
    suspend fun insert(entity: GpsEntity): Long

    // Delete

    // Read

    /**
     * Returns a flow of all [GpsEntity] objects matching [runActionId]
     * @param runActionId Id of the [RunActionEntity] to match the [GpsEntity] object with
     */
    fun byRunActionId(runActionId: Long): Flow<List<GpsEntity>>

    /**
     * Returns a flow of all [GpsEntity] objects
     */
    fun all(): Flow<List<GpsEntity>>
}