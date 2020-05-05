package com.example.coolrecipes


class Recipe {

    private var name: String = ""
    private var description: String = ""
    private var rating: List<Int> = emptyList()
    private var userID: String = ""
    private var date: String = ""

    fun getName(): String {
        return name
    }

    fun getDescription(): String {
        return description
    }

    fun getRating(): List<Int> {
        return rating
    }

    fun getUserID(): String {
        return userID
    }

    fun getDate(): String {
        return date
    }

}