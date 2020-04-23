package com.example.coolrecipes


class Recipe {

    private var name: String = ""
    private var description: String = ""
    private var rating: Float = 0.0F
    private var userName: String = ""

    fun Recipe() { //constructor
    }

    fun Recipe(name: String, description: String, rating: Float, userName: String) {
        this.name = name
        this.description = description
        this.rating = rating
        this.userName = userName
    }

    fun getName(): String {
        return name
    }

    fun getDescription(): String {
        return description
    }

    fun getRating(): Float {
        return rating
    }

    fun getUserName(): String {
        return userName
    }

}