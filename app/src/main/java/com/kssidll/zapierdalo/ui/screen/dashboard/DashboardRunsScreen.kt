package com.kssidll.zapierdalo.ui.screen.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.kssidll.zapierdalo.DAY_IN_MILIS
import com.kssidll.zapierdalo.ExpandedPreviews
import com.kssidll.zapierdalo.R
import com.kssidll.zapierdalo.ui.theme.Typography
import com.kssidll.zapierdalo.ui.theme.ZapierdaloTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

@Composable
fun DashboardRunsScreen(
    uiState: DashboardUiState,
    onEvent: (event: DashboardEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val runActionList = uiState.runActionSummaryData.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier
    ) { innerPaddingValues ->
        LazyColumn(
            state = uiState.runsScreenListState,
            modifier = Modifier
                .padding(innerPaddingValues)
                .consumeWindowInsets(innerPaddingValues)
                .fillMaxSize()
        ) {
            // TODO display total distance run and such stats

            items(
                count = runActionList.itemCount,
                key = { index ->
                    when (val ephemeral = runActionList.peek(index)) {
                        is RunActionElement.Element -> ephemeral.data.id
                        is RunActionElement.Separator -> Long.MIN_VALUE + index
                        null -> Long.MIN_VALUE + index
                    }
                },
                contentType = { index ->
                    when (runActionList.peek(index)) {
                        is RunActionElement.Element -> RunActionElement.Element::class
                        is RunActionElement.Separator -> RunActionElement.Separator::class
                        null -> null
                    }
                },
            ) { index ->
                val runActionElement = runActionList[index]

                if (runActionElement != null) {
                    when (runActionElement) {
                        is RunActionElement.Element -> {
                            val runAction = runActionElement.data

                            Column(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .clickable {
                                        onEvent(DashboardEvent.NavigateRunAction(runAction))
                                    }
                            ) {
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
                                        contentDescription = null
                                    )

                                    val timeFormatter =
                                        SimpleDateFormat("HH:mm", Locale.getDefault())
                                    val textStyle = Typography.labelLarge

                                    Text(
                                        text = timeFormatter.format(runAction.startTimestamp),
                                        style = textStyle
                                    )

                                    AnimatedVisibility(
                                        visible = runAction.endTimestamp != null,
                                        enter = fadeIn(),
                                        exit = fadeOut()
                                    ) {
                                        runAction.endTimestamp?.let { endTimestamp ->
                                            Row {
                                                Spacer(modifier = Modifier.width(2.dp))

                                                Text(
                                                    text = "-",
                                                    style = textStyle
                                                )

                                                Spacer(modifier = Modifier.width(2.dp))

                                                Text(
                                                    text = timeFormatter.format(endTimestamp),
                                                    style = textStyle
                                                )
                                            }
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp)
                                ) {
                                    val textStyle = Typography.labelLarge

                                    val distance =
                                        if (runAction.totalDistance >= 1000.0) {
                                            Pair(
                                                "%.1f".format((runAction.totalDistance.toLong() / 100).toDouble() / 10),
                                                "km"
                                            )
                                        } else {
                                            Pair("${runAction.totalDistance.toLong()}", "m")
                                        }

                                    var endTime by remember {
                                        mutableLongStateOf(
                                            runAction.endTimestamp
                                                ?: Calendar.getInstance().timeInMillis
                                        )
                                    }

                                    LaunchedEffect(
                                        runAction.endTimestamp,
                                        Calendar.getInstance().timeInMillis
                                    ) {
                                        endTime = runAction.endTimestamp
                                            ?: Calendar.getInstance().timeInMillis
                                    }

                                    val duration =
                                        ((endTime - runAction.startTimestamp) / 1000).seconds
                                    val hours = duration.inWholeHours
                                    val minutes = duration.inWholeMinutes - (hours * 60)
                                    val seconds =
                                        duration.inWholeSeconds - (minutes * 60) - (hours * 60 * 60)

                                    val show =
                                        if (hours > 0) 1
                                        else if (minutes > 0) 2
                                        else 3

                                    Crossfade(
                                        targetState = show,
                                        label = ""
                                    ) {
                                        when (it) {
                                            1 -> {
                                                Text(
                                                    text = "${distance.first} ${distance.second} in ${hours}h ${minutes}m",
                                                    style = textStyle
                                                )
                                            }

                                            2 -> {
                                                Text(
                                                    text = "${distance.first} ${distance.second} in $minutes min",
                                                    style = textStyle
                                                )
                                            }

                                            3 -> {
                                                Text(
                                                    text = "${distance.first} ${distance.second} in $seconds sec",
                                                    style = textStyle
                                                )
                                            }
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp)
                                ) {
                                    val textStyle = Typography.labelLarge

                                    Text(
                                        text = "${runAction.totalSteps} steps",
                                        style = textStyle
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        is RunActionElement.Separator -> {
                            val date = runActionElement.date

                            val dateDayDate = date / DAY_IN_MILIS

                            val todayDayDate = Calendar.getInstance().timeInMillis / DAY_IN_MILIS
                            val yesterdayDayDate =
                                (Calendar.getInstance().timeInMillis - DAY_IN_MILIS) / DAY_IN_MILIS

                            val separatorDateText =
                                when (dateDayDate) {
                                    todayDayDate -> stringResource(R.string.today)
                                    yesterdayDayDate -> stringResource(R.string.yesterday)
                                    else -> SimpleDateFormat(
                                        stringResource(R.string.run_date_format),
                                        Locale.getDefault()
                                    ).format(date)
                                }

                            Column {
                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = separatorDateText,
                                    modifier = Modifier
                                        .padding(
                                            vertical = 6.dp,
                                            horizontal = 20.dp
                                        )
                                )

                                HorizontalDivider()
                            }
                        }
                    }
                } else {
                    Text("Loading")
                }
            }
        }
    }
}

@PreviewLightDark
@ExpandedPreviews
@Composable
private fun DashboardScreenRunsPreview() {
    ZapierdaloTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            DashboardScreen(
                uiState = DashboardUiState(
                    currentDestination = DashboardDestinations.RUNS
                ),
                onEvent = {}
            )
        }
    }
}
