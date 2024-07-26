package com.mobdeve.s12.fallarme.sophia.bookbuddy.collection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobdeve.s12.fallarme.sophia.bookbuddy.Book
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R

//class AllAdapter(private val data: List<Book>) : RecyclerView.Adapter<AllAdapter.BookViewHolder>() {
//
//    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val bookTitle: TextView = itemView.findViewById(R.id.tvwBookTitle)
//        val bookAuthor: TextView = itemView.findViewById(R.id.tvwAuthor)
//        val bookCategory : TextView = itemView.findViewById(R.id.tvwCategory)
//        val bookCover: ImageView = itemView.findViewById(R.id.imgvwBookCover)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bookrecyclerview_layout, parent, false)
//        return BookViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
//        val book = data[position]
//        holder.bookTitle.text = book.title
//        holder.bookAuthor.text = book.author
//        holder.bookCategory .text = book.category
//        // Load image using an image loading library like Glide or Picasso
//        Glide.with(itemView.context).load(book.imageUrl).into(imgBookCover)
//    }
//
//    override fun getItemCount() = data.size
//}

class AllAdapter(private var books: List<com.mobdeve.s12.fallarme.sophia.bookbuddy.Book>) : RecyclerView.Adapter<AllAdapter.BookViewHolder>() {

//    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imgBookCover: ImageView = itemView.findViewById(R.id.imgvwBookCover)
//        val tvwCategory: TextView = itemView.findViewById(R.id.tvwCategory)
//        val tvwBookTitle: TextView = itemView.findViewById(R.id.tvwBookTitle)
//        val tvwAuthor: TextView = itemView.findViewById(R.id.tvwAuthor)
//
//        fun bind(book: Book) {
//            tvwBookTitle.text = book.title
//            tvwAuthor.text = book.author
//            tvwCategory.text = book.category
//            // Load image using an image loading library like Glide or Picasso
//            Glide.with(itemView.context).load(book.image).into(imgBookCover)
//        }
//    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookrecyclerview_layout, parent, false)
        return BookViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        var book = books[position]
        holder.bind(book)
//        val book = books[position]
//        holder.tvwBookTitle.text = book.title
//        holder.tvwAuthor.text = book.author
//        holder.tvwCategory.text = book.category
//        Glide.with(itemView.context).load(book.image).into(imgBookCover)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(book)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(book: Book)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = books.size

    fun updateBooks(newBook: List<Book>){
        this.books = newBook
        notifyDataSetChanged()

    }

    fun getCurrentBooks(): List<Book> {
        return books
    }

    class BookViewHolder(itemView: View, private val onItemClickListener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        val imgBookCover: ImageView = itemView.findViewById(R.id.imgvwBookCover)
        val tvwCategory: TextView = itemView.findViewById(R.id.tvwCategory)
        val tvwBookTitle: TextView = itemView.findViewById(R.id.tvwBookTitle)
        val tvwAuthor: TextView = itemView.findViewById(R.id.tvwAuthor)





        fun bind(book: Book) {


            tvwBookTitle.text = book.title
            tvwAuthor.text = book.author
            tvwCategory.text = book.category
            // Load image using an image loading library like Glide or Picasso
            Glide.with(itemView.context).load(book.image).into(imgBookCover)
        }
    }
}


