package com.mobdeve.s12.fallarme.sophia.bookbuddy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookViewModel : ViewModel() {
    private val repository = BookRepository()
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    fun searchBooks(query: String) {
        repository.searchBooks(query) { bookList ->
            _books.postValue(bookList)
        }
    }

}
