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

    override suspend fun setEndTimestamp(entityId: Long, endTimestamp: Long?) {
        dao.setEndTimestamp(entityId, endTimestamp)
    }

    override suspend fun setStepsCount(entityId: Long, count: Long) {
        dao.setStepsCount(entityId, count)
    }

    // Delete

    // Read

}