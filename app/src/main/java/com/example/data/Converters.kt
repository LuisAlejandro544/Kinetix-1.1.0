package com.example.data

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val actionListType = Types.newParameterizedType(List::class.java, ActionData::class.java)
    private val actionListAdapter = moshi.adapter<List<ActionData>>(actionListType)

    @TypeConverter
    fun fromActionList(value: List<ActionData>?): String {
        return actionListAdapter.toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toActionList(value: String?): List<ActionData> {
        if (value.isNullOrEmpty()) return emptyList()
        return actionListAdapter.fromJson(value) ?: emptyList()
    }
}
