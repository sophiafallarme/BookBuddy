package com.mobdeve.s12.fallarme.sophia.bookbuddy.collection

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mobdeve.s12.fallarme.sophia.bookbuddy.Book
import com.mobdeve.s12.fallarme.sophia.bookbuddy.BookDetailsActivity
import com.mobdeve.s12.fallarme.sophia.bookbuddy.BookViewModel
import com.mobdeve.s12.fallarme.sophia.bookbuddy.MyDbHelper
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R


class CurrentlyReadingFragment : Fragment() {

    private lateinit var bookAdapter: CurrentlyReadingAdapter
    private lateinit var myDbHelper: MyDbHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: BookViewModel
    private var accountId: Long = -1L
    private var originalBooks: List<Book> = emptyList()
    private val selectedCategories = mutableSetOf<String>()  // Store selected categories


    private val bookUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Refresh the books list
            refreshBooksList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize DB Helper
        myDbHelper = MyDbHelper(requireContext())
        Log.d("CRFragment", "onCreate called")

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_all_page, container, false)
        val view = inflater.inflate(R.layout.fragment_currently_reading, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        bookAdapter = CurrentlyReadingAdapter(emptyList())
        recyclerView.adapter = bookAdapter

        Log.d("CRFragment", "onCreateView called")

        //used to update fragment based on newly saved books
        viewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)

        // Set up FAB to open the filter dialog
//        view.findViewById<FloatingActionButton>(R.id.fabFilter).setOnClickListener {
//            showFilterDialog()
//        }


        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve accountId from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        accountId = sharedPreferences.getLong("accountId", -1L)

        // Retrieve books and update adapter
        if (accountId != -1L) {
//            val books = myDbHelper.getBooksByAccountId(accountId, "Currently Reading")
//            Log.d("CurrentlyReadingFragment", "Books retrieved: ${books.size}")
//            books.forEach { Log.d("CurrentlyReadingFragment", "Book: ${it.title}, Status: ${it.status}") }
//            bookAdapter.updateBooks(books)

            originalBooks = myDbHelper.getBooksByAccountId(accountId, "Currently Reading")
            bookAdapter.updateBooks(originalBooks)

        } else {
            // Handle the case where accountId is not found
            Toast.makeText(context, "No account ID found", Toast.LENGTH_SHORT).show()
            Log.e("CRFragment", "Account ID not found in SharedPreferences")

        }

        // Set the click listener for items in the RecyclerView
        bookAdapter.setOnItemClickListener(object : CurrentlyReadingAdapter.OnItemClickListener {
            override fun onItemClick(book: Book) {
                Log.d("AllFragment", "Clicked book: ${book.title}")
                val intent = Intent(requireContext(), BookDetailsActivity::class.java)
                intent.putExtra("book", book)  // Pass the clicked book to BookDetailsActivity
                startActivity(intent)
            }
        })


    }

    private fun refreshBooksList() {
        val books = myDbHelper.getBooksByAccountId(accountId).filter { it.status == "Currently Reading" }
        Log.d("CurrentlyReadingFragment", "Currently Reading books refreshed: ${books.size}")
        bookAdapter.updateBooks(books)
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
//        // Use dynamic accountId from SharedPreferences
//        if (accountId == -1L) {
//            Log.e("CRFragment", "Invalid account ID")
//            return
//        }
//
//        // Retrieve categories only within the "Currently Reading" status
//        val currentlyReadingBooks = myDbHelper.getBooksByAccountId(accountId, "Currently Reading")
//        val categories = currentlyReadingBooks.map { it.category }.distinct()
//
//
//        if (categories.isEmpty()) {
//            Log.d("CRFragment", "No categories found for currently reading books")
//            return
//        }
//
//        val categoryAdapter = CategoryAdapter(categories, selectedCategories)
//        rvCategories.adapter = categoryAdapter
//
//        // Set up dialog buttons
//        val buttonSave: Button = view.findViewById(R.id.buttonSave)
//        val buttonReset: Button = view.findViewById(R.id.buttonReset)
//
//        builder.setView(view)
//
//        if (categories.isEmpty()) {
//            Log.d("CRFragment", "No categories found for account ID: $accountId")
//            return
//        }
//
//        // Create and show the dialog
//        val dialog = builder.create()
//        dialog.show()
//
//        buttonSave.setOnClickListener {
//            applyCategoryFilter()
//            dialog.dismiss()
//        }
//
//        buttonReset.setOnClickListener {
//              selectedCategories.clear()
//              resetFilters()
//              dialog.dismiss()
//
//        }
//    }
//
//    private fun applyCategoryFilter() {
//        val filteredBooks = if (selectedCategories.isEmpty()) {
//            originalBooks
//        } else {
//            originalBooks.filter { it.category in selectedCategories }
//        }
//
//        bookAdapter.updateBooks(filteredBooks)
//    }
//
//    private fun resetFilters() {
//        Log.d("CRFragment", "Resetting filters")
//
//        // Retrieve books again to restore the original state
//        if (accountId != -1L) {
//            val originalBooks = myDbHelper.getBooksByAccountId(accountId, "Currently Reading")
//            bookAdapter.updateBooks(originalBooks)
//        } else {
//            Log.e("CRFragment", "Account ID not found, cannot reset filters")
//        }
//    }
//
}