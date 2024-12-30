package com.example.shakyafinal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TasksViewModel: ViewModel() {
    val tasks = MutableLiveData<Tasks>()
}
