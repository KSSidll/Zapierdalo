package com.kssidll.zapierdalo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.kssidll.zapierdalo.data.data.StepsEntity
import com.kssidll.zapierdalo.data.data.shadow.StepsAmountShadow

@Dao
interface StepsDao {
    // Shadow
    // TODO optimise by adding per RunAction separation

    @Query(StepsAmountShadow.DELETE_QUERY)
    suspend fun mDeleteShadow()

    @Query(StepsAmountShadow.REINSERT_QUERY)
    suspend fun mReinsertShadow()

    @Transaction
    suspend fun refreshShadow() {
        mDeleteShadow()
        mReinsertShadow()
    }

    // Create

    @Transaction
    suspend fun insert(entity: StepsEntity): Long {
        val entityId = mInsert(entity)
        refreshShadow()

        return entityId
    }

    @Insert
    suspend fun mInsert(entity: StepsEntity): Long

    // Update

    // Delete

    // Read

}