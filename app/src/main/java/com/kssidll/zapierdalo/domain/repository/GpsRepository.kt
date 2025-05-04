package com.kssidll.zapierdalo.domain.repository

import com.kssidll.zapierdalo.data.data.GpsEntity
import kotlinx.coroutines.flow.Flow

interface GpsRepository {

    // Create

    /**
     * Inserts [GpsEntity] object into the database
     */
    suspend fun insert(entity: GpsEntity): Long

    // Delete

    // Read

    fun forRunAction(runActionId: Long): Flow<List<GpsEntity>>

}