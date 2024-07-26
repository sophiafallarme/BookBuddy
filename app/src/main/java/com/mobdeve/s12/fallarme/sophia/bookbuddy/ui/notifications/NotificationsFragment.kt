package com.mobdeve.s12.fallarme.sophia.bookbuddy.ui.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.fallarme.sophia.bookbuddy.NotificationReceiver
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R
import com.mobdeve.s12.fallarme.sophia.bookbuddy.TimeAdapter
import com.mobdeve.s12.fallarme.sophia.bookbuddy.databinding.FragmentNotificationsBinding
import com.mobdeve.s12.fallarme.sophia.bookbuddy.databinding.HoursRepeatBinding
import com.mobdeve.s12.fallarme.sophia.bookbuddy.databinding.ScheduleRepeatBinding
import java.util.*

class NotificationsFragment : Fragment() {

    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 1
        private const val PREFS_NAME = "NotificationsPrefs"
        private const val PREF_SELECTED_DAYS = "selected_days"
        private const val PREF_SELECTED_TIMES = "selected_times"
    }

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationsViewModel: NotificationsViewModel
    private val selectedTimes = mutableListOf<String>()
    private lateinit var timeAdapter: TimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val view = binding.root

        notificationsViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]

        timeAdapter = TimeAdapter(selectedTimes) { time ->
            removeTime(time)
        }
        binding.recyclerViewTimes.adapter = timeAdapter
        binding.recyclerViewTimes.layoutManager = LinearLayoutManager(requireContext())

        notificationsViewModel.repeatedDays.observe(viewLifecycleOwner) { days ->
            binding.daysPopup.text = days
        }

        notificationsViewModel.repeatedHours.observe(viewLifecycleOwner) { text ->
            binding.hoursPopup.text = text
        }

        binding.daysPopup.setOnClickListener {
            showSchedulePopup()
        }

        binding.buttonAddTime.setOnClickListener {
            showTimePickerPopup { selectedTime ->
                addTime(selectedTime)
            }
        }

        checkNotificationPermission()
        loadPreferences()

        return view
    }

    private fun showSchedulePopup() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding = ScheduleRepeatBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)

        val selectedDays = loadSelectedDays()
        dialogBinding.checkBoxMonday.isChecked = selectedDays.contains("Monday")
        dialogBinding.checkBoxTuesday.isChecked = selectedDays.contains("Tuesday")
        dialogBinding.checkBoxWednesday.isChecked = selectedDays.contains("Wednesday")
        dialogBinding.checkBoxThursday.isChecked = selectedDays.contains("Thursday")
        dialogBinding.checkBoxFriday.isChecked = selectedDays.contains("Friday")
        dialogBinding.checkBoxSaturday.isChecked = selectedDays.contains("Saturday")
        dialogBinding.checkBoxSunday.isChecked = selectedDays.contains("Sunday")

        builder.setPositiveButton("OK") { _, _ ->
            val localSelectedDays = mutableListOf<String>()
            if (dialogBinding.checkBoxMonday.isChecked) localSelectedDays.add("Monday")
            if (dialogBinding.checkBoxTuesday.isChecked) localSelectedDays.add("Tuesday")
            if (dialogBinding.checkBoxWednesday.isChecked) localSelectedDays.add("Wednesday")
            if (dialogBinding.checkBoxThursday.isChecked) localSelectedDays.add("Thursday")
            if (dialogBinding.checkBoxFriday.isChecked) localSelectedDays.add("Friday")
            if (dialogBinding.checkBoxSaturday.isChecked) localSelectedDays.add("Saturday")
            if (dialogBinding.checkBoxSunday.isChecked) localSelectedDays.add("Sunday")

            val daysText = when {
                selectedDays.size == 7 -> "Everyday"
                selectedDays.isEmpty() -> "No days selected"
                else -> selectedDays.joinToString(", ")
            }
            notificationsViewModel.setSelectedDays(daysText)

            saveSelectedDays(localSelectedDays)
            Toast.makeText(requireContext(), getString(R.string.selected_days, daysText), Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel", null)
        builder.create().show()
    }

    private fun showTimePickerPopup(onTimeSelected: (String) -> Unit) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding = HoursRepeatBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)

        initializeNumberPickers(dialogBinding)

        builder.setPositiveButton("OK") { _, _ ->
            val hour = dialogBinding.numberPickerHour.value
            val minute = dialogBinding.numberPickerMinute.value
            val amPm = if (dialogBinding.numberPickerAmPm.value == 0) "AM" else "PM"

            val timeText = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} $amPm"

            notificationsViewModel.setAdditionalText(timeText)
            onTimeSelected(timeText)
        }

        builder.setNegativeButton("Cancel", null)
        builder.create().show()
    }

    private fun initializeNumberPickers(binding: HoursRepeatBinding) {
        binding.numberPickerHour.apply {
            minValue = 1
            maxValue = 12
            wrapSelectorWheel = true
        }

        binding.numberPickerMinute.apply {
            minValue = 0
            maxValue = 59
            wrapSelectorWheel = true
            displayedValues = getFormattedMinutes()
        }

        binding.numberPickerAmPm.apply {
            minValue = 0
            maxValue = 1
            wrapSelectorWheel = true
            displayedValues = resources.getStringArray(R.array.am_pm_values)
        }
    }

    private fun addTime(timeText: String) {
        if (selectedTimes.contains(timeText)) return // Prevent duplicate times

        selectedTimes.add(timeText)
        timeAdapter.notifyItemInserted(selectedTimes.size - 1) // Use specific notify method
        saveSelectedTimes()
        scheduleNotification(timeText)
        binding.hoursPopup.visibility = View.GONE
    }

    private fun removeTime(timeText: String) {
        val index = selectedTimes.indexOf(timeText)
        if (index != -1) {
            selectedTimes.removeAt(index)
            timeAdapter.notifyItemRemoved(index) // Use specific notify method
            saveSelectedTimes()
        }

        if (selectedTimes.isEmpty()) {
            binding.hoursPopup.visibility = View.VISIBLE
        }
    }

    private fun getFormattedMinutes(): Array<String> {
        return Array(60) { minute ->
            minute.toString().padStart(2, '0')
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
        }
    }

    @Deprecated("Deprecated in Android 13", ReplaceWith("requestPermissions"))
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Notification permission granted.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Notification permission denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun scheduleNotification(timeText: String) {
        val (hour, minute, amPm) = parseTime(timeText)
        val selectedDays = notificationsViewModel.repeatedDays.value?.split(", ") ?: emptyList()

        selectedDays.forEach { day ->
            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                set(Calendar.AM_PM, if (amPm == "AM") Calendar.AM else Calendar.PM)
                set(Calendar.DAY_OF_WEEK, getDayOfWeek(day))
            }

            if (calendar.timeInMillis < System.currentTimeMillis()) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
            }

            val intent = Intent(requireContext(), NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(requireContext(), calendar.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                pendingIntent
            )
        }
    }

    private fun parseTime(timeText: String): Triple<Int, Int, String> {
        val parts = timeText.split(" ", ":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()
        val amPm = parts[2]
        return Triple(hour, minute, amPm)
    }

    private fun getDayOfWeek(day: String): Int {
        return when (day) {
            "Sunday" -> Calendar.SUNDAY
            "Monday" -> Calendar.MONDAY
            "Tuesday" -> Calendar.TUESDAY
            "Wednesday" -> Calendar.WEDNESDAY
            "Thursday" -> Calendar.THURSDAY
            "Friday" -> Calendar.FRIDAY
            "Saturday" -> Calendar.SATURDAY
            else -> Calendar.MONDAY
        }
    }

    private fun saveSelectedDays(days: List<String>) {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putStringSet(PREF_SELECTED_DAYS, days.toSet())
            apply()
        }
    }

    private fun loadSelectedDays(): Set<String> {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet(PREF_SELECTED_DAYS, emptySet()) ?: emptySet()
    }

    private fun saveSelectedTimes() {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putStringSet(PREF_SELECTED_TIMES, selectedTimes.toSet())
            apply()
        }
    }

    private fun loadPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedDays = sharedPreferences.getStringSet(PREF_SELECTED_DAYS, emptySet())
        val savedTimes = sharedPreferences.getStringSet(PREF_SELECTED_TIMES, emptySet())

        savedDays?.let { days ->
            val daysText = when {
                days.size == 7 -> "Everyday"
                days.isEmpty() -> "No days selected"
                else -> days.joinToString(", ")
            }
            notificationsViewModel.setSelectedDays(daysText)
        }

        savedTimes?.let { times ->
            // Save the old list for comparison
            val oldList = ArrayList(selectedTimes)

            // Clear and add new items
            selectedTimes.clear()
            selectedTimes.addAll(times)

            // Determine the indices of added and removed items
            val removedItems = oldList - selectedTimes.toSet()
            val addedItems = selectedTimes - oldList.toSet()

            // Notify removed items
            removedItems.forEach { item ->
                val index = oldList.indexOf(item)
                if (index != -1) {
                    timeAdapter.notifyItemRemoved(index)
                }
            }

            // Notify inserted items
            addedItems.forEach { item ->
                val index = selectedTimes.indexOf(item)
                if (index != -1) {
                    timeAdapter.notifyItemInserted(index)
                }
            }

            // Notify item changes for any modifications in the existing list
            val minSize = minOf(oldList.size, selectedTimes.size)
            for (i in 0 until minSize) {
                if (oldList[i] != selectedTimes[i]) {
                    timeAdapter.notifyItemChanged(i)
                }
            }

            // Update the visibility of hoursPopup based on the data set
            binding.hoursPopup.visibility = if (selectedTimes.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}