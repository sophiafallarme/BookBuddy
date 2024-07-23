package com.mobdeve.s12.fallarme.sophia.bookbuddy
import com.google.gson.annotations.SerializedName

/*
class Account {
    var id: Long = 0
        private set
    lateinit var firstName: String
        private set
    lateinit var lastName: String
        private set
    lateinit var username: String
        private set
    lateinit var password: String
        private set
    var savedBookIds: ArrayList<Long> = ArrayList()
        private set
    var categories: ArrayList<String> = ArrayList()
        private set

    constructor(firstName: String, lastName: String, username: String, password: String) {
        this.firstName = firstName
        this.lastName = lastName
        this.username = username
        this.password = password
    }

    constructor(firstName: String, lastName: String, username: String, password: String, id: Long, savedBookIds: ArrayList<Long>, categories: ArrayList<String>) {
        this.firstName = firstName
        this.lastName = lastName
        this.username = username
        this.password = password
        this.id = id
        this.savedBookIds = savedBookIds
        this.categories = categories
    }

    fun addBook(bookId: Long) {
        savedBookIds.add(bookId)
    }

    fun addCategory(categoryName: String) {
        if (!categories.contains(categoryName)) {
            categories.add(categoryName)
        }
    }

    override fun toString(): String {
        return "Account(id=$id, firstName='$firstName', lastName='$lastName', username='$username', password='$password', savedBookIds=${savedBookIds.joinToString(",")}, categories=${categories.joinToString(",")})"
    }
}

 */
/*

class Account {
    var id: Long = 0
        private set
    lateinit var firstName: String
        private set
    lateinit var lastName: String
        private set
    lateinit var username: String
        private set
    lateinit var password: String
        private set
    var savedBooks: ArrayList<String> = ArrayList()
        private set
    var category: ArrayList<String> = ArrayList()
        private set

    constructor(firstName: String, lastName: String, username: String, password: String) {
        this.firstName = firstName
        this.lastName = lastName
        this.username = username
        this.password = password
    }

    constructor(firstName: String, lastName: String, username: String, password: String, id: Long, savedBooks: ArrayList<String>, category: ArrayList<String>) {
        this.firstName = firstName
        this.lastName = lastName
        this.username = username
        this.password = password
        this.id = id
        this.savedBooks = savedBooks
        this.category = category
    }

    fun addBook(bookId: String) {
        savedBooks.add(bookId)
    }

    fun addCategory(categoryName: String) {
        category.add(categoryName)
    }

    override fun toString(): String {
        return "Account(id=$id, firstName='$firstName', lastName='$lastName', username='$username', password='$password', savedBooks=${savedBooks.joinToString(",")}, category=${category.joinToString(",")})"
    }
}



/*
class Account {

    var id: Long = 0
        private set
    lateinit var firstName: String
        private set
    lateinit var lastName: String
        private set
    lateinit var username: String
        private set
    lateinit var password: String
        private set
   // lateinit var savedBooks: ArrayList<String>
     //   private set
   // lateinit var category: ArrayList<String>
       // private set
    var savedBooks: ArrayList<String> = ArrayList()
         private set
     var category: ArrayList<String> = ArrayList()
        private set

    constructor(firstName: String, lastName: String, username: String, password: String, id: Long, savedBooks: ArrayList<String>, category: ArrayList<String>) {
        this.lastName = lastName
        this.firstName = firstName
        this.username = username
        this.password = password
        this.id = id
        this.savedBooks = savedBooks
        this.category = category
    }

    constructor(firstName: String, lastName: String, username: String, password: String, savedBooks: ArrayList<String>, category: ArrayList<String>) {
        this.lastName = lastName
        this.firstName = firstName
        this.username = username
        this.password = password
        this.savedBooks = savedBooks
        this.category = category
    }

    override fun toString(): String {
        return "UserModel{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", savedBooks='" + savedBooks.joinToString(",") + '\'' +
                ", category='" + category.joinToString(",") + '\'' +
                '}'
    }
}


 */
 */

data class Account(
    var firstName: String,
    var lastName: String,
    var username: String,
    var password: String,
    var id: Long = 0,
    var savedBooks: MutableList<String> = mutableListOf(),
    var category: MutableList<String> = mutableListOf()
) {
    fun addBook(bookId: String) {
        savedBooks.add(bookId)
    }

    fun addCategory(categoryName: String) {
        if (!category.contains(categoryName)) {
            category.add(categoryName)
        }
    }


    override fun toString(): String {
        return "Account(id=$id, firstName='$firstName', lastName='$lastName', username='$username', password='$password', savedBookIds=${savedBooks.joinToString(",")}, categories=${category.joinToString(",")})"
    }
}
