package com.mobdeve.s12.fallarme.sophia.bookbuddy.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Your Collection"
    }
    val text: LiveData<String> = _text
}