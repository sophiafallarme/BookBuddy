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


class CurrentlyReadingFragment : Fragment() {

//    private var _binding: FragmentCurrentlyReadingBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: CurrentlyReadingAdapter
//    private lateinit var layoutManager: RecyclerView.LayoutManager
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
////        return inflater.inflate(R.layout.fragment_currently_reading, container, false)
//        _binding = FragmentCurrentlyReadingBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        recyclerView = binding.recyclerView
//        layoutManager = GridLayoutManager(context, 2)
//        adapter = CurrentlyReadingAdapter(getSampleData())
//
//        recyclerView.layoutManager = layoutManager
//        recyclerView.adapter = adapter
//    }
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//
//    private fun getSampleData(): ArrayList<Book> {
////        return listOf("Rich Dad, Poor Dad", "The Alchemist", "Negotiation 101", "The Cruel Prince", "Item 5")
//        val books = ArrayList<Book>()
//        books.add(Book("I want a better catastrophe", "Andre Boyd", "Non-fiction", R.drawable.book1))
//        books.add(Book("The Midnight Library", "Matt Haig", "Fiction", R.drawable.book2))
//        books.add(Book("Rich Dad, Poor Dad", "Robert Kiyosaki", "Non-fiction", R.drawable.book3))
//        books.add(Book("The Cruel Prince", "Holly Black", "Fantasy", R.drawable.book4))
//        books.add(Book("Atomic Habits", "James Clear", "Non-fiction", R.drawable.book5))
//        return books
//    }

    private lateinit var bookAdapter: CurrentlyReadingAdapter
    private lateinit var myDbHelper: MyDbHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize DB Helper
        myDbHelper = MyDbHelper(requireContext())
        Log.d("CRFragment", "onCreate called")

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

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve accountId from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val accountId = sharedPreferences.getLong("accountId", -1L)

        // Retrieve books and update adapter
        if (accountId != -1L) {
            val books = myDbHelper.getBooksByAccountId(accountId, "Currently Reading")
            Log.d("CurrentlyReadingFragment", "Books retrieved: ${books.size}")
            books.forEach { Log.d("CurrentlyReadingFragment", "Book: ${it.title}, Status: ${it.status}") }
            bookAdapter.updateBooks(books)
        } else {
            // Handle the case where accountId is not found
            Toast.makeText(context, "No account ID found", Toast.LENGTH_SHORT).show()
            Log.e("CRFragment", "Account ID not found in SharedPreferences")

        }

        //used to update fragment based on newly saved books
//        viewModel.books.observe(viewLifecycleOwner) { books ->
//            bookAdapter.updateBooks(books)
//        }

        // Observe books in ViewModel
//        viewModel.books.observe(viewLifecycleOwner) { books ->
//            val currentlyReadingBooks = books.filter { it.status == "Currently Reading" }
//            Log.d("CurrentlyReadingFragment", "Currently Reading Books observed: ${currentlyReadingBooks.size}")
//            currentlyReadingBooks.forEach { Log.d("CurrentlyReadingFragment", "Book: ${it.title}, Status: ${it.status}") }
//            bookAdapter.updateBooks(currentlyReadingBooks)
//        }

    }
//    fun updateBooks(books: List<Book>) {
//        bookAdapter.updateBooks(books)
//    }

}