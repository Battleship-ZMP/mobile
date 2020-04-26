package com.example.coolrecipes


class Recipe {

    private var name: String = ""
    private var description: String = ""
    private var rating: Float = 0.0F
    private var userID: String = ""

    fun getName(): String {
        return name
    }

    fun getDescription(): String {
        return description
    }

    fun getRating(): Float {
        return rating
    }

    fun getUserID(): String {
        return userID
    }

}