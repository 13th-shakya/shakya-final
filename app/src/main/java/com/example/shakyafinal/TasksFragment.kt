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
import androidx.lifecycle.ViewModelProvider
import com.example.shakyafinal.databinding.FragmentTasksBinding
import com.example.shakyafinal.task.Task
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        binding.listView.adapter = adapter

        val viewModel = ViewModelProvider(this)[TasksViewModel::class.java]

        with(viewModel) {
            loadTasks()
            tasks.observe(viewLifecycleOwner) { tasks ->
                adapter.clear()
                adapter.addAll(tasks.map { it.toString() })
            }
        }

        binding.btnInsert.setOnClickListener {
            val title = getTitle()
            val date = getDate()
            if (title == null || date == null) {
                showToast("請輸入待辦事項與時間")
                return@setOnClickListener
            }

            val task = Task(null, title, date, "")
            if (viewModel.addTask(task)) {
                showToast("新增成功：$task ($date)")
            } else {
                showToast("新增失敗：待辦事項已存在")
            }
        }

        binding.btnUpdate.setOnClickListener {
            val title = getTitle()
            val time = getDate()
            if (title == null || time == null) {
                showToast("請輸入待辦事項與時間")
                return@setOnClickListener
            }

            val task = Task(null, title, time, "")
            if (viewModel.updateTask(title, task)) {
                showToast("更新成功：$title 的時間修改為 $time")
            } else {
                showToast("更新失敗：待辦事項不存在")
            }
        }

        binding.btnDelete.setOnClickListener {
            val title = getTitle()
            if (title == null) {
                showToast("請輸入待辦事項名稱")
                return@setOnClickListener
            }

            if (viewModel.deleteTask(title)) {
                showToast("刪除成功：$title")
            } else {
                showToast("刪除失敗：待辦事項不存在")
            }
        }

        binding.btnQuery.isEnabled = false

        binding.edDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            // 日期選擇器
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                val selectedDate = String.format(Locale("zh_TW"), "%04d-%02d-%02d", year, month + 1, dayOfMonth)

                // 時間選擇器
                TimePickerDialog(requireContext(), { _, hour, minute ->
                    val selectedTime = String.format(Locale("zh_TW"), "%02d:%02d", hour, minute)
                    binding.edDate.setText("$selectedDate $selectedTime")
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 顯示 Toast 訊息
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun getTitle(): String? {
        val title = binding.edTitle.text.toString().trim()
        if (title.isEmpty()) return null
        return title
    }

    private fun getDate(): Date? {
        val date = binding.edDate.text.toString().trim()
        if (date.isEmpty()) return null
        return Date(date)
    }
}
