package com.example.shakyafinal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shakyafinal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseManager: DatabaseManager
    private lateinit var uiManager: UIManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化 View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 初始化 DatabaseManager
        databaseManager = DatabaseManager(this)

        // 初始化 UIManager，傳入 View Binding 的元件
        uiManager = UIManager(
            this,
            databaseManager,
            binding.edTask,       // 待辦事項輸入框
            binding.edTime,       // 時間輸入框
            binding.btnInsert,    // 新增按鈕
            binding.btnUpdate,    // 修改按鈕
            binding.btnDelete,    // 刪除按鈕
            binding.btnQuery,     // 查詢按鈕
            binding.listView      // ListView 顯示結果
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseManager.closeDatabase()
    }
}
