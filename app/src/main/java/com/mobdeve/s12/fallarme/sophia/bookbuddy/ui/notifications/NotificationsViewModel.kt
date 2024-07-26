package com.mobdeve.s12.fallarme.sophia.bookbuddy.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

 /* private val _text = MutableLiveData<String>().apply {
        value = "Schedule"
    }
    val text: LiveData<String> = _text*/

    private val _repeatedDays = MutableLiveData<String>()
        val repeatedDays: LiveData<String> get() = _repeatedDays

    private val _repeatedHours = MutableLiveData<String>()
        val repeatedHours: LiveData<String> get() = _repeatedHours

    fun setSelectedDays(days: String) {
        _repeatedDays.value = days
    }

    fun setAdditionalText(text: String) {
        _repeatedHours.value = text
    }
}