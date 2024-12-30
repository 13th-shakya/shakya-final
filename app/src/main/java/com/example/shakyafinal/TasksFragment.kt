package com.example.shakyafinal

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.shakyafinal.databinding.FragmentTasksBinding
import java.util.Calendar
import java.util.Locale

class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TasksViewModel by activityViewModels()

    private val items = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)

        val context = activity
        adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, adapter)
        binding.listView.adapter = adapter

        binding.btnInsert.setOnClickListener {
            val task = binding.edTask.text.toString().trim()
            val time = binding.edDate.text.toString().trim()
            if (task.isEmpty() || time.isEmpty()) {
                showToast("請輸入待辦事項與時間")
            } else {
                if (databaseManager.insertTask(task, time)) {
                    showToast("新增成功：$task ($time)")
                    binding.edTask.text.clear()
                    binding.edDate.text.clear()
                    handleQuery() // 自動查詢最新資料
                } else {
                    showToast("新增失敗：待辦事項已存在")
                }
            }
        }

        binding.btnUpdate.setOnClickListener {
            val task = binding.edTask.text.toString().trim()
            val time = binding.edDateTime.text.toString().trim()
            if (task.isEmpty() || time.isEmpty()) {
                showToast("請輸入待辦事項與時間")
            } else {
                if (databaseManager.updateTask(task, time)) {
                    showToast("更新成功：$task 的時間修改為 $time")
                    binding.edTask.text.clear()
                    binding.edDate.text.clear()
                    handleQuery() // 自動查詢最新資料
                } else {
                    showToast("更新失敗：待辦事項不存在")
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            val task = binding.edTask.text.toString().trim()
            if (task.isEmpty()) {
                showToast("請輸入待辦事項名稱")
            } else {
                if (databaseManager.deleteTask(task)) {
                    showToast("刪除成功：$task")
                    binding.edTask.text.clear()
                    binding.edDate.text.clear()
                    handleQuery() // 自動查詢最新資料
                } else {
                    showToast("刪除失敗：待辦事項不存在")
                }
            }
        }

        binding.btnQuery.setOnClickListener { handleQuery() }

        binding.edDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            // 日期選擇器
            DatePickerDialog(context, { _, year, month, dayOfMonth ->
                val selectedDate = String.format(Locale("zh_TW"), "%04d-%02d-%02d", year, month + 1, dayOfMonth)

                // 時間選擇器
                TimePickerDialog(context, { _, hour, minute ->
                    val selectedTime = String.format(Locale("zh_TW"), "%02d:%02d", hour, minute)
                    binding.edDate.setText("$selectedDate $selectedTime")
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        handleQuery() // 初始查詢

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 處理查詢操作
    private fun handleQuery() {
        val task = binding.edTask.text.toString().trim()
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
