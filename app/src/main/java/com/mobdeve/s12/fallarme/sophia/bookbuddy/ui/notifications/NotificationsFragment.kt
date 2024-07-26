package com.mobdeve.s12.fallarme.sophia.bookbuddy.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R
import com.mobdeve.s12.fallarme.sophia.bookbuddy.TimeAdapter
import com.mobdeve.s12.fallarme.sophia.bookbuddy.databinding.FragmentNotificationsBinding
import com.mobdeve.s12.fallarme.sophia.bookbuddy.databinding.HoursRepeatBinding
import com.mobdeve.s12.fallarme.sophia.bookbuddy.databinding.ScheduleRepeatBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        return view
    }

    private fun showSchedulePopup() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding = ScheduleRepeatBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)

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
                selectedDays.size == 7 -> getString(R.string.everyday)
                selectedDays.isEmpty() -> getString(R.string.no_days_selected)
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

        initializeNumberPickers(dialogBinding)

        builder.setPositiveButton("OK") { _, _ ->
            val hour = dialogBinding.numberPickerHour.value
            val minute = dialogBinding.numberPickerMinute.value
            val amPm = if (dialogBinding.numberPickerAmPm.value == 0) "AM" else "PM"

            val timeText = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} $amPm"
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
        timeAdapter.notifyItemInserted(selectedTimes.size - 1) // Efficiently notify about new item
        checkAndUpdateHoursPopupVisibility()
    }

    private fun removeTime(timeText: String) {
        val index = selectedTimes.indexOf(timeText)
        if (index != -1) {
            selectedTimes.removeAt(index)
            timeAdapter.notifyItemRemoved(index) // Efficiently notify about removed item
            checkAndUpdateHoursPopupVisibility()
        }
    }

    private fun getFormattedMinutes(): Array<String> {
        return Array(60) { minute ->
            minute.toString().padStart(2, '0')
        }
    }

    private fun checkAndUpdateHoursPopupVisibility() {
        if (selectedTimes.isEmpty()) {
            binding.hoursPopup.visibility = View.VISIBLE
        } else {
            binding.hoursPopup.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}