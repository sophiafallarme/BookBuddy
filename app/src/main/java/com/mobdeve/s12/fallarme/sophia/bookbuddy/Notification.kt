package com.mobdeve.s12.fallarme.sophia.bookbuddy

data class Notification(
    val id: Long,
    val accountId: Long,
    val title: String,
    val message: String,
    val time: String
)
