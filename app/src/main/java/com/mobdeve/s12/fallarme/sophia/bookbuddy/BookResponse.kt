package com.mobdeve.s12.fallarme.sophia.bookbuddy


data class BookResponse(
    val results: List<Book>
)
/*
data class Book(
    val bookId: Long,
    val title: String,
    val author: String,
    val description: String,
    val cover: String,
    val status: String,
    val category: String,
    val review: String
)

 */
// separae this
/*
class Book {
    var bookId: Long = 0
        private set
    lateinit var title: String
        private set
    lateinit var author: String
        private set
    lateinit var description: String
        private set
    lateinit var cover: String
        private set
    // to add array of saved books
    // to add categories
    lateinit var status: Status
        private set
    lateinit var category: String
        private set
    lateinit var review: String
        private set

    constructor(bookId: Long, bookName: String, author: String, cover: String, description: String,
        status: Status, category: String, review: String) {
        this.bookId = bookId
        this.title = bookName
        this.author = author
        this.cover = cover
        this.description = description
        this.status = status
        this.category = category
        this.review = review
    }

    constructor(bookName: String, author: String, cover: String, description: String,
                status: Status, category: String, review: String) {
        this.title = bookName
        this.author = author
        this.cover = cover
        this.description = description
        this.status = status
        this.category = category
        this.review = review
    }

    override fun toString(): String {
        return "UserModel{" +
                "bookId=" + bookId +
                ", bookName='" + title + '\'' +
                ", author='" + author + '\'' +
                ", cover='" + cover + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", category='" + category + '\'' +
                ", review='" + review + '\'' +
                '}'
    }

}

 */
