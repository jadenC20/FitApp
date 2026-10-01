package com.example.fitapp.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitapp.R
import com.example.fitapp.data.HealthMetricType
import com.example.fitapp.ui.HealthViewModel
import java.util.Locale

@Composable
fun TrendsScreen(
    viewModel: HealthViewModel
) {
    val summary by viewModel.summary.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Trends & Averages (Past 7 Days)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TrendStatCard(
                    title = "Avg Sleep",
                    value = "${String.format(Locale.getDefault(), "%.1f", summary.avgSleep7Days)} hrs/day",
                    metricType = HealthMetricType.SLEEP,
                    modifier = Modifier.weight(1f)
                )

                TrendStatCard(
                    title = "Avg Calories",
                    value = "${summary.avgCalories7Days.toInt()} kcal/day",
                    metricType = HealthMetricType.CALORIES,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TrendStatCard(
                    title = "Total Exercise",
                    value = "${summary.totalExercise7Days.toInt()} mins",
                    metricType = HealthMetricType.EXERCISE,
                    modifier = Modifier.weight(1f)
                )

                TrendStatCard(
                    title = "Total Logged",
                    value = "${summary.totalEntriesCount} entries",
                    metricType = HealthMetricType.WATER,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Text(
                text = "Health Goals Comparison",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            GoalComparisonCard(
                title = "Sleep Goal (8 hrs/day)",
                actual = summary.avgSleep7Days,
                target = HealthMetricType.SLEEP.defaultDailyGoal,
                unit = "hrs",
                color = HealthMetricType.SLEEP.color
            )
        }

        item {
            GoalComparisonCard(
                title = "Calorie Target (2,000 kcal/day)",
                actual = summary.avgCalories7Days,
                target = HealthMetricType.CALORIES.defaultDailyGoal,
                unit = "kcal",
                color = HealthMetricType.CALORIES.color
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Data Management",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.loadEntries() },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Reset Sample Data")
                        }

                        Button(
                            onClick = { viewModel.clearAllEntries() },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Clear All")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrendStatCard(
    title: String,
    value: String,
    metricType: HealthMetricType,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = metricType.color.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(metricType.color)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = metricType.color,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun GoalComparisonCard(
    title: String,
    actual: Double,
    target: Double,
    unit: String,
    color: Color
) {
    val percentage = if (target > 0) ((actual / target) * 100).toInt() else 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
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
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "7-Day Average: ${if (actual % 1.0 == 0.0) actual.toInt() else String.format(Locale.getDefault(), "%.1f", actual)} $unit",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = color.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "$percentage% of Goal",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}
