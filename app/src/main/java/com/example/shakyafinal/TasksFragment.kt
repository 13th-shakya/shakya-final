package com.example.shakyafinal

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shakyafinal.databinding.FragmentTasksBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.DateFormat
import java.util.*

class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArrayAdapter<String>
    private var selectedDateInMillis = 0L
    private var selectedHour = 0
    private var selectedMinute = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)

        val db = AppDatabase.getInstance(requireContext())
        val taskDao = db.taskDao()

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        binding.listView.adapter = adapter
        refreshListView(taskDao)

        binding.btnInsert.setOnClickListener {
            val title = getTitle()
            val date = getDate()
            if (title == null || date == null) {
                showToast("請輸入待辦事項與時間")
                return@setOnClickListener
            }

            val task = Task(0, title, date, "location")
            taskDao.insert(task)
            refreshListView(taskDao)
            showToast("新增成功：$task ($date)")
//            showToast("新增失敗：待辦事項已存在")
        }

        binding.btnUpdate.setOnClickListener {
            val title = getTitle()
            val date = getDate()
            if (title == null || date == null) {
                showToast("請輸入待辦事項與時間")
                return@setOnClickListener
            }

            val task = Task(0, title, date, "")
            taskDao.update(task)
            refreshListView(taskDao)
            showToast("更新成功：$title 的時間修改為 $date")
//            showToast("更新失敗：待辦事項不存在")
        }

        binding.btnDelete.setOnClickListener {
            val title = getTitle()
            if (title == null) {
                showToast("請輸入待辦事項名稱")
                return@setOnClickListener
            }

            val task = taskDao.findByTitle(title)
            if (task == null) {
                showToast("刪除失敗：待辦事項不存在")
                return@setOnClickListener
            }
            taskDao.delete(task)
            refreshListView(taskDao)
            showToast("刪除成功：$title")
        }

        binding.btnQuery.isEnabled = false

        binding.btnDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener {
                showToast(datePicker.selection.toString())
                selectedDateInMillis = it
                binding.btnDate.text = DateFormat.getDateInstance().format(it)
                binding.btnTime.isEnabled = true
            }

            datePicker.show(activity!!.supportFragmentManager, "datepicker")
        }

        binding.btnTime.isEnabled = false
        binding.btnTime.setOnClickListener {
            val isSystem24Hour = is24HourFormat(requireContext())
            val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                selectedHour = timePicker.hour
                selectedMinute = timePicker.minute

                val time = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    set(Calendar.SECOND, 0)
                }.time

                binding.btnTime.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(time)
            }

            timePicker.show(activity!!.supportFragmentManager, "timepicker")
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

    private fun getDate() = Calendar.getInstance().apply {
        timeInMillis = selectedDateInMillis
        set(Calendar.HOUR_OF_DAY, selectedHour)
        set(Calendar.MINUTE, selectedMinute)
        set(Calendar.SECOND, 0)
    }.time

    private fun refreshListView(taskDao: TaskDao) {
        val tasks = taskDao.getAll().map { it.toString() }
        adapter.clear()
        adapter.addAll(tasks)
        adapter.notifyDataSetChanged()
    }
}
