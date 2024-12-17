package com.example.shakyafinal

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class DatabaseManager(context: Context) {
    private val dbHelper = MyDBHelper(context)
    private val db: SQLiteDatabase = dbHelper.writableDatabase

    // 新增待辦事項
    fun insertTask(task: String, time: String): Boolean {
        return try {
            val values = ContentValues().apply {
                put("task", task)
                put("time", time)
            }
            db.insertOrThrow("todoTable", null, values)
            true
        } catch (e: Exception) {
            false
        }
    }

    // 更新待辦事項時間
    fun updateTask(task: String, time: String): Boolean {
        return try {
            val values = ContentValues().apply {
                put("time", time)
            }
            db.update("todoTable", values, "task = ?", arrayOf(task)) > 0
        } catch (e: Exception) {
            false
        }
    }

    // 刪除待辦事項
    fun deleteTask(task: String): Boolean {
        return try {
            db.delete("todoTable", "task = ?", arrayOf(task)) > 0
        } catch (e: Exception) {
            false
        }
    }

    // 查詢待辦事項
    fun queryTasks(task: String? = null): List<Pair<String, String>> {
        val dataList = mutableListOf<Pair<String, String>>()
        val cursor: Cursor = if (task.isNullOrEmpty()) {
            db.rawQuery("SELECT * FROM todoTable", null)
        } else {
            db.rawQuery("SELECT * FROM todoTable WHERE task LIKE ?", arrayOf(task))
        }

        cursor.use {
            while (it.moveToNext()) {
                val taskName = it.getString(it.getColumnIndexOrThrow("task"))
                val taskTime = it.getString(it.getColumnIndexOrThrow("time"))
                dataList.add(Pair(taskName, taskTime))
            }
        }
        return dataList
    }

    fun closeDatabase() {
        db.close()
    }
}
