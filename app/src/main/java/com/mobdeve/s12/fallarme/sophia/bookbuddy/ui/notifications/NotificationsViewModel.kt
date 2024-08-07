package com.mobdeve.s12.fallarme.sophia.bookbuddy.ui.notifications




class NotificationsViewModel : ViewModel() {

    private val repository = NotificationRepository()
    private val _repeatedDays = MutableLiveData<String>()
    private val _repeatedHours = MutableLiveData<String>()
    val repeatedDays: LiveData<String> get() = _repeatedDays
    val repeatedHours: LiveData<String> get() = _repeatedHours

    init {
        loadPreferences()
    }

    private fun handleException(tag: String, message: String, exception: Exception) {
        Log.e(tag, message, exception)
        // Handle error appropriately, e.g., notify the user
    }

    private fun splitAndTrim(value: String): List<String> {
        return value.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    private suspend fun savePreferences(userId: String, preferences: NotificationPreferences) {
        try {
            repository.saveNotificationPreferences(userId, preferences)
        } catch (e: Exception) {
            handleException("savePreferences", "Error saving notification preferences", e)
        }
    }

    fun loadPreferences() {
        viewModelScope.launch {
            try {
                val user = FirebaseAuth.getInstance().currentUser
                user?.let {
                    repository.loadNotificationPreferences(it.uid)?.let { prefs ->
                        _repeatedDays.value = prefs.selectedDays.joinToString(", ")
                        _repeatedHours.value = prefs.selectedTimes.joinToString(", ")
                    }
                }
            } catch (e: Exception) {
                handleException("loadPreferences", "Error loading notification preferences", e)
            }
        }
    }

    fun setSelectedDays(days: String) {
        viewModelScope.launch {
            try {
                val user = FirebaseAuth.getInstance().currentUser
                val selectedDaysList = splitAndTrim(days)
                val selectedTimesList = _repeatedHours.value?.let { splitAndTrim(it) } ?: emptyList()

                val preferences = NotificationPreferences(selectedDaysList, selectedTimesList)
                user?.let { savePreferences(it.uid, preferences) }
                _repeatedDays.value = days
            } catch (e: Exception) {
                handleException("setSelectedDays", "Error saving notification preferences", e)
            }
        }
    }

    fun addSelectedTime(time: String) {
        viewModelScope.launch {
            try {
                val user = FirebaseAuth.getInstance().currentUser
                val times = _repeatedHours.value?.let { splitAndTrim(it) }?.toMutableList() ?: mutableListOf()
                if (!times.contains(time)) {
                    times.add(time)
                    val preferences = NotificationPreferences(
                        _repeatedDays.value?.let { splitAndTrim(it) } ?: emptyList(),
                        times
                    )
                    user?.let { savePreferences(it.uid, preferences) }
                    _repeatedHours.value = times.joinToString(", ")
                }
            } catch (e: Exception) {
                handleException("addSelectedTime", "Error adding selected time", e)
            }
        }
    }

    fun removeSelectedTime(time: String) {
        viewModelScope.launch {
            try {
                val user = FirebaseAuth.getInstance().currentUser
                val times = _repeatedHours.value?.let { splitAndTrim(it) }?.toMutableList() ?: mutableListOf()
                if (times.remove(time)) {
                    val preferences = NotificationPreferences(
                        _repeatedDays.value?.let { splitAndTrim(it) } ?: emptyList(),
                        times
                    )
                    user?.let { savePreferences(it.uid, preferences) }
                    _repeatedHours.value = times.joinToString(", ")
                }
            } catch (e: Exception) {
                handleException("removeSelectedTime", "Error removing selected time", e)
            }
        }
    }
}

