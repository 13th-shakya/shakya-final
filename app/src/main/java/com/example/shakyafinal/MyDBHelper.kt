package com.example.shakyafinal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "todoDatabase.db" // 資料庫名稱
        private const val DATABASE_VERSION = 1 // 資料庫版本
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 建立待辦事項資料表
        db.execSQL("CREATE TABLE todoTable(task TEXT PRIMARY KEY, time TEXT NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS todoTable")
        onCreate(db)
    }
}
