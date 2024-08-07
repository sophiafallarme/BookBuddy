package com.mobdeve.s12.fallarme.sophia.bookbuddy.ui.notifications

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R
import com.mobdeve.s12.fallarme.sophia.bookbuddy.TimeAdapter
import com.mobdeve.s12.fallarme.sophia.bookbuddy.databinding.FragmentNotificationsBinding
import com.mobdeve.s12.fallarme.sophia.bookbuddy.databinding.HoursRepeatBinding
import com.mobdeve.s12.fallarme.sophia.bookbuddy.databinding.ScheduleRepeatBinding
import java.util.*

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationsViewModel: NotificationsViewModel
    private val selectedTimes = mutableListOf<String>()
    private lateinit var timeAdapter: TimeAdapter

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Notification permission granted.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Notification permission denied.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val view = binding.root

        notificationsViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]

        setupRecyclerView()
        setupObservers()

        binding.daysPopup.setOnClickListener {
            showSchedulePopup()
        }

        binding.buttonAddTime.setOnClickListener {
            showTimePickerPopup { selectedTime ->
                addTime(selectedTime)
            }
        }

        checkNotificationPermission()
        notificationsViewModel.loadPreferences()

        return view
    }

    private fun setupRecyclerView() {
        timeAdapter = TimeAdapter(selectedTimes) { time ->
            removeTime(time)
        }
        binding.recyclerViewTimes.apply {
            adapter = timeAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = true
        }
    }

    private fun setupObservers() {
        notificationsViewModel.repeatedDays.observe(viewLifecycleOwner) { days ->
            binding.daysPopup.text = days ?: "No days selected"
        }

        notificationsViewModel.repeatedHours.observe(viewLifecycleOwner) { times ->
            val oldTimes = ArrayList(selectedTimes)
            selectedTimes.clear()
            if (!times.isNullOrEmpty()) {
                selectedTimes.addAll(times.split(", ").map { it.trim() }.filter { it.isNotEmpty() })
                sortSelectedTimes()
            }

            updateRecyclerView(oldTimes, selectedTimes)
            updateHoursPopupVisibility()
        }
    }

    private fun updateRecyclerView(oldTimes: List<String>, newTimes: List<String>) {
        val diffCallback = TimeDiffCallback(oldTimes, newTimes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        // Apply the diff result to the adapter
        diffResult.dispatchUpdatesTo(timeAdapter)
    }

    private fun showSchedulePopup() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding = ScheduleRepeatBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)

        val days = notificationsViewModel.repeatedDays.value?.split(", ") ?: emptyList()
        dialogBinding.checkBoxMonday.isChecked = days.contains("Monday")
        dialogBinding.checkBoxTuesday.isChecked = days.contains("Tuesday")
        dialogBinding.checkBoxWednesday.isChecked = days.contains("Wednesday")
        dialogBinding.checkBoxThursday.isChecked = days.contains("Thursday")
        dialogBinding.checkBoxFriday.isChecked = days.contains("Friday")
        dialogBinding.checkBoxSaturday.isChecked = days.contains("Saturday")
        dialogBinding.checkBoxSunday.isChecked = days.contains("Sunday")

        builder.setPositiveButton("OK") { _, _ ->
            val selectedDays = mutableListOf<String>()
            if (dialogBinding.checkBoxMonday.isChecked) selectedDays.add("Monday")
            if (dialogBinding.checkBoxTuesday.isChecked) selectedDays.add("Tuesday")
            if (dialogBinding.checkBoxWednesday.isChecked) selectedDays.add("Wednesday")
            if (dialogBinding.checkBoxThursday.isChecked) selectedDays.add("Thursday")
            if (dialogBinding.checkBoxFriday.isChecked) selectedDays.add("Friday")
            if (dialogBinding.checkBoxSaturday.isChecked) selectedDays.add("Saturday")
            if (dialogBinding.checkBoxSunday.isChecked) selectedDays.add("Sunday")

            val daysText = when {
                selectedDays.size == 7 -> "Everyday"
                selectedDays.isEmpty() -> "No days selected"
                else -> selectedDays.joinToString(", ")
            }
            notificationsViewModel.setSelectedDays(daysText)
            Toast.makeText(requireContext(), getString(R.string.selected_days, daysText), Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel", null)
        builder.create().show()
    }


    private fun showTimePickerPopup(onTimeSelected: (String) -> Unit) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding = HoursRepeatBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)

        // Set TimePicker to 12-hour format
        dialogBinding.timePicker.setIs24HourView(false)

        builder.setPositiveButton("OK") { _, _ ->
            val hour = dialogBinding.timePicker.hour
            val minute = dialogBinding.timePicker.minute
            val amPm = if (dialogBinding.timePicker.hour < 12) "AM" else "PM"

            // Convert 24-hour time to 12-hour time with AM/PM
            val timeText = formatTime(hour, minute, amPm)
            if (!selectedTimes.contains(timeText)) {
                onTimeSelected(timeText)
            } else {
                Toast.makeText(requireContext(), "Time already selected", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.create().show()
    }

    private fun formatTime(hour: Int, minute: Int, amPm: String): String {
        val formattedHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        return String.format(Locale.getDefault(), "%02d:%02d %s", formattedHour, minute, amPm)
    }

    private fun addTime(timeText: String) {
        if (!selectedTimes.contains(timeText)) {
            selectedTimes.add(timeText)
            sortSelectedTimes()
            // Update the RecyclerView with changes
            timeAdapter.notifyItemInserted(selectedTimes.indexOf(timeText))
            notificationsViewModel.addSelectedTime(timeText)
            updateHoursPopupVisibility()
        }
    }

    private fun removeTime(timeText: String) {
        val index = selectedTimes.indexOf(timeText)
        if (index != -1) {
            selectedTimes.removeAt(index)
            timeAdapter.notifyItemRemoved(index)
            notificationsViewModel.removeSelectedTime(timeText)
            updateHoursPopupVisibility()
        }
    }

    private fun sortSelectedTimes() {
        val oldTimes = ArrayList(selectedTimes)  // Create a copy of the current list
        val sortedTimes = selectedTimes.sortedWith { time1, time2 ->
            val (hour1, minute1) = parseTime(time1)
            val (hour2, minute2) = parseTime(time2)

            // Compare hours first, then minutes
            val hourComparison = compareValues(hour1, hour2)
            if (hourComparison != 0) {
                hourComparison
            } else {
                compareValues(minute1, minute2)
            }
        }

        // Update the list with sorted times
        selectedTimes.clear()
        selectedTimes.addAll(sortedTimes)

        // Calculate the diff between the old and new lists
        val diffCallback = TimeDiffCallback(oldTimes, selectedTimes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        // Notify the adapter with the diff result
        diffResult.dispatchUpdatesTo(timeAdapter)
    }

    private fun parseTime(time: String): Pair<Int, Int> {
        val parts = time.split(" ")
        if (parts.size != 2) throw IllegalArgumentException("Invalid time format")

        val timePart = parts[0]
        val amPm = parts[1]
        val timeParts = timePart.split(":")

        if (timeParts.size != 2) throw IllegalArgumentException("Invalid time format")

        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        return when (amPm) {
            "AM" -> if (hour == 12) Pair(0, minute) else Pair(hour, minute)
            "PM" -> if (hour == 12) Pair(12, minute) else Pair(hour + 12, minute)
            else -> throw IllegalArgumentException("Invalid AM/PM format")
        }
    }

    private fun updateHoursPopupVisibility() {
        binding.hoursPopup.visibility = if (selectedTimes.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) -> {
                    Toast.makeText(requireContext(), "Notification permission already granted.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class TimeDiffCallback(
        private val oldList: List<String>,
        private val newList: List<String>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}