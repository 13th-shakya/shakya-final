package com.example.shakyafinal.classroom

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ClassroomDataHelper {
    companion object {
        val service: NtutCourseService = Retrofit.Builder()
            .baseUrl("https://gnehs.github.io/ntut-course-crawler-node/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NtutCourseService::class.java)
    }

    suspend fun getClassroomList(): List<Map<String, Any>> {
        val yearData = service.fetchYearData()

        // Find the latest year and semester
        val lastYear = yearData.mapKeys { it.key.toInt() }.toSortedMap().lastKey()
        val lastSemester = yearData[lastYear.toString()]!!.toSortedSet().last()

        val systems = listOf("研究所(日間部、進修部、週末碩士班)", "進修部", "main")
        val courseItems = systems.flatMap { system ->
            service.fetchCourse(lastYear, lastSemester, system).course
        }

        // Timetable mapping
        val timetable = mapOf(
            1 to "8:10", 2 to "9:10", 3 to "10:10", 4 to "11:10",
            "N" to "12:10", 5 to "13:10", 6 to "14:10", 7 to "15:10",
            8 to "16:10", 9 to "17:10", "A" to "18:30",
            "B" to "19:20", "C" to "20:20", "D" to "21:10"
        )
        val upcomingCourseIncludes = timetable.keys.map { it.toString() }

        // Create classroom and category lists
        val classSet = mutableSetOf<String>()
        val categorySet = mutableSetOf<String>()

        courseItems.forEach { course ->
            course.classroom.forEach { classroom ->
                classSet.add(classroom.name)
            }
        }

        val classList = classSet.map { name ->
            val category = name.first().toString()
            categorySet.add(category)
            mutableMapOf(
                "name" to name,
                "category" to category,
                "timetable" to upcomingCourseIncludes.toMutableList()
            )
        }.toMutableList()

        // Filter timetable based on today's schedule
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val dayMap = mapOf(
            1 to "sun", 2 to "mon", 3 to "tue",
            4 to "wed", 5 to "thu", 6 to "fri", 7 to "sat"
        )
        val todayDayOfWeek = dayMap[currentDay] ?: "mon"

        courseItems.filter { it.classroom.isNotEmpty() }.forEach { course ->
            course.classroom.forEach { classroom ->
                classList.find { it["name"] == classroom.name }?.apply {
                    val occupiedTimes = getOccupiedTimes(course.time, todayDayOfWeek)
                    this["timetable"] = (this["timetable"] as List<String>).filterNot { occupiedTimes.contains(it) }
                    this["link"] = classroom.link
                }
            }
        }

        return classList.sortedBy { it["name"] as String }
    }


    // Mapping day string to Time property
    fun getOccupiedTimes(time: Time, day: String): List<String> {
        return when (day) {
            "sun" -> time.sun.filterIsInstance<String>()
            "mon" -> time.mon.filterIsInstance<String>()
            "tue" -> time.tue.filterIsInstance<String>()
            "wed" -> time.wed.filterIsInstance<String>()
            "thu" -> time.thu.filterIsInstance<String>()
            "fri" -> time.fri.filterIsInstance<String>()
            "sat" -> time.sat.filterIsInstance<String>()
            else -> emptyList()
        }
    }
}
