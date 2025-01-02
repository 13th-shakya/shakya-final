package com.example.shakyafinal.classroom

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NtutCourseService {
    @GET("/main.json")
    suspend fun fetchYearData(): YearData

    @GET("/{year}/{sem}/{system}.json")
    suspend fun fetchCourse(
        @Path("year") year: Int,
        @Path("sem") sem: Int,
        @Path("system") system: String,
    ): Course
}
