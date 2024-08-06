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
    private var selectedCategories: MutableSet<String> = mutableSetOf()
    private var selectedStatuses: MutableSet<String> = mutableSetOf()

    private val bookUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Refresh the books list
            refreshBooksList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDbHelper = MyDbHelper(requireContext())
        Log.d("HomeFragment", "onCreate called")

        // Register the receiver for book updates
        val filter = IntentFilter("com.mobdeve.s12.fallarme.sophia.bookbuddy.BOOK_UPDATED")
        requireContext().registerReceiver(bookUpdateReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver
        requireContext().unregisterReceiver(bookUpdateReceiver)
    }

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
            val tabView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null)
            val tabTitle = tabView.findViewById<TextView>(R.id.tabTitle)
//
//            tab.text = when (position) {
//                0 -> "All"
//                1 -> "Currently Reading"
//                2 -> "To Read"
//                3 -> "Finished"
//                else -> "All"
//            }

            tabTitle.text = when (position) {
                0 -> "All"
                1 -> "Currently Reading"
                2 -> "To Read"
                3 -> "Finished"
                else -> "All"
            }

            // Apply custom view to tab
            tab.customView = tabView
        }.attach()

        val fabFilter = binding.fabFilter
        // Set up FAB to open the filter dialog
        fabFilter.setOnClickListener {
            showFilterDialog()
        }

        // Hide FAB for other tabs except "All"
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // Show FAB only for the "All" tab
                if (tab.position == 0) {
                    binding.fabFilter.show()
                } else {
                    binding.fabFilter.hide()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // No action needed
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // No action needed
            }
        })
    }

    private fun showFilterDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_filter, null)

        // Initialize RecyclerView for categories
        val rvCategories: RecyclerView = view.findViewById(R.id.rvCategories)
        rvCategories.layoutManager = GridLayoutManager(context, 2)

        if (accountId == -1L) {
            Log.e("HomeFragment", "Invalid account ID")
            return
        }

        val categories = myDbHelper.getCategoriesByAccountId(accountId)
        val categoryAdapter = CategoryAdapter(categories, selectedCategories)
        rvCategories.adapter = categoryAdapter

        // Initialize checkboxes for status
        val checkBoxCurrentlyReading: CheckBox = view.findViewById(R.id.checkBoxCurrentlyReading)
        val checkBoxToRead: CheckBox = view.findViewById(R.id.checkBoxToRead)
        val checkBoxFinished: CheckBox = view.findViewById(R.id.checkBoxFinished)

        // Set initial states of checkboxes
        checkBoxCurrentlyReading.isChecked = selectedStatuses.contains("Currently Reading")
        checkBoxToRead.isChecked = selectedStatuses.contains("To Read")
        checkBoxFinished.isChecked = selectedStatuses.contains("Finished")

        // Set up dialog buttons
        val buttonSave: Button = view.findViewById(R.id.buttonSave)
        val buttonReset: Button = view.findViewById(R.id.buttonReset)

        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        buttonSave.setOnClickListener {
            // Update selected statuses based on checkbox states
            selectedStatuses.clear()
            if (checkBoxCurrentlyReading.isChecked) selectedStatuses.add("Currently Reading")
            if (checkBoxToRead.isChecked) selectedStatuses.add("To Read")
            if (checkBoxFinished.isChecked) selectedStatuses.add("Finished")

//            applyCategoryFilter()
            applyCategoryAndStatusFilter()
            dialog.dismiss()
        }

        buttonReset.setOnClickListener {
            selectedCategories.clear()
            selectedStatuses.clear()
            resetFilters()
            dialog.dismiss()
        }
    }

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
//        Log.d("HomeFragment", "Filtered books count: All=${filteredBooksAll.size}, Currently Reading=${filteredBooksCurrentlyReading.size}, To Read=${filteredBooksToRead.size}, Finished=${filteredBooksFinished.size}")
//
//        // Refresh fragments to display filtered books
//        refreshFragments(
//            filteredBooksAll,
//            filteredBooksCurrentlyReading,
//            filteredBooksToRead,
//            filteredBooksFinished
//        )
//    }

    private fun applyCategoryAndStatusFilter() {
        Log.d("HomeFragment", "Applying filters: Categories=$selectedCategories, Statuses=$selectedStatuses")

        val filteredBooksAll = originalBooksAll.filter { book ->
            (selectedCategories.isEmpty() || selectedCategories.contains(book.category)) &&
                    (selectedStatuses.isEmpty() || selectedStatuses.contains(book.status))
        }

        // The other fragments will only display the status they represent, no need to filter by status there.
        Log.d("HomeFragment", "Filtered books count: All=${filteredBooksAll.size}")

        // Refresh the AllFragment with the filtered list
        (childFragmentManager.findFragmentByTag("f0") as? AllFragment)?.updateBooks(filteredBooksAll)
    }

    private fun resetFilters() {
        Log.d("HomeFragment", "Resetting filters")
        selectedCategories.clear()
        selectedStatuses.clear()
        refreshFragments(originalBooksAll, originalBooksCurrentlyReading, originalBooksToRead, originalBooksFinished)
    }

    private fun refreshBooksList() {
        // Refresh the books list from the database
        if (accountId != -1L) {
            originalBooksAll = myDbHelper.getBooksByAccountId(accountId)
            originalBooksCurrentlyReading = originalBooksAll.filter { it.status == "Currently Reading" }
            originalBooksToRead = originalBooksAll.filter { it.status == "To Read" }
            originalBooksFinished = originalBooksAll.filter { it.status == "Finished" }
            resetFilters()  // Refresh all lists to ensure updated data
        }
    }

    private fun refreshFragments(
        allBooks: List<Book>,
        currentlyReadingBooks: List<Book>,
        toReadBooks: List<Book>,
        finishedBooks: List<Book>
    ) {
        (childFragmentManager.findFragmentByTag("f0") as? AllFragment)?.updateBooks(allBooks)
//        (childFragmentManager.findFragmentByTag("f1") as? CurrentlyReadingFragment)?.updateBooks(currentlyReadingBooks)
//        (childFragmentManager.findFragmentByTag("f2") as? ToReadFragment)?.updateBooks(toReadBooks)
//        (childFragmentManager.findFragmentByTag("f3") as? FinishedFragment)?.updateBooks(finishedBooks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}