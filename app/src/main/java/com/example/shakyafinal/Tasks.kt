package com.example.shakyafinal

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class Tasks() {
    class Task(
        val title: String,
        val date: Date,
        val location: String,
    )

    val tasks = mutableListOf<Task>()

    private val dbHelper = object : SQLiteOpenHelper(null, "tasks.db", null, 1) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE tasks(name TEXT PRIMARY KEY, date TEXT NOT NULL)")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS tasks")
            onCreate(db)
        }
    }
    private val db = dbHelper.writableDatabase

    fun insert(task: Task): Boolean = try {
        val values = ContentValues().apply {
            put(task.title, task.description)
        }
        db.insertOrThrow("tasks", null, values)
        true
    } catch (e: Exception) {
        false
    }

    fun close() {
        dbHelper.close()
    }
}
