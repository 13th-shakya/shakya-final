package com.example.shakyafinal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shakyafinal.task.Task
import com.example.shakyafinal.task.TaskRepository

class TasksViewModel() : ViewModel() {
    val context = null
    val repository = TaskRepository()
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    fun loadTasks() {
        _tasks.postValue(repository.getTasks())
    }

    fun addTask(task: Task): Boolean {
        val id = repository.addTask(task)
        return id != -1L
        loadTasks()
    }

    fun updateTask(title: String, task: Task): Boolean {
        val oldTask = repository.getTask(title)
        oldTask ?: return false
        val rows = repository.updateTask(task, oldTask.id!!)
        loadTasks()
        return rows != 0
    }

    fun deleteTask(title: String): Boolean {
        val task = repository.getTask(title)
        task ?: return false
        val rows = repository.deleteTask(task.id!!)
        loadTasks()
        return rows != 0
    }
}
