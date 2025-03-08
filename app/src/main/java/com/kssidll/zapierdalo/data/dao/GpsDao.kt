package com.kssidll.zapierdalo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.kssidll.zapierdalo.data.data.GpsEntity

@Dao
interface GpsDao {
    // Create

    @Insert
    suspend fun insert(entity: GpsEntity): Long

    // Delete

    // Read

}