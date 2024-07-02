package com.mobdeve.s12.fallarme.sophia.bookbuddy

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
    // to add array of saved books

    constructor(firstName: String, lastName: String, username: String, password: String, id: Long) {
        this.lastName = lastName
        this.firstName = firstName
        this. username = username
        this.password = password
        this.id = id
    }

    constructor(firstName: String, lastName: String, username: String, password: String) {
        this.lastName = lastName
        this.firstName = firstName
        this. username = username
        this.password = password

    }

    override fun toString(): String {
        return "UserModel{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}'
    }

}