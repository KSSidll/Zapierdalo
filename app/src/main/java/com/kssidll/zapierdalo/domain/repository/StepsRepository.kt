package com.kssidll.zapierdalo.domain.repository

import com.kssidll.zapierdalo.data.data.StepsEntity

interface StepsRepository {

    // Create

    /**
     * Inserts [StepsEntity] object into the database
     */
    suspend fun insert(entity: StepsEntity): Long

    /**
     * Inserts list of [StepsEntity] objects into the database
     */
    suspend fun insert(entities: List<StepsEntity>): List<Long>

    // Update

    // Delete

    // Read

}