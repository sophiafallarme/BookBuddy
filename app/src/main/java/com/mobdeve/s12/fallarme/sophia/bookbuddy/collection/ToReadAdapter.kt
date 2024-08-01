package com.mobdeve.s12.fallarme.sophia.bookbuddy.collection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobdeve.s12.fallarme.sophia.bookbuddy.Book
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R

class ToReadAdapter(private var books: List<Book>) :
    RecyclerView.Adapter<ToReadAdapter.BookViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(book: Book)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBookCover: ImageView = itemView.findViewById(R.id.imgvwBookCover)
        val tvwCategory: TextView = itemView.findViewById(R.id.tvwCategory)
        val tvwBookTitle: TextView = itemView.findViewById(R.id.tvwBookTitle)
        val tvwAuthor: TextView = itemView.findViewById(R.id.tvwAuthor)
        val btnMoreDetails: Button = itemView.findViewById(R.id.btnMoreDetails)

        fun bind(book: Book) {
            tvwBookTitle.text = book.title
            tvwAuthor.text = book.author
            tvwCategory.text = book.category
            // Load image using an image loading library like Glide or Picasso
            Glide.with(itemView.context).load(book.image).into(imgBookCover)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookrecyclerview_layout, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        var book = books[position]
        holder.bind(book)
        holder.btnMoreDetails.setOnClickListener {
            onItemClickListener?.onItemClick(book)
        }
    }

    override fun getItemCount(): Int = books.size

    fun updateBooks(newBook: List<Book>){
        this.books = newBook
        notifyDataSetChanged()

    }

}