package com.example.learningdiary2.utils
import androidx.room.TypeConverter
import com.example.learningdiary2.models.Genre

class CustomConverters {
    @TypeConverter
    fun listToString(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun stringToList(value: String): List<String> {
        return value.split(",").map { it.trim() }
    }

    @TypeConverter
    fun genresToString(value: List<Genre>): String {
        return value.joinToString(",") { it.toString() }
    }

    @TypeConverter
    fun stringToGenres(value: String): List<Genre> {
        return value.split(",").map { Genre.valueOf(it) }.toMutableList()
    }

    @TypeConverter
    fun toGenre(value: String) = Genre.valueOf(value)

    @TypeConverter
    fun fromGenre(value: Genre) = value.name

}