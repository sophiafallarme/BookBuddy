package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookViewModel : ViewModel() {
    private val repository = BookRepository()
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books


    fun searchBooks(query: String) {
        repository.searchBooks(query) { bookList ->
            // Log the size of the book list being loaded
            Log.d("BookViewModel", "Books loaded: ${bookList.size}")
            _books.postValue(bookList)
        }
    }

//    init {
//        // Load books initially with an empty query to fetch all
//        loadBooks("")
//    }
//
//    fun loadBooks(query: String) {
//        repository.searchBooks(query) { bookList ->
//            // Log the size of the book list being loaded
//            Log.d("BookViewModel", "Books loaded: ${bookList.size}")
//            _books.postValue(bookList)
//        }
//    }


}
