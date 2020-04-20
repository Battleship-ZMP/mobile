package com.example.coolrecipes


class Recipe {

    private var name: String = ""
    private var description: String = ""
    private var rating: Float = 0.0F

    fun Recipe() { //constructor
    }

    fun Recipe(name: String, description: String, rating: Float) {
        this.name = name
        this.description = description
        this.rating = rating
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

}