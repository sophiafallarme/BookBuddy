package com.mobdeve.s12.fallarme.sophia.bookbuddy.collection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R
import com.mobdeve.s12.fallarme.sophia.bookbuddy.databinding.FragmentCurrentlyReadingBinding


class CurrentlyReadingFragment : Fragment() {

    private var _binding: FragmentCurrentlyReadingBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CurrentlyReadingAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_currently_reading, container, false)
        _binding = FragmentCurrentlyReadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        layoutManager = GridLayoutManager(context, 2)
        adapter = CurrentlyReadingAdapter(getSampleData())

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun getSampleData(): ArrayList<Book> {
//        return listOf("Rich Dad, Poor Dad", "The Alchemist", "Negotiation 101", "The Cruel Prince", "Item 5")
        val books = ArrayList<Book>()
        books.add(Book("I want a better catastrophe", "Andre Boyd", "Non-fiction", R.drawable.book1))
        books.add(Book("The Midnight Library", "Matt Haig", "Fiction", R.drawable.book2))
        books.add(Book("Rich Dad, Poor Dad", "Robert Kiyosaki", "Non-fiction", R.drawable.book3))
        books.add(Book("The Cruel Prince", "Holly Black", "Fantasy", R.drawable.book4))
        books.add(Book("Atomic Habits", "James Clear", "Non-fiction", R.drawable.book5))
        return books
    }

}