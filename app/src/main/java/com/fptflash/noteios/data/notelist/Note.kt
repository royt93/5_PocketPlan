package com.fptflash.noteios.data.notelist

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Note(
    @SerializedName(value = "t")
    var title: String,

    @SerializedName(value = "ct")
    var content: String?,

    @SerializedName(value = "cl")
    var color: NoteColors,

    @SerializedName(value = "nl")
    var noteList: NoteList,
) {
    constructor(name: String, color: NoteColors, noteList: NoteList)
            : this(name, null, color, noteList)

    constructor(name: String, content: String, color: NoteColors)
            : this(name, content, color, NoteList())
}
