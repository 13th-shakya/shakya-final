package com.example.shakyafinal

import android.widget.*
import android.content.Context

class UIManager(
    private val context: Context,
    private val databaseManager: DatabaseManager,
    private val edTask: EditText,
    private val edTime: EditText,
    private val btnInsert: Button,
    private val btnUpdate: Button,
    private val btnDelete: Button,
    private val btnQuery: Button,
    private val listView: ListView
) {
    private val items = ArrayList<String>()
    private val adapter: ArrayAdapter<String> =
        ArrayAdapter(context, android.R.layout.simple_list_item_1, items)

    init {
        listView.adapter = adapter
        setupButtonListeners()
        handleQuery() // 初始化時先查詢所有資料
    }

    // 設定按鈕的點擊事件
    private fun setupButtonListeners() {
        btnInsert.setOnClickListener { handleInsert() }
        btnUpdate.setOnClickListener { handleUpdate() }
        btnDelete.setOnClickListener { handleDelete() }
        btnQuery.setOnClickListener { handleQuery() }
    }

    // 處理新增操作
    private fun handleInsert() {
        val task = edTask.text.toString().trim()
        val time = edTime.text.toString().trim()
        if (task.isEmpty() || time.isEmpty()) {
            showToast("請輸入待辦事項與時間")
        } else {
            if (databaseManager.insertTask(task, time)) {
                showToast("新增成功：$task ($time)")
                edTask.text.clear()
                edTime.text.clear()
                handleQuery() // 自動查詢最新資料
            } else {
                showToast("新增失敗：待辦事項已存在")
            }
        }
    }

    // 處理更新操作
    private fun handleUpdate() {
        val task = edTask.text.toString().trim()
        val time = edTime.text.toString().trim()
        if (task.isEmpty() || time.isEmpty()) {
            showToast("請輸入待辦事項與時間")
        } else {
            if (databaseManager.updateTask(task, time)) {
                showToast("更新成功：$task 的時間修改為 $time")
                edTask.text.clear()
                edTime.text.clear()
                handleQuery() // 自動查詢最新資料
            } else {
                showToast("更新失敗：待辦事項不存在")
            }
        }
    }

    // 處理刪除操作
    private fun handleDelete() {
        val task = edTask.text.toString().trim()
        if (task.isEmpty()) {
            showToast("請輸入待辦事項名稱")
        } else {
            if (databaseManager.deleteTask(task)) {
                showToast("刪除成功：$task")
                edTask.text.clear()
                edTime.text.clear()
                handleQuery() // 自動查詢最新資料
            } else {
                showToast("刪除失敗：待辦事項不存在")
            }
        }
    }

    // 處理查詢操作
    private fun handleQuery() {
        val task = edTask.text.toString().trim()
        val results = databaseManager.queryTasks(task)
        items.clear()
        if (results.isEmpty()) {
            showToast("未找到相關的待辦事項")
        } else {
            results.forEach { (taskName, taskTime) ->
                items.add("待辦事項: $taskName  |  時間: $taskTime")
            }
        }
        adapter.notifyDataSetChanged()
    }

    // 顯示 Toast 訊息
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
