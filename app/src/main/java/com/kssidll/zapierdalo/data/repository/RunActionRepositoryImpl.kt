package com.kssidll.zapierdalo.data.repository

import com.kssidll.zapierdalo.data.dao.RunActionDao
import com.kssidll.zapierdalo.data.data.RunActionEntity
import com.kssidll.zapierdalo.domain.repository.RunActionRepository
import kotlinx.coroutines.flow.Flow

class RunActionRepositoryImpl(private val dao: RunActionDao): RunActionRepository {

    // Create

    override suspend fun insert(entity: RunActionEntity): Long {
        return dao.insert(entity)
    }

    // Update

    override suspend fun update(entity: RunActionEntity) {
        dao.update(entity)
    }

    // Delete

    // Read

    override fun get(id: Long): Flow<RunActionEntity?> {
        return dao.get(id)
    }

    override fun all(): Flow<List<RunActionEntity>> {
        return dao.all()
    }

    override fun latest(): Flow<RunActionEntity?> {
        return dao.latest()
    }
}