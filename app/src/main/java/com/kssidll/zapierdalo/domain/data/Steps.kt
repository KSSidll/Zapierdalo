package com.kssidll.zapierdalo.domain.data

import com.kssidll.zapierdalo.data.data.StepsEntity

data class Steps(
    val entity: StepsEntity,
    val runAction: RunAction,
    val startTimestamp: Long,
    val endTimestamp: Long?,
    val steps: Long
)

fun Steps.toEntity(): StepsEntity = this.entity

fun StepsEntity.toDomain(runAction: RunAction): Steps = Steps(
    entity = this,
    runAction = runAction,
    startTimestamp = startTimestamp,
    endTimestamp = endTimestamp,
    steps = steps
)
