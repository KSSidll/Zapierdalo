package com.kssidll.zapierdalo.domain.repository

import com.kssidll.zapierdalo.data.data.StepsEntity

interface StepsRepository {

    // Create

    /**
     * Inserts [StepsEntity] object into the database
     */
    suspend fun insert(entity: StepsEntity): Long

    // Update

    // Delete

    // Read

}