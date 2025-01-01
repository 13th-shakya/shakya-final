package com.example.shakyafinal.task

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.lifecycle.LiveData
import java.util.Date

class TaskRepository() {
    private val db = TaskDatabaseHelper().writableDatabase

    fun addTask(task: Task): Long {
        val values = ContentValues().apply {
            put("title", task.title)
            put("date", task.date.toString())
            put("location", task.location)
        }
        return db.insert("Task", null, values)
    }

    fun getTask(id: Long): Task? {
        db.rawQuery("SELECT * FROM Task WHERE id = ?", arrayOf(id.toString())).use { cursor ->
            if (cursor.moveToFirst()) return cursorToTask(cursor)
        }
        return null
    }

    fun getTask(title: String): Task? {
        db.rawQuery("SELECT * FROM Task WHERE title = ?", arrayOf(title)).use { cursor ->
            if (cursor.moveToFirst()) return cursorToTask(cursor)
        }
        return null
    }

    fun updateTask(task: Task, id: Long): Int {
        val values = ContentValues().apply {
            put("title", task.title)
            put("date", task.date.toString())
            put("location", task.location)
        }
        return db.update("Task", values, "id = ?", arrayOf(id.toString()))
    }

    fun deleteTask(id: Long): Int {
        return db.delete("Task", "id = ?", arrayOf(id.toString()))
    }

    fun getTasks(): List<Task> {
        val list = mutableListOf<Task>()
        db.rawQuery("SELECT * FROM Task", null).use { cursor ->
            while (cursor.moveToNext()) {
                list.add(cursorToTask(cursor))
            }
        }
        return list
    }

    private fun cursorToTask(cursor: Cursor): Task {
        return Task(
            title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
            date = Date(cursor.getString(cursor.getColumnIndexOrThrow("date"))),
            location = cursor.getString(cursor.getColumnIndexOrThrow("location"))
        )
    }
}
