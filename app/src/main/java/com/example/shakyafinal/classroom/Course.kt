package com.example.shakyafinal.classroom

import com.google.gson.annotations.SerializedName

data class Course(
    @field:SerializedName("Course") val course: List<CourseItem>
)

data class ClassroomItem(
    @field:SerializedName("code") val code: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("link") val link: String
)

data class Name(
    @field:SerializedName("en") val en: String,
    @field:SerializedName("zh") val zh: String
)

data class Time(
    @field:SerializedName("thu") val thu: List<Any>,
    @field:SerializedName("tue") val tue: List<Any>,
    @field:SerializedName("wed") val wed: List<Any>,
    @field:SerializedName("sat") val sat: List<Any>,
    @field:SerializedName("fri") val fri: List<String>,
    @field:SerializedName("sun") val sun: List<Any>,
    @field:SerializedName("mon") val mon: List<Any>
)

data class CourseItem(
    @field:SerializedName("peopleWithdraw") val peopleWithdraw: String,
    @field:SerializedName("hours") val hours: String,
    @field:SerializedName("courseType") val courseType: String,
    @field:SerializedName("code") val code: String,
    @field:SerializedName("notes") val notes: String,
    @field:SerializedName("description") val description: Description,
    @field:SerializedName("classroom") val classroom: List<ClassroomItem>,
    @field:SerializedName("language") val language: String,
    @field:SerializedName("people") val people: String,
    @field:SerializedName("ta") val ta: List<Any>,
    @field:SerializedName("courseDescriptionLink") val courseDescriptionLink: String,
    @field:SerializedName("teacher") val teacher: List<TeacherItem>,
    @field:SerializedName("stage") val stage: String,
    @field:SerializedName("syllabusLinks") val syllabusLinks: List<String>,
    @field:SerializedName("name") val name: Name,
    @field:SerializedName("id") val id: String,
    @field:SerializedName("time") val time: Time,
    @field:SerializedName("credit") val credit: String,
    @field:SerializedName("class") val jsonMemberClass: List<JsonMemberClassItem>
)

data class Description(
    @field:SerializedName("en") val en: String,
    @field:SerializedName("zh") val zh: String
)

data class TeacherItem(
    @field:SerializedName("code") val code: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("link") val link: String
)

data class JsonMemberClassItem(
    @field:SerializedName("code") val code: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("link") val link: String
)
