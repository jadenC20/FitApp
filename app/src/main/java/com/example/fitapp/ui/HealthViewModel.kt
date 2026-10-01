package com.example.fitapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitapp.data.HealthEntry
import com.example.fitapp.data.HealthMetricType
import com.example.fitapp.data.HealthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class HealthSummary(
    val todayCalories: Double = 0.0,
    val todayWater: Double = 0.0,
    val todaySleep: Double = 0.0,
    val todayExercise: Double = 0.0,
    val latestWeight: Double? = null,
    val avgSleep7Days: Double = 0.0,
    val avgCalories7Days: Double = 0.0,
    val totalExercise7Days: Double = 0.0,
    val totalEntriesCount: Int = 0
)

class HealthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HealthRepository(application)

    private val _entries = MutableStateFlow<List<HealthEntry>>(emptyList())
    val entries: StateFlow<List<HealthEntry>> = _entries.asStateFlow()

    private val _selectedFilter = MutableStateFlow<HealthMetricType?>(null)
    val selectedFilter: StateFlow<HealthMetricType?> = _selectedFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _summary = MutableStateFlow(HealthSummary())
    val summary: StateFlow<HealthSummary> = _summary.asStateFlow()

    private val _showCreateDialog = MutableStateFlow(false)
    val showCreateDialog: StateFlow<Boolean> = _showCreateDialog.asStateFlow()

    init {
        loadEntries()
    }

    fun loadEntries() {
        viewModelScope.launch {
            val list = repository.getAllEntries()
            if (list.isEmpty()) {
                // Populate initial sample data so user sees immediate results on first run
                populateSampleDataInternal()
            } else {
                _entries.value = list
                calculateSummary(list)
            }
        }
    }

    fun setFilter(metricType: HealthMetricType?) {
        _selectedFilter.value = metricType
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openCreateDialog() {
        _showCreateDialog.value = true
    }

    fun closeCreateDialog() {
        _showCreateDialog.value = false
    }

    fun addEntry(entry: HealthEntry) {
        viewModelScope.launch {
            repository.addEntry(entry)
            val updated = repository.getAllEntries()
            _entries.value = updated
            calculateSummary(updated)
            closeCreateDialog()
        }
    }

    fun deleteEntry(id: Long) {
        viewModelScope.launch {
            repository.deleteEntry(id)
            val updated = repository.getAllEntries()
            _entries.value = updated
            calculateSummary(updated)
        }
    }

    fun clearAllEntries() {
        viewModelScope.launch {
            repository.clearAll()
            _entries.value = emptyList()
            _summary.value = HealthSummary()
        }
    }

    private suspend fun populateSampleDataInternal() {
        val now = System.currentTimeMillis()
        val day = 86400000L

        val samples = listOf(
            HealthEntry(
                metricType = HealthMetricType.CALORIES,
                value = 650.0,
                notes = "Oatmeal with berries & coffee",
                dateMillis = now - (day * 0.1).toLong()
            ),
            HealthEntry(
                metricType = HealthMetricType.WATER,
                value = 24.0,
                notes = "Morning hydration",
                dateMillis = now - (day * 0.2).toLong()
            ),
            HealthEntry(
                metricType = HealthMetricType.EXERCISE,
                value = 45.0,
                notes = "Morning jog in the park",
                dateMillis = now - (day * 0.3).toLong()
            ),
            HealthEntry(
                metricType = HealthMetricType.SLEEP,
                value = 7.5,
                notes = "Restful sleep",
                dateMillis = now - day
            ),
            HealthEntry(
                metricType = HealthMetricType.CALORIES,
                value = 850.0,
                notes = "Chicken salad bowl",
                dateMillis = now - day + 14000000L
            ),
            HealthEntry(
                metricType = HealthMetricType.WATER,
                value = 32.0,
                notes = "Afternoon water bottle",
                dateMillis = now - day + 20000000L
            ),
            HealthEntry(
                metricType = HealthMetricType.WEIGHT,
                value = 168.5,
                notes = "Weekly weigh-in",
                dateMillis = now - (day * 2)
            ),
            HealthEntry(
                metricType = HealthMetricType.SLEEP,
                value = 8.0,
                notes = "Weekend deep sleep",
                dateMillis = now - (day * 2)
            )
        )

        for (sample in samples) {
            repository.addEntry(sample)
        }

        val all = repository.getAllEntries()
        _entries.value = all
        calculateSummary(all)
    }

    private fun calculateSummary(allEntries: List<HealthEntry>) {
        val calendar = Calendar.getInstance()
        val startOfToday = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val sevenDaysAgo = startOfToday - (7 * 86400000L)

        val todayEntries = allEntries.filter { it.dateMillis >= startOfToday }
        val last7DaysEntries = allEntries.filter { it.dateMillis >= sevenDaysAgo }

        val todayCalories = todayEntries.filter { it.metricType == HealthMetricType.CALORIES }.sumOf { it.value }
        val todayWater = todayEntries.filter { it.metricType == HealthMetricType.WATER }.sumOf { it.value }
        val todaySleep = todayEntries.filter { it.metricType == HealthMetricType.SLEEP }.sumOf { it.value }
        val todayExercise = todayEntries.filter { it.metricType == HealthMetricType.EXERCISE }.sumOf { it.value }

        val latestWeight = allEntries
            .filter { it.metricType == HealthMetricType.WEIGHT }
            .maxByOrNull { it.dateMillis }?.value

        val sleep7Days = last7DaysEntries.filter { it.metricType == HealthMetricType.SLEEP }
        val avgSleep = if (sleep7Days.isNotEmpty()) sleep7Days.map { it.value }.average() else 0.0

        val cal7Days = last7DaysEntries.filter { it.metricType == HealthMetricType.CALORIES }
        val avgCal = if (cal7Days.isNotEmpty()) cal7Days.map { it.value }.average() else 0.0

        val totalEx7Days = last7DaysEntries.filter { it.metricType == HealthMetricType.EXERCISE }.sumOf { it.value }

        _summary.value = HealthSummary(
            todayCalories = todayCalories,
            todayWater = todayWater,
            todaySleep = todaySleep,
            todayExercise = todayExercise,
            latestWeight = latestWeight,
            avgSleep7Days = avgSleep,
            avgCalories7Days = avgCal,
            totalExercise7Days = totalEx7Days,
            totalEntriesCount = allEntries.size
        )
    }
}
