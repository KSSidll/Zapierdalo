package com.kssidll.zapierdalo.data.repository

import com.kssidll.zapierdalo.data.dao.GpsDao
import com.kssidll.zapierdalo.data.data.GpsEntity
import com.kssidll.zapierdalo.domain.repository.GpsRepository
import kotlinx.coroutines.flow.Flow

class GpsRepositoryImpl(private val dao: GpsDao): GpsRepository {

    // Create

    override suspend fun insert(entity: GpsEntity): Long {
        return dao.insert(entity)
    }

    // Delete

    // Read

    override fun byRunActionId(runActionId: Long): Flow<List<GpsEntity>> {
        return dao.byRunActionId(runActionId)
    }

    override fun all(): Flow<List<GpsEntity>> {
        return dao.all()
    }
}