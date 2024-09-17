package com.kssidll.zapierdalo.domain.data

import com.kssidll.zapierdalo.data.data.RunActionEntity
import kotlinx.serialization.Serializable

@Serializable
data class RunAction(
    val entity: RunActionEntity,
    val startTimestamp: Long,
    val endTimestamp: Long?,
)

fun RunAction.toEntity() = this.entity

fun RunActionEntity.toDomain() = RunAction(
    entity = this,
    startTimestamp = startTimestamp,
    endTimestamp = endTimestamp
)