package com.example.coolrecipes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.coolrecipes.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_view_recipe.*

class ViewRecipe : Fragment() {

    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        val RecipeID = bundle?.getString("ID")
        val recipeRef = RecipeID?.let { db.collection("recipes").document(it) }


        if (recipeRef != null) {
            recipeRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        recipeName.text = "${document.get("name")}"
                        recipeDateAdded.text = "${document.get("date")}"
                        recipeRating.rating = "${document.get("rating")}".toFloat()
                        recipeAddedBy.text = "${document.get("userName")}"
                        recipeDesc.text = "${document.get("description")}"
                        recipeIngredients.text = "${document.get("ingredients")}"
                        recipeTextMain.text = "${document.get("instructions")}"
                    }

                }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ViewRecipe().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
