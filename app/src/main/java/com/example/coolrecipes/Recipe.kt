package com.example.coolrecipes

import com.google.firebase.Timestamp


class Recipe {

    private var name: String = ""
    private var description: String = ""
    private var rating: List<Int> = emptyList()
    private var averageRating: Float = 0.0F
    private var userID: String = ""
    private lateinit var date: Timestamp
    private var photo: String = ""

    fun getName(): String {
        return name
    }

    fun getDescription(): String {
        return description
    }

    fun getUserID(): String {
        return userID
    }

    fun getDate(): Timestamp {
        return date
    }

    fun getPhoto(): String {
        return photo
    }

    fun getAverageRating(): Float {
        return averageRating
    }

}