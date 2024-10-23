package com.kssidll.zapierdalo.data.repository

import androidx.paging.PagingSource
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

    override suspend fun delete(entity: RunActionEntity) {
        dao.delete(entity)
    }

    // Read

    override fun get(id: Long): Flow<RunActionEntity?> {
        return dao.get(id)
    }

    override fun all(): Flow<List<RunActionEntity>> {
        return dao.all()
    }

    override fun allPaged(): PagingSource<Int, RunActionEntity> {
        return dao.allPaged()
    }

    override fun latest(): Flow<RunActionEntity?> {
        return dao.latest()
    }
}