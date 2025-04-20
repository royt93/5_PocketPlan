package com.fptflash.noteios.data.shoppinglist

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ShoppingItem(
    @SerializedName(value = "n")
    var name: String?,

    @SerializedName(value = "t")
    var tag: String,

    @SerializedName(value = "s")
    var suggestedUnit: String?,

    @SerializedName(value = "a")
    var amount: String?,

    @SerializedName(value = "u")
    var unit: String?,

    @SerializedName(value = "c")
    var checked: Boolean,
) {
    constructor(tag: String, checked: Boolean, position: String?) : this(null, tag, null, position, null, checked)
}
