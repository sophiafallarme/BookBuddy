package com.mobdeve.s12.fallarme.sophia.bookbuddy.ui.home

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mobdeve.s12.fallarme.sophia.bookbuddy.Book
import com.mobdeve.s12.fallarme.sophia.bookbuddy.BookViewModel
import com.mobdeve.s12.fallarme.sophia.bookbuddy.MyDbHelper
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.AllAdapter
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.AllFragment
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.CategoryAdapter
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.CurrentlyReadingAdapter
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.CurrentlyReadingFragment
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.FinishedAdapter
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.FinishedFragment
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.ToReadAdapter
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

    private var accountId: Long = -1L
    private var originalBooksAll: List<Book> = emptyList()
    private var originalBooksCurrentlyReading: List<Book> = emptyList()
    private var originalBooksToRead: List<Book> = emptyList()
    private var originalBooksFinished: List<Book> = emptyList()

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var allAdapter: AllAdapter
    private lateinit var currentlyReadingAdapter: CurrentlyReadingAdapter
    private lateinit var toReadAdapter: ToReadAdapter
    private lateinit var finishedAdapter: FinishedAdapter
    private var selectedCategories: MutableSet<String> = mutableSetOf()

//    private val bookUpdateReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            // Refresh the books list
//            refreshBooksList()
//        }
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        myDbHelper = MyDbHelper(requireContext())
//        Log.d("HomeFragment", "onCreate called")
//
//        // Register the receiver for book updates
//        val filter = IntentFilter("com.mobdeve.s12.fallarme.sophia.bookbuddy.BOOK_UPDATED")
//        requireContext().registerReceiver(bookUpdateReceiver, filter)
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        // Unregister the receiver
//        requireContext().unregisterReceiver(bookUpdateReceiver)
//    }

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

        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager2

        // Initialize adapters for all tabs
        allAdapter = AllAdapter(emptyList())
        currentlyReadingAdapter = CurrentlyReadingAdapter(emptyList())
        toReadAdapter = ToReadAdapter(emptyList())
        finishedAdapter = FinishedAdapter(emptyList())

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        myDbHelper = MyDbHelper(requireContext())
        viewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)

        // Retrieve accountId from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        accountId = sharedPreferences.getLong("accountId", -1L)

        // Load all books initially
//        viewModel.searchBooks("")
        if (accountId != -1L) {
            originalBooksAll = myDbHelper.getBooksByAccountId(accountId)
            originalBooksCurrentlyReading = originalBooksAll.filter { it.status == "Currently Reading" }
            originalBooksToRead = originalBooksAll.filter { it.status == "To Read" }
            originalBooksFinished = originalBooksAll.filter { it.status == "Finished" }
        } else {
            Toast.makeText(context, "No account ID found", Toast.LENGTH_SHORT).show()
            Log.e("HomeFragment", "Account ID not found in SharedPreferences")
        }


        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "All"
                1 -> "Currently Reading"
                2 -> "To Read"
                3 -> "Finished"
                else -> "All"
            }
        }.attach()

//        val fabFilter = binding.fabFilter
        // Set up FAB to open the filter dialog
//        fabFilter.setOnClickListener {
//            showFilterDialog()
//        }
    }

