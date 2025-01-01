package com.example.shakyafinal

import androidx.room.*
import java.util.Date

@Database(entities = [Task::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val date: Date,
    val location: String,
)

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM Task WHERE title LIKE :title")
    fun findByTitle(title: String): Task?

    @Insert
    fun insert(vararg tasks: Task)

    @Update
    fun update(vararg tasks: Task)

    @Delete
    fun delete(vararg tasks: Task)
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}
