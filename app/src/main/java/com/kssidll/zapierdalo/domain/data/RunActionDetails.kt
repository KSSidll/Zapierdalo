package com.kssidll.zapierdalo.domain.data

import com.kssidll.zapierdalo.data.data.RunActionEntity
import kotlinx.coroutines.flow.Flow

data class RunActionDetails(
    val entity: RunActionEntity,
    val startTimestamp: Long,
    val endTimestamp: Long?,

    val totalDistance: Flow<Double>,
    val totalSteps: Flow<Long>,
    val gpsPoints: Flow<List<Gps>>
)

fun RunActionDetails.toEntity() = this.entity

fun RunActionEntity.toDomain(
    totalDistance: Flow<Double>,
    totalSteps: Flow<Long>,
    gpsPoints: Flow<List<Gps>>
) = RunActionDetails(
    entity = this,
    startTimestamp = startTimestamp,
    endTimestamp = endTimestamp,
    totalDistance = totalDistance,
    totalSteps = totalSteps,
    gpsPoints = gpsPoints
)
