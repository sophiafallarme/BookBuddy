package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookAdapter(
    private val onItemSaveClickListener: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private val books = mutableListOf<Book>()
    private var onItemClickListener: ((Book) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun submitList(bookList: List<Book>) {
        books.clear()
        books.addAll(bookList)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Book) -> Unit) {
        onItemClickListener = listener
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        private val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
        private val coverImageView: ImageView = itemView.findViewById(R.id.coverImageView)
        private val saveButton: LinearLayout = itemView.findViewById(R.id.saveBookBtn)

        fun bind(book: Book) {
            titleTextView.text = book.title
            authorTextView.text = book.author
            ratingTextView.text = book.rating
            Glide.with(itemView.context)
                .load(book.image)
                .into(coverImageView)

            saveButton.setOnClickListener {
                onItemSaveClickListener(book)
            }
        }
    }
}

/*
class BookAdapter(private val onItemSaveClickListener: (Book) -> Unit) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private val books = mutableListOf<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view, onItemSaveClickListener)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun submitList(bookList: List<Book>) {
        books.clear()
        books.addAll(bookList)
        notifyDataSetChanged()
    }

    class BookViewHolder(itemView: View, private val onItemSaveClickListener: (Book) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        private val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
        private val coverImageView: ImageView = itemView.findViewById(R.id.coverImageView)
        private val saveButton: LinearLayout = itemView.findViewById(R.id.saveBookBtn)

        fun bind(book: Book) {
            titleTextView.text = book.title
            authorTextView.text = book.author
            ratingTextView.text = book.rating
            Glide.with(itemView.context)
                .load(book.image)
                .into(coverImageView)

            saveButton.setOnClickListener {
                onItemSaveClickListener(book)
            }
        }
    }
}


/*
class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private val books = mutableListOf<Book>()
    private var onItemClickListener: ((Book) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun submitList(bookList: List<Book>) {
        books.clear()
        books.addAll(bookList)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Book) -> Unit) {
        onItemClickListener = listener
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        private val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
        private val coverImageView: ImageView = itemView.findViewById(R.id.coverImageView)

        fun bind(book: Book) {
            titleTextView.text = book.title
            authorTextView.text = book.author
            ratingTextView.text = book.rating
            Glide.with(itemView.context)
                .load(book.image)
                .into(coverImageView)

            itemView.setOnClickListener {
                onItemClickListener?.invoke(book)
            }
        }
    }
}

 */

 */



/*package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private val books = mutableListOf<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun submitList(bookList: List<Book>) {
        books.clear()
        books.addAll(bookList)
        notifyDataSetChanged()
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        fun bind(book: Book) {
            titleTextView.text = book.title
            authorTextView.text = book.author
            descriptionTextView.text = book.description
        }
    }
}

 */
