package com.kssidll.zapierdalo.data.data.shadow

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(
    tableName = "StepsAmountShadow"
)
data class StepsAmountShadow(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val runActionId: Long,
    val totalSteps: Long
) {
    companion object {
        @Ignore
        const val DELETE_QUERY = """
            DELETE FROM StepsAmountShadow
        """

        @Ignore
        const val REINSERT_QUERY = """
            INSERT INTO StepsAmountShadow (runActionId, totalSteps) 
            SELECT
                runActionId,
                SUM(steps) as totalSteps
            FROM StepsEntity
            GROUP BY runActionId
        """
    }
}

data class StepsAmount(
    val runActionId: Long,
    val totalSteps: Long
) {
    companion object {
        fun fromShadow(shadow: StepsAmountShadow): StepsAmount {
            return StepsAmount(
                runActionId = shadow.runActionId,
                totalSteps = shadow.totalSteps
            )
        }

        fun fromShadow(shadow: List<StepsAmountShadow>): List<StepsAmount> {
            return shadow.map {
                fromShadow(it)
            }
        }
    }
}
