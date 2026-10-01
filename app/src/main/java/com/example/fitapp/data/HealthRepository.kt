package com.example.fitapp.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HealthRepository(context: Context) {

    private val dbHelper = HealthDatabaseHelper(context.applicationContext)

    suspend fun getAllEntries(): List<HealthEntry> = withContext(Dispatchers.IO) {
        dbHelper.getAllEntries()
    }

    suspend fun addEntry(entry: HealthEntry): Long = withContext(Dispatchers.IO) {
        dbHelper.insertEntry(entry)
    }

    suspend fun deleteEntry(id: Long): Boolean = withContext(Dispatchers.IO) {
        dbHelper.deleteEntry(id) > 0
    }

    suspend fun clearAll(): Boolean = withContext(Dispatchers.IO) {
        dbHelper.deleteAllEntries() >= 0
    }
}