//    private fun showFilterDialog() {
//        val builder = AlertDialog.Builder(requireContext())
//        val inflater = requireActivity().layoutInflater
//        val view = inflater.inflate(R.layout.dialog_filter, null)
//
//        // Initialize RecyclerView for categories
//        val rvCategories: RecyclerView = view.findViewById(R.id.rvCategories)
//        rvCategories.layoutManager = GridLayoutManager(context, 2)
//
//        if (accountId == -1L) {
//            Log.e("HomeFragment", "Invalid account ID")
//            return
//        }
//
//        val categories = myDbHelper.getCategoriesByAccountId(accountId)
//        val categoryAdapter = CategoryAdapter(categories, selectedCategories)
//        rvCategories.adapter = categoryAdapter
//
//        // Set up dialog buttons
//        val buttonSave: Button = view.findViewById(R.id.buttonSave)
//        val buttonReset: Button = view.findViewById(R.id.buttonReset)
//
//        builder.setView(view)
//        val dialog = builder.create()
//        dialog.show()
//
//        buttonSave.setOnClickListener {
//            applyCategoryFilter()
//            dialog.dismiss()
//        }
//
//        buttonReset.setOnClickListener {
//            selectedCategories.clear()
//            resetFilters()
//            dialog.dismiss()
//        }
//    }
//
//    private fun applyCategoryFilter() {
//        Log.d("HomeFragment", "Applying category filter: $selectedCategories")
//
//        val filteredBooksAll = if (selectedCategories.isEmpty()) {
//            originalBooksAll
//        } else {
//            originalBooksAll.filter { selectedCategories.contains(it.category) }
//        }
//
//        val filteredBooksCurrentlyReading = if (selectedCategories.isEmpty()) {
//            originalBooksCurrentlyReading
//        } else {
//            originalBooksCurrentlyReading.filter { selectedCategories.contains(it.category) }
//        }
//
//        val filteredBooksToRead = if (selectedCategories.isEmpty()) {
//            originalBooksToRead
//        } else {
//            originalBooksToRead.filter { selectedCategories.contains(it.category) }
//        }
//
//        val filteredBooksFinished = if (selectedCategories.isEmpty()) {
//            originalBooksFinished
//        } else {
//            originalBooksFinished.filter { selectedCategories.contains(it.category) }
//        }
//
//        allAdapter.updateBooks(filteredBooksAll)
//        currentlyReadingAdapter.updateBooks(filteredBooksCurrentlyReading)
//        toReadAdapter.updateBooks(filteredBooksToRead)
//        finishedAdapter.updateBooks(filteredBooksFinished)
//
//        // Refresh fragments to display filtered books
//        refreshFragments()
//    }
//
//    private fun resetFilters() {
//        Log.d("HomeFragment", "Resetting filters")
//
//        if (accountId == -1L) {
//            Log.e("HomeFragment", "Invalid account ID")
//            return
//        }
//
//        // Retrieve original book lists for each category
//        originalBooksAll = myDbHelper.getBooksByAccountId(accountId)
//        originalBooksCurrentlyReading = originalBooksAll.filter { it.status == "Currently Reading" }
//        originalBooksToRead = originalBooksAll.filter { it.status == "To Read" }
//        originalBooksFinished = originalBooksAll.filter { it.status == "Finished" }
//
//        allAdapter.updateBooks(originalBooksAll)
//        currentlyReadingAdapter.updateBooks(originalBooksCurrentlyReading)
//        toReadAdapter.updateBooks(originalBooksToRead)
//        finishedAdapter.updateBooks(originalBooksFinished)
//
//        // Refresh fragments to display original books
//        refreshFragments()
//    }
//
//    private fun refreshBooksList() {
//        // Reload original books and update adapters
//        originalBooksAll = myDbHelper.getBooksByAccountId(accountId)
//        originalBooksCurrentlyReading = originalBooksAll.filter { it.status == "Currently Reading" }
//        originalBooksToRead = originalBooksAll.filter { it.status == "To Read" }
//        originalBooksFinished = originalBooksAll.filter { it.status == "Finished" }
//
//        applyCategoryFilter() // Apply filters with the current selected categories
//    }
//
//    private fun refreshFragments() {
//        // Notify all fragments of the adapter changes
//        (childFragmentManager.fragments.find { it is AllFragment } as? AllFragment)?.updateBooks(allAdapter.currentBooks)
//        (childFragmentManager.fragments.find { it is CurrentlyReadingFragment } as? CurrentlyReadingFragment)?.updateBooks(currentlyReadingAdapter.currentBooks)
//        (childFragmentManager.fragments.find { it is ToReadFragment } as? ToReadFragment)?.updateBooks(toReadAdapter.currentBooks)
//        (childFragmentManager.fragments.find { it is FinishedFragment } as? FinishedFragment)?.updateBooks(finishedAdapter.currentBooks)
//    }
//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



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
//
//        }





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