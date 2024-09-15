package com.kssidll.zapierdalo.data.repository

import com.kssidll.zapierdalo.data.dao.StepsDao
import com.kssidll.zapierdalo.data.data.StepsEntity
import com.kssidll.zapierdalo.domain.repository.StepsRepository
import kotlinx.coroutines.flow.Flow

class StepsRepositoryImpl(private val dao: StepsDao): StepsRepository {

    // Create

    override suspend fun insert(entity: StepsEntity): Long {
        return dao.insert(entity)
    }

    // Update

    override suspend fun update(entity: StepsEntity) {
        dao.update(entity)
    }

    // Delete

    // Read

    override fun get(id: Long): Flow<StepsEntity?> {
        return dao.get(id)
    }

    override fun byRunActionId(runActionId: Long): Flow<List<StepsEntity>> {
        return dao.byRunActionId(runActionId)
    }

    override fun all(): Flow<List<StepsEntity>> {
        return dao.all()
    }
}