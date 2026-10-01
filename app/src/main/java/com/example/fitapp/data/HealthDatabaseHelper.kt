package com.example.fitapp.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HealthDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "fitapp_health.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_ENTRIES = "health_entries"
        const val COLUMN_ID = "_id"
        const val COLUMN_METRIC_TYPE = "metric_type"
        const val COLUMN_VALUE = "value"
        const val COLUMN_UNIT = "unit"
        const val COLUMN_NOTES = "notes"
        const val COLUMN_DATE_MILLIS = "date_millis"
        const val COLUMN_PHOTO_URI = "photo_uri"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_ENTRIES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_METRIC_TYPE TEXT NOT NULL,
                $COLUMN_VALUE REAL NOT NULL,
                $COLUMN_UNIT TEXT NOT NULL,
                $COLUMN_NOTES TEXT,
                $COLUMN_DATE_MILLIS INTEGER NOT NULL,
                $COLUMN_PHOTO_URI TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ENTRIES")
        onCreate(db)
    }

    fun insertEntry(entry: HealthEntry): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_METRIC_TYPE, entry.metricType.name)
            put(COLUMN_VALUE, entry.value)
            put(COLUMN_UNIT, entry.unit)
            put(COLUMN_NOTES, entry.notes)
            put(COLUMN_DATE_MILLIS, entry.dateMillis)
            put(COLUMN_PHOTO_URI, entry.photoUri)
        }
        return db.insert(TABLE_ENTRIES, null, values)
    }

    fun getAllEntries(): List<HealthEntry> {
        val entriesList = mutableListOf<HealthEntry>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_ENTRIES,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_DATE_MILLIS DESC"
        )

        cursor.use { c ->
            val idIndex = c.getColumnIndexOrThrow(COLUMN_ID)
            val metricIndex = c.getColumnIndexOrThrow(COLUMN_METRIC_TYPE)
            val valueIndex = c.getColumnIndexOrThrow(COLUMN_VALUE)
            val unitIndex = c.getColumnIndexOrThrow(COLUMN_UNIT)
            val notesIndex = c.getColumnIndexOrThrow(COLUMN_NOTES)
            val dateIndex = c.getColumnIndexOrThrow(COLUMN_DATE_MILLIS)
            val photoIndex = c.getColumnIndexOrThrow(COLUMN_PHOTO_URI)

            while (c.moveToNext()) {
                val entry = HealthEntry(
                    id = c.getLong(idIndex),
                    metricType = HealthMetricType.fromString(c.getString(metricIndex)),
                    value = c.getDouble(valueIndex),
                    unit = c.getString(unitIndex),
                    notes = c.getString(notesIndex) ?: "",
                    dateMillis = c.getLong(dateIndex),
                    photoUri = c.getString(photoIndex)
                )
                entriesList.add(entry)
            }
        }
        return entriesList
    }

    fun deleteEntry(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_ENTRIES, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteAllEntries(): Int {
        val db = writableDatabase
        return db.delete(TABLE_ENTRIES, null, null)
    }
}
