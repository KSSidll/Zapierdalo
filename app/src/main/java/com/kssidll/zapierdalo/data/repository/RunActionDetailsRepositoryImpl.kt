package com.kssidll.zapierdalo.data.repository

import com.kssidll.zapierdalo.data.dao.RunActionDetailsDao
import com.kssidll.zapierdalo.domain.data.RunActionDetails
import com.kssidll.zapierdalo.domain.repository.RunActionDetailsRepository
import kotlinx.coroutines.flow.Flow

class RunActionDetailsRepositoryImpl(private val dao: RunActionDetailsDao): RunActionDetailsRepository {

    override fun get(id: Long): Flow<RunActionDetails?> {
        return dao.get(id)
    }
}