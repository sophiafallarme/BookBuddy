package com.mobdeve.s12.fallarme.sophia.bookbuddy

/*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


 */


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface BookApiService {
    @Headers(
        "x-rapidapi-host: book-info-hub.p.rapidapi.com",
        "x-rapidapi-key: f202c4e367msh2e988a6647e6844p16235bjsn3369ff6e2f6b"
    )
    @GET("search.php")
    fun searchBooks(
        @Query("query") query: String
    ): Call<List<Book>>

    companion object {
        fun create(): BookApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://book-info-hub.p.rapidapi.com/")
                .build()
            return retrofit.create(BookApiService::class.java)
        }
    }
}


/*
interface BookApiService {

    /*
    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://book-info-hub.p.rapidapi.com/search.php?query=atomic")
        .get()
        .addHeader("x-rapidapi-key", "f202c4e367msh2e988a6647e6844p16235bjsn3369ff6e2f6b")
        .addHeader("x-rapidapi-host", "book-info-hub.p.rapidapi.com")
        .build()

    val response = client.newCall(request).execute()

    @GET("search.php")
    fun searchBooks(
        @Query("query") query: String,
        @Header("x-rapidapi-host") host: String = "book-info-hub.p.rapidapi.com",
        @Header("x-rapidapi-key") apiKey: String = "f202c4e367msh2e988a6647e6844p16235bjsn3369ff6e2f6b"
    ): Call<BookResponse>

     */
    companion object {

        var BASE_URL = "https://book-info-hub.p.rapidapi.com/search.php?query=atomic"

       /* @GET("volley_array.json")
        fun getMovies() : Call<List<Movie>>

        */

        @Headers("x-rapidapi-host: book-info-hub.p.rapidapi.com", "x-rapidapi-key: f202c4e367msh2e988a6647e6844p16235bjsn3369ff6e2f6b")
        @GET("search.php")


        fun searchBooks(
         //   @Query("query") query: String,

        ): Call <List<Book>> {

            return // TODO: Add return // find the tab where getbooks is most

        }

        val apiInterface = ApiInterface.create().getMovies()


        fun create() : BookApiService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(BookApiService::class.java)

        }
    }

}

// client to make the call wala pa

/*
@POST("user/classes")
fun addToPlaylist(
    @Header("Content-Type") content_type: String?,
    @Body req: RequestModel?
): Call<ResponseModel?>?

@Headers("user-key: 9900a9720d31dfd5fdb4352700c")
@GET("api/v2.1/search")

 */
 */