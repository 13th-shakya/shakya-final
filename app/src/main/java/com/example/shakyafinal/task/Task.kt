package com.example.shakyafinal.task

import java.util.Date

data class Task(
    val id: Long? = null,
    val title: String,
    val date: Date,
    val location: String,
)
