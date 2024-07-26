package com.mobdeve.s12.fallarme.sophia.bookbuddy.collection

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mobdeve.s12.fallarme.sophia.bookbuddy.Book
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R
import com.mobdeve.s12.fallarme.sophia.bookbuddy.MyDbHelper
import com.mobdeve.s12.fallarme.sophia.bookbuddy.BookViewModel



class AllFragment : Fragment() {
    private lateinit var bookAdapter: AllAdapter
    private lateinit var myDbHelper: MyDbHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: BookViewModel
    private var selectedCategory: String? = null
    private var accountId: Long = -1L
    private var originalBooks: List<Book> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize DB Helper
        myDbHelper = MyDbHelper(requireContext())
        Log.d("AllFragment", "onCreate called")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_all_page, container, false)
        val view = inflater.inflate(R.layout.fragment_all_page, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.all_recyclervw)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        //Initialize adapter with an empty list to avoid errors
        bookAdapter = AllAdapter(emptyList())
        recyclerView.adapter = bookAdapter

        Log.d("AllFragment", "onCreateView called")

        //used to update fragment based on newly saved books
        viewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)

        // Initialize Database Helper
        myDbHelper = MyDbHelper(requireContext())  // Assuming you have a constructor that accepts context

        // Observe books LiveData
//        viewModel.books.observe(viewLifecycleOwner) { books ->
//            Log.d("AllFragment", "Observed books: ${books.size}")
//            bookAdapter.updateBooks(books)
//        }

        // Set up FAB to open the filter dialog
        view.findViewById<FloatingActionButton>(R.id.fabFilter).setOnClickListener {
            showFilterDialog()
        }

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve accountId from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        accountId = sharedPreferences.getLong("accountId", -1L)

        // Retrieve books and update adapter
        if (accountId != -1L) {
            originalBooks = myDbHelper.getBooksByAccountId(accountId)
//            val books = myDbHelper.getBooksByAccountId(accountId)
            bookAdapter.updateBooks(originalBooks)
        } else {
            // Handle the case where accountId is not found
            Toast.makeText(context, "No account ID found", Toast.LENGTH_SHORT).show()
            Log.e("AllFragment", "Account ID not found in SharedPreferences")

        }


    }

    private fun showFilterDialog() {

        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_filter, null)

        // Initialize views in the dialog
        val spinnerCategories: Spinner = view.findViewById(R.id.spinnerCategories)
        val buttonSave: Button = view.findViewById(R.id.buttonSave)
        val buttonReset: Button = view.findViewById(R.id.buttonReset)
        val buttonCancel: Button = view.findViewById(R.id.buttonCancel)

        // Use dynamic accountId from SharedPreferences
        if (accountId == -1L) {
            Log.e("AllFragment", "Invalid account ID")
            return
        }

        val categories = myDbHelper.getCategoriesByAccountId(accountId)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        spinnerCategories.adapter = adapter

        builder.setView(view)

        if (categories.isEmpty()) {
            Log.d("AllFragment", "No categories found for account ID: $accountId")
            return
        }

//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle("Select Category")
//        builder.setItems(categories.toTypedArray()) { _, which ->
//            selectedCategory = categories[which]
//            Log.d("AllFragment", "Selected category: $selectedCategory")
//            applyCategoryFilter(selectedCategory)
//        }
//        builder.create().show()

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()

        buttonSave.setOnClickListener {
            val selectedCategory = spinnerCategories.selectedItem as? String
            applyCategoryFilter(selectedCategory)
            dialog.dismiss()
        }

        buttonReset.setOnClickListener {
            spinnerCategories.setSelection(0) // Reset to default
            resetFilters()
            dialog.dismiss()
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun applyCategoryFilter(category: String?) {
        Log.d("AllFragment", "Applying category filter: $category")

        // Get current books from the adapter
//        val currentBooks = bookAdapter.getCurrentBooks()

        // Apply category filter
        val filteredBooks = if (category.isNullOrEmpty()) {
            originalBooks
        } else {
            originalBooks.filter { it.category == category }
        }

        Log.d("AllFragment", "Filtered books count: ${filteredBooks.size}")

        // Update the adapter with filtered books
        bookAdapter.updateBooks(filteredBooks)
    }

    private fun resetFilters() {
        Log.d("AllFragment", "Resetting filters")

        // Retrieve books again to restore the original state
        if (accountId != -1L) {
            val originalBooks = myDbHelper.getBooksByAccountId(accountId)
            bookAdapter.updateBooks(originalBooks)
        } else {
            Log.e("AllFragment", "Account ID not found, cannot reset filters")
        }
    }



}