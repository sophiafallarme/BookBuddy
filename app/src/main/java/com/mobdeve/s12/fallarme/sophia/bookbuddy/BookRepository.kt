package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookRepository {
    private val apiService = BookApiService.create()

    fun searchBooks(query: String, callback: (List<Book>) -> Unit) {
        Log.d("BookRepository", "API call initiated with query: $query")
        apiService.searchBooks(query).enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful) {
                    Log.d("BookRepository", "API call successful. Data received: ${response.body()}")
                    callback(response.body() ?: emptyList())
                } else {
                    Log.e("BookRepository", "Response not successful: ${response.errorBody()?.string()}")
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.e("BookRepository", "API call failed", t)
                callback(emptyList())
            }
        })
    }
}

/*

class BookRepository {
    private val apiService = RetrofitInstance.api

    fun searchBooks(query: String, callback: (List<Book>) -> Unit) {
        apiService.searchBooks(query).enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.results ?: emptyList())
                } else {
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                callback(emptyList())
            }
        })
    }

    recyclerView = findViewById(R.id.recyclerview)
    recyclerAdapter = RecyclerAdapter(this)
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = recyclerAdapter


    val apiInterface = BookApiService.create().searchBooks()

    // fix the model

//apiInterface.enqueue( Callback<List<Movie>>())
    BookApiService.enqueue( object : Callback<List<Book>> {
        override fun onResponse(call: Call<List<Book>>?, response: Response<List<Book>>?) {

            if(response?.body() != null)
                recyclerAdapter.setMovieListItems(response.body()!!)
        }

        override fun onFailure(call: Call<List<Movie>>?, t: Throwable?) {

        }


    })
}

 */
