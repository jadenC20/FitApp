package com.example.fitapp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitapp.R
import com.example.fitapp.data.HealthMetricType
import com.example.fitapp.ui.HealthViewModel
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: HealthViewModel,
    onAddEntryClick: () -> Unit
) {
    val summary by viewModel.summary.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddEntryClick,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_favorite),
                        contentDescription = "Add Entry"
                    )
                },
                text = { Text("Log Entry") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HeaderCard()
            }

            item {
                Text(
                    text = "Today's Metrics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            item {
                MetricProgressCard(
                    metricType = HealthMetricType.CALORIES,
                    currentValue = summary.todayCalories,
                    goalValue = HealthMetricType.CALORIES.defaultDailyGoal,
                    unit = HealthMetricType.CALORIES.defaultUnit
                )
            }

            item {
                MetricProgressCard(
                    metricType = HealthMetricType.WATER,
                    currentValue = summary.todayWater,
                    goalValue = HealthMetricType.WATER.defaultDailyGoal,
                    unit = HealthMetricType.WATER.defaultUnit
                )
            }

            item {
                MetricProgressCard(
                    metricType = HealthMetricType.SLEEP,
                    currentValue = summary.todaySleep,
                    goalValue = HealthMetricType.SLEEP.defaultDailyGoal,
                    unit = HealthMetricType.SLEEP.defaultUnit
                )
            }

            item {
                MetricProgressCard(
                    metricType = HealthMetricType.EXERCISE,
                    currentValue = summary.todayExercise,
                    goalValue = HealthMetricType.EXERCISE.defaultDailyGoal,
                    unit = HealthMetricType.EXERCISE.defaultUnit
                )
            }

            if (summary.latestWeight != null) {
                item {
                    WeightCard(weight = summary.latestWeight!!)
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
fun HeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Welcome to FitApp 👋",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Track your daily calories, water intake, sleep hours, exercise, and weight persistently.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun MetricProgressCard(
    metricType: HealthMetricType,
    currentValue: Double,
    goalValue: Double,
    unit: String
) {
    val progress = (currentValue / goalValue).coerceIn(0.0, 1.0).toFloat()
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

    val formattedCurrent = if (currentValue % 1.0 == 0.0) {
        currentValue.toInt().toString()
    } else {
        String.format(Locale.getDefault(), "%.1f", currentValue)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(metricType.color)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = metricType.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "$formattedCurrent / ${goalValue.toInt()} $unit",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = metricType.color,
                trackColor = metricType.color.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
fun WeightCard(weight: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = HealthMetricType.WEIGHT.color.copy(alpha = 0.12f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Latest Weight",
                    style = MaterialTheme.typography.labelMedium,
                    color = HealthMetricType.WEIGHT.color
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$weight lbs",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Surface(
                shape = CircleShape,
                color = HealthMetricType.WEIGHT.color.copy(alpha = 0.2f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_account_box),
                        contentDescription = "Weight Icon",
                        tint = HealthMetricType.WEIGHT.color
                    )
                }
            }
        }
    }
}
