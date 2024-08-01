package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimeAdapter(
    private val times: MutableList<String>,
    private val onDeleteClicked: (String) -> Unit
) : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    inner class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time, parent, false)
        return TimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val time = times[position]
        holder.timeTextView.text = time
        holder.deleteButton.setOnClickListener {
            onDeleteClicked(time)
        }
    }

    override fun getItemCount(): Int {
        return times.size
    }

    // Method to add a time to the list
    fun addTime(time: String) {
        if (!times.contains(time)) {
            times.add(time)
            notifyItemInserted(times.size - 1)
        }
    }

    // Method to remove a time from the list
    fun removeTime(time: String) {
        val position = times.indexOf(time)
        if (position != -1) {
            times.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}