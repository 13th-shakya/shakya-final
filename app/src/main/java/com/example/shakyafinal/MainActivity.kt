package com.example.shakyafinal

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var databaseManager: DatabaseManager
    private lateinit var uiManager: UIManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化資料庫管理器
        databaseManager = DatabaseManager(this)

        // 連結 UI 元件
        val edBook: EditText = findViewById(R.id.ed_task)
        val edPrice: EditText = findViewById(R.id.ed_time)
        val btnInsert: Button = findViewById(R.id.btn_insert)
        val btnUpdate: Button = findViewById(R.id.btn_update)
        val btnDelete: Button = findViewById(R.id.btn_delete)
        val btnQuery: Button = findViewById(R.id.btn_query)
        val listView: ListView = findViewById(R.id.listView)

        // 初始化 UI 管理器
        uiManager = UIManager(
            this,
            databaseManager,
            edBook,
            edPrice,
            btnInsert,
            btnUpdate,
            btnDelete,
            btnQuery,
            listView
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseManager.closeDatabase()
    }
}
