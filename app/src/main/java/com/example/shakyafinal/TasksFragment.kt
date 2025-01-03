package com.example.shakyafinal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
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

    private lateinit var adapter: ArrayAdapter<Task>
    private var selectedDateInMillis: Long? = null
    private var selectedHour: Int? = null
    private var selectedMinute: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)

        val ctx = requireContext()
        val db = AppDatabase.getInstance(ctx)
        val taskDao = db.taskDao()

        val tasks = taskDao.getAll()
        adapter = object : ArrayAdapter<Task>(ctx, android.R.layout.simple_list_item_2, android.R.id.text1, tasks) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView1 = view.findViewById<TextView>(android.R.id.text1)
                val textView2 = view.findViewById<TextView>(android.R.id.text2)
                val task = tasks[position]
                textView1.text = task.title
                textView2.text = "${DateFormat.getInstance().format(task.date)}・${task.location}"
                return view
            }
        }
        binding.listView.adapter = adapter
        refreshListView(taskDao)

        binding.edTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                val tasks = taskDao.findByTitle(query)
                adapter.clear()
                adapter.addAll(tasks)
                adapter.notifyDataSetChanged()
            }
        })

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
            val isSystem24Hour = is24HourFormat(ctx)
            val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                selectedHour = timePicker.hour
                selectedMinute = timePicker.minute

                val time = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour!!)
                    set(Calendar.MINUTE, selectedMinute!!)
                    set(Calendar.SECOND, 0)
                }.time

                binding.btnTime.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(time)
            }

            timePicker.show(activity!!.supportFragmentManager, "timepicker")
        }

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
            showToast("新增成功：${task.title} (${DateFormat.getInstance().format(date)})")
        }

        binding.btnUpdate.setOnClickListener {
            val title = getTitle()
            val date = getDate()
            if (title == null || date == null) {
                showToast("請輸入待辦事項與時間")
                return@setOnClickListener
            }

            val task = Task(0, title, date, "")
            val rows = taskDao.update(task)
            if (rows == 0) {
                showToast("更新失敗：待辦事項不存在")
            } else {
                showToast("更新成功：$title 的時間修改為 $date")
                refreshListView(taskDao)
            }
        }

        binding.btnDelete.setOnClickListener {
            val title = getTitle()
            if (title == null) {
                showToast("請輸入待辦事項名稱")
                return@setOnClickListener
            }

            val tasks = taskDao.findByTitle(title)
            val rows = taskDao.delete(*tasks.toTypedArray())
            if (rows == 0) {
                showToast("刪除失敗：待辦事項不存在")
            } else {
                showToast("刪除了 $rows 個待辦事項")
                refreshListView(taskDao)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun getTitle(): String? {
        val title = binding.edTitle.text.toString().trim()
        if (title.isEmpty()) return null
        return title
    }

    private fun getDate(): Date? {
        selectedDateInMillis ?: return null
        selectedHour ?: return null
        selectedMinute ?: return null
        return Calendar.getInstance().apply {
            timeInMillis = selectedDateInMillis!!
            set(Calendar.HOUR_OF_DAY, selectedHour!!)
            set(Calendar.MINUTE, selectedMinute!!)
            set(Calendar.SECOND, 0)
        }.time
    }

    private fun refreshListView(taskDao: TaskDao) {
        adapter.clear()
        adapter.addAll(taskDao.getAll())
        adapter.notifyDataSetChanged()
    }
}
