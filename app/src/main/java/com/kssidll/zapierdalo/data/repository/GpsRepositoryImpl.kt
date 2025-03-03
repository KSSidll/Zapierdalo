package com.kssidll.zapierdalo.data.repository

import com.kssidll.zapierdalo.data.dao.GpsDao
import com.kssidll.zapierdalo.data.data.GpsEntity
import com.kssidll.zapierdalo.domain.repository.GpsRepository

class GpsRepositoryImpl(private val dao: GpsDao): GpsRepository {

    // Create

    override suspend fun insert(entity: GpsEntity): Long {
        return dao.insert(entity)
    }

    // Delete

    // Read

}