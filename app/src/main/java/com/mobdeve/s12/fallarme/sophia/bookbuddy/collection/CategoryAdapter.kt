package com.mobdeve.s12.fallarme.sophia.bookbuddy.collection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R

class CategoryAdapter(
    private val categories: List<String>,
    private val selectedCategories: MutableSet<String>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryCheckBox: CheckBox = itemView.findViewById(R.id.checkboxCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryCheckBox.text = category
        holder.categoryCheckBox.isChecked = selectedCategories.contains(category)

        holder.categoryCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedCategories.add(category)
            } else {
                selectedCategories.remove(category)
            }
        }
    }

    override fun getItemCount(): Int = categories.size
}
