package com.kssidll.zapierdalo.data.repository

import com.kssidll.zapierdalo.data.dao.StepsDao
import com.kssidll.zapierdalo.data.data.StepsEntity
import com.kssidll.zapierdalo.domain.repository.StepsRepository

class StepsRepositoryImpl(private val dao: StepsDao): StepsRepository {

    // Create

    override suspend fun insert(entity: StepsEntity): Long {
        return dao.insert(entity)
    }

    // Update

    // Delete

    // Read

}