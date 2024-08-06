package com.mobdeve.s12.fallarme.sophia.bookbuddy

data class NotificationPreferences(
    val selectedDays: List<String>,
    val selectedTimes: List<String>
)