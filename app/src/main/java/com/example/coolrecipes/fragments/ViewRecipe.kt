package com.example.coolrecipes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import com.example.coolrecipes.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_view_recipe.*

class ViewRecipe : Fragment() {

    private var db = FirebaseFirestore.getInstance()
    lateinit var buttonFavorite: Button
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid

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
                        val userRef = document.get("userRef") as DocumentReference
                        var userName=""
                        userRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    userName = document.get("userName") as String
                                    recipeAddedBy.text = userName
                                }
                            }

                        recipeName.text = "${document.get("name")}"
                        recipeDateAdded.text = "${document.get("date")}"
                        recipeRating.rating = "${document.get("rating")}".toFloat()
                        recipeDesc.text = "${document.get("description")}"
                        recipeIngredients.text = "${document.get("ingredients")}"
                        recipeTextMain.text = "${document.get("instructions")}"

                        buttonFavorite = favoriteButton
                        var usersArray = document.get("savedByUsers") as List<*>
                        var isFavorited = true
                        if(usersArray.contains(currentUserID))
                        {
                            buttonFavorite.text = "Usuń z ulubionych"
                        } else {
                            buttonFavorite.text = "Dodaj do ulubionych"
                            isFavorited = false
                        }

                        buttonFavorite.setOnClickListener{
                            if(isFavorited) {
                                recipeRef.update("savedByUsers", FieldValue.arrayRemove(currentUserID))
                                Toast.makeText(activity,"Usunięto z ulubionych!", Toast.LENGTH_SHORT).show()
                                buttonFavorite.text = "Dodaj do ulubionych"
                                isFavorited = false
                            } else {
                                recipeRef.update("savedByUsers", FieldValue.arrayUnion(currentUserID))
                                Toast.makeText(activity,"Dodano do ulubionych!", Toast.LENGTH_SHORT).show()
                                buttonFavorite.text = "Usuń z ulubionych"
                                isFavorited = true
                            }
                        }
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