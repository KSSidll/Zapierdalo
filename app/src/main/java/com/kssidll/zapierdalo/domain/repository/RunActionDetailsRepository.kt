package com.kssidll.zapierdalo.domain.repository

import com.kssidll.zapierdalo.domain.data.RunActionDetails
import kotlinx.coroutines.flow.Flow

interface RunActionDetailsRepository {

    /**
     * Returns a flow of [RunActionDetails] object matching [id]
     * @param id Id of the object to match with
     */
    fun get(id: Long): Flow<RunActionDetails?>
}
