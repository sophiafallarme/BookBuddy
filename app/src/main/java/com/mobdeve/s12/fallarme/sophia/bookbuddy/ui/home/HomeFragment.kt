package com.mobdeve.s12.fallarme.sophia.bookbuddy.ui.home

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mobdeve.s12.fallarme.sophia.bookbuddy.Book
import com.mobdeve.s12.fallarme.sophia.bookbuddy.BookViewModel
import com.mobdeve.s12.fallarme.sophia.bookbuddy.MyDbHelper
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.AllFragment
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.CurrentlyReadingFragment
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.FinishedFragment
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.ToReadFragment
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.ViewPagerAdapter
import com.mobdeve.s12.fallarme.sophia.bookbuddy.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var myDbHelper: MyDbHelper
    private lateinit var viewModel: BookViewModel
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = binding.tabLayout
        val viewPager2 = binding.viewPager2
//        val fabFilter = binding.fabFilter

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        myDbHelper = MyDbHelper(requireContext())
        viewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)

        // Load all books initially
        viewModel.searchBooks("")
//        loadAllBooks()


        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "All"
                1 -> "Currently Reading"
                2 -> "To Read"
                3 -> "Finished"
                else -> "All"
            }
        }.attach()

//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                if (tab.text == "All") {
//                    fabFilter.show()
//                } else {
//                    fabFilter.hide()
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })
//
//        fabFilter.setOnClickListener {
//            showFilterDialog()
//        }



    }


//    private fun showFilterDialog() {
//        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_filter, null)
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setView(dialogView)
//        val alertDialog = builder.create()
//
//        val checkBoxCurrentlyReading = dialogView.findViewById<CheckBox>(R.id.checkBoxCurrentlyReading)
//        val checkBoxToRead = dialogView.findViewById<CheckBox>(R.id.checkBoxToRead)
//        val checkBoxFinished = dialogView.findViewById<CheckBox>(R.id.checkBoxFinished)
//        val spinnerCategories = dialogView.findViewById<Spinner>(R.id.spinnerCategories)
//
//        // Load categories from the database
//        val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
//        val accountId = sharedPreferences.getLong("accountId", -1L)
//        val categories = myDbHelper.getCategoriesByAccountId(accountId)
//
//        // Set up the spinner
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerCategories.adapter = adapter
//
//        dialogView.findViewById<Button>(R.id.buttonSave).setOnClickListener {
//            val selectedStatus = when {
//                checkBoxCurrentlyReading.isChecked -> "Currently Reading"
//                checkBoxToRead.isChecked -> "To Read"
//                checkBoxFinished.isChecked -> "Finished"
//                else -> null
//            }
//
//            val selectedCategory = spinnerCategories.selectedItem as? String
//
//            applyFilters(selectedStatus, selectedCategory)
//            alertDialog.dismiss()
//        }
//
//        dialogView.findViewById<Button>(R.id.buttonCancel).setOnClickListener {
//            alertDialog.dismiss()
//        }
//
//        dialogView.findViewById<Button>(R.id.buttonReset).setOnClickListener {
//            resetFilters()
//            alertDialog.dismiss()
//        }
//
//        alertDialog.show()
//    }


//    private fun showFilterDialog() {
//        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_filter, null)
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setView(dialogView)
//        val alertDialog = builder.create()
//
//        val spinnerCategories = dialogView.findViewById<Spinner>(R.id.spinnerCategories)
//
//        // Load categories from the database
//        val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
//        val accountId = sharedPreferences.getLong("accountId", -1L)
//        val categories = myDbHelper.getCategoriesByAccountId(accountId)
//
//        // Set up the spinner adapter
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerCategories.adapter = adapter
//
//        dialogView.findViewById<Button>(R.id.buttonSave).setOnClickListener {
//            val selectedCategory = spinnerCategories.selectedItem as? String
//            applyCategoryFilter(selectedCategory)
//            alertDialog.dismiss()
//        }
//
//        dialogView.findViewById<Button>(R.id.buttonReset).setOnClickListener {
//            resetFilters()
//            alertDialog.dismiss()
//        }
//
//        dialogView.findViewById<Button>(R.id.buttonCancel).setOnClickListener {
//            alertDialog.dismiss()
//        }
//
//        alertDialog.show()
//    }

//    private fun applyFilters(status: String?, category: String?) {
//        // Apply filters to the ViewModel
//        viewModel.searchBooks("") // Assuming searchBooks loads all books
//
//        viewModel.books.observe(viewLifecycleOwner) { books ->
//            val filteredBooks = books.filter { book ->
//                val statusMatch = status?.let { it == book.status } ?: true
//                val categoryMatch = category?.let { it == book.category } ?: true
//                statusMatch && categoryMatch
//            }
//
//            // Update the fragments with filtered books
//            updateFragments(filteredBooks, status)
//        }
//    }

//    private fun applyCategoryFilter(category: String?) {
//        Log.d("HomeFragment", "Applying filter for category: $category")
//        viewModel.searchBooks("") // Reload all books before applying filter
//        // Log entry into the function
//
//
//        // Observe the books from the ViewModel
//        viewModel.books.observe(viewLifecycleOwner) { books ->
//            // Log initial book list
//            Log.d("HomeFragment", "Total books available: ${books.size}")
//            // Filter books based on the selected category
//            val filteredBooks = books.filter { book ->
//                category?.let { it == book.category } ?: true
//            }
//
//            // Log the number of books after filtering
//            Log.d("HomeFragment", "Filtered books count: ${filteredBooks.size}")
//
//            // Update the AllFragment with filtered books
////            val adapter = binding.viewPager2.adapter as ViewPagerAdapter
////            val allFragment = adapter.getFragment(0) as AllFragment
////            allFragment.updateBooks(filteredBooks)
//                updateAllFragmentBooks(filteredBooks)
//
//            // Switch to All tab
//            binding.viewPager2.currentItem = 0
//        }
//    }
//    private fun resetFilters() {
//        // Reset filters and reload all books
//        viewModel.searchBooks("")
//
//    }

//    private fun updateAllFragmentBooks(filteredBooks: List<Book>) {
//        Log.d("HomeFragment", "Updating AllFragment with books count: ${filteredBooks.size}")
//        val adapter = binding.viewPager2.adapter as ViewPagerAdapter
//        val allFragment = adapter.getFragment(0) as AllFragment
//        allFragment.updateBooks(filteredBooks)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}