package com.kssidll.zapierdalo.domain.repository

import com.kssidll.zapierdalo.data.data.StepsEntity

interface StepsRepository {

    // Create

    /**
     * Inserts [StepsEntity] object into the database
     */
    suspend fun insert(entity: StepsEntity): Long

    // Update

    /**
     * Set end timestamp of entity matching id = [entityId] to [endTimestamp]
     */
    suspend fun setEndTimestamp(entityId: Long, endTimestamp: Long?)

    /**
     * Set steps count of entity matching id = [entityId] to [count]
     */
    suspend fun setStepsCount(entityId: Long, count: Long)

    // Delete

    // Read

}