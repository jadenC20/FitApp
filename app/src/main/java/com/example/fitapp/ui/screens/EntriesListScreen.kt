package com.example.fitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fitapp.R
import com.example.fitapp.data.HealthMetricType
import com.example.fitapp.ui.HealthViewModel
import com.example.fitapp.ui.components.HealthEntryItem

@Composable
fun EntriesListScreen(
    viewModel: HealthViewModel,
    onAddEntryClick: () -> Unit
) {
    val entries by viewModel.entries.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val filteredEntries = entries.filter { entry ->
        val matchesFilter = selectedFilter == null || entry.metricType == selectedFilter
        val matchesQuery = searchQuery.isEmpty() ||
                entry.notes.contains(searchQuery, ignoreCase = true) ||
                entry.metricType.displayName.contains(searchQuery, ignoreCase = true) ||
                entry.formattedValue.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesQuery
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search & Filter Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search entries or notes...") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home),
                        contentDescription = "Search"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Category Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(end = 16.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedFilter == null,
                        onClick = { viewModel.setFilter(null) },
                        label = { Text("All (${entries.size})") },
                        shape = RoundedCornerShape(20.dp)
                    )
                }

                items(HealthMetricType.entries.toTypedArray()) { metric ->
                    val count = entries.count { it.metricType == metric }
                    FilterChip(
                        selected = selectedFilter == metric,
                        onClick = { viewModel.setFilter(if (selectedFilter == metric) null else metric) },
                        label = { Text("${metric.displayName} ($count)") },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(metric.color)
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = metric.color.copy(alpha = 0.2f),
                            selectedLabelColor = metric.color
                        )
                    )
                }
            }
        }

        if (filteredEntries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "No Health Entries Found",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = if (searchQuery.isNotEmpty() || selectedFilter != null) {
                            "Try clearing search filters or add a new entry."
                        } else {
                            "Start tracking your health metrics today!"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onAddEntryClick,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Add New Entry")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(
                    items = filteredEntries,
                    key = { it.id }
                ) { entry ->
                    HealthEntryItem(
                        entry = entry,
                        onDelete = { id -> viewModel.deleteEntry(id) }
                    )
                }
            }
        }
    }
}
