package com.mobdeve.s12.fallarme.sophia.bookbuddy.collection

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class Book(
    val id: String,
    val title: String,
    val author: String,
    val image: String,
    val rating: String,
    //val status: String,
    // val category: String
    val status: String = "",
    val category: String = "",
    val accountId: Long
)