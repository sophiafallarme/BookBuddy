package com.mobdeve.s12.fallarme.sophia.bookbuddy.collection

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.fallarme.sophia.bookbuddy.Book
import com.mobdeve.s12.fallarme.sophia.bookbuddy.BookViewModel
import com.mobdeve.s12.fallarme.sophia.bookbuddy.MyDbHelper
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R


class FinishedFragment : Fragment() {

    private lateinit var bookAdapter: FinishedAdapter
    private lateinit var myDbHelper: MyDbHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize DB Helper
        myDbHelper = MyDbHelper(requireContext())
        Log.d("Finished Fragment", "onCreate called")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_all_page, container, false)
        val view = inflater.inflate(R.layout.fragment_finished, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.finished_recyclervw)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        bookAdapter = FinishedAdapter(emptyList())
        recyclerView.adapter = bookAdapter

        Log.d("Finished Fragment", "onCreateView called")

        //used to update fragment based on newly saved books
        viewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve accountId from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val accountId = sharedPreferences.getLong("accountId", -1L)

        // Retrieve books and update adapter
        if (accountId != -1L) {
            val books = myDbHelper.getBooksByAccountId(accountId, "Finished")
            Log.d("Finished Fragment", "Books retrieved: ${books.size}")
            books.forEach { Log.d("Finished Fragment", "Book: ${it.title}, Status: ${it.status}") }
            bookAdapter.updateBooks(books)
        } else {
            // Handle the case where accountId is not found
            Toast.makeText(context, "No account ID found", Toast.LENGTH_SHORT).show()
            Log.e("Finished Fragment", "Account ID not found in SharedPreferences")

        }

        //used to update fragment based on newly saved books
//        viewModel.books.observe(viewLifecycleOwner) { books ->
//            bookAdapter.updateBooks(books)
//        }

        // Observe books in ViewModel
//        viewModel.books.observe(viewLifecycleOwner) { books ->
//            val finishedReadingBooks = books.filter { it.status == "Finished" }
//            Log.d("Finished Fragment", "Finished Books observed: ${finishedReadingBooks.size}")
//            finishedReadingBooks.forEach { Log.d("CurrentlyReadingFragment", "Book: ${it.title}, Status: ${it.status}") }
//            bookAdapter.updateBooks(finishedReadingBooks)
//        }

    }

//    fun updateBooks(books: List<Book>) {
//        bookAdapter.updateBooks(books)
//    }
}