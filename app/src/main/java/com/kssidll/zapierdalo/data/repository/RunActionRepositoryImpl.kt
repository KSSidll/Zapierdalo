package com.kssidll.zapierdalo.data.repository

import androidx.paging.PagingSource
import com.kssidll.zapierdalo.data.dao.RunActionDao
import com.kssidll.zapierdalo.data.data.RunActionEntity
import com.kssidll.zapierdalo.data.data.view.RunAction
import com.kssidll.zapierdalo.data.data.view.RunActionSummary
import com.kssidll.zapierdalo.domain.repository.RunActionRepository
import kotlinx.coroutines.flow.Flow

class RunActionRepositoryImpl(private val dao: RunActionDao): RunActionRepository {

    // Create

    override suspend fun insert(entity: RunActionEntity): Long {
        return dao.insert(entity)
    }

    // Update

    override suspend fun setEndTimestamp(entityId: Long, endTimestamp: Long?) {
        dao.setEndTimestamp(entityId, endTimestamp)
    }

    // Delete

    override suspend fun delete(entity: RunActionEntity) {
        dao.delete(entity)
    }

    override suspend fun delete(entityId: Long) {
        dao.delete(entityId)
    }

    // Read

    override fun get(id: Long): Flow<RunAction?> {
        return dao.get(id)
    }

    override fun getSummary(id: Long): Flow<RunActionSummary?> {
        return dao.getSummary(id)
    }

    override fun allSummaryPaged(): PagingSource<Int, RunActionSummary> {
        return dao.allPagedSummary()
    }
}