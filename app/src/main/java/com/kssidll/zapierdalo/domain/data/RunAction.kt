package com.kssidll.zapierdalo.domain.data

import com.kssidll.zapierdalo.data.data.RunActionEntity
import kotlinx.coroutines.flow.Flow

data class RunAction(
    val entity: RunActionEntity,
    val startTimestamp: Long,
    val endTimestamp: Long?,

    val totalDistance: Flow<Double>,
    val totalSteps: Flow<Long>
)

fun RunAction.toEntity() = this.entity

fun RunActionEntity.toDomain(
    totalDistance: Flow<Double>,
    totalSteps: Flow<Long>,
) = RunAction(
    entity = this,
    startTimestamp = startTimestamp,
    endTimestamp = endTimestamp,
    totalDistance = totalDistance,
    totalSteps = totalSteps
)