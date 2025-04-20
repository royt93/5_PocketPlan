package com.fptflash.noteios.data.todolist

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class Task(
    @SerializedName(value = "t")
    var title: String,

    @SerializedName(value = "p")
    var priority: Int,

    @SerializedName(value = "i")
    var isChecked: Boolean,
)
