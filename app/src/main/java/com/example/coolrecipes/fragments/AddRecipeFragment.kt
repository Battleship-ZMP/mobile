package com.example.coolrecipes.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import com.example.coolrecipes.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_add_recipe.*
import java.text.SimpleDateFormat
import java.util.*

class AddRecipeFragment : Fragment() {

    private var db = FirebaseFirestore.getInstance()
    lateinit var buttonUpload: Button

    private val sdf = SimpleDateFormat("dd.MM.yyyy hh:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        val recipeEditID = bundle?.getString("RecipeEditID")
        val recipeRef = recipeEditID?.let { db.collection("recipes").document(it) }

        if (recipeRef != null) {
            recipeRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        recipeNameAdd.setText("${document.get("name")}")
                        recipeDescAdd.setText("${document.get("description")}")
                        recipeIngredientsAdd.setText("${document.get("ingredients")}")
                        recipeMainTextAdd.setText("${document.get("instructions")}")
                    }
                }
        }

        buttonUpload = recipeButtonUpload
        buttonUpload.setOnClickListener{
            addRecipe()
        }
    }

    @SuppressLint("NewApi")
    private fun addRecipe() {
        sdf.timeZone = TimeZone.getTimeZone("GMT+2")
        val currentDate = sdf.format(Date())
        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        val bundle = this.arguments
        val recipeEditID = bundle?.getString("RecipeEditID")
        val recipeRef = recipeEditID?.let { db.collection("recipes").document(it) }

        val newRecipe = hashMapOf(
            "date" to currentDate.toString(),
            "description" to recipeDescAdd.text.toString(),
            "ingredients" to recipeIngredientsAdd.text.toString(),
            "instructions" to recipeMainTextAdd.text.toString(),
            "name" to recipeNameAdd.text.toString().trim(),
            "photo" to null,
            "rating" to arrayListOf<Int>(),
            "userID" to userID,
            "savedByUsers" to arrayListOf<String>()
        )

        if (recipeDescAdd.text.isEmpty() || recipeIngredientsAdd.text.isEmpty() || recipeMainTextAdd.text.isEmpty() || recipeNameAdd.text.isEmpty())
        {
            Toast.makeText(activity,"Uzupełnij wszystkie pola!", Toast.LENGTH_SHORT).show()
            return
        }

        if (recipeRef != null) {
            recipeRef.get()
                .addOnSuccessListener { document ->
                    val photo = document.get("photo")
                    val ratings = document.get("rating")
                    val savedByUsers = document.get("savedByUsers")

                    val updateRecipe = hashMapOf(
                        "date" to currentDate.toString(),
                        "description" to recipeDescAdd.text.toString(),
                        "ingredients" to recipeIngredientsAdd.text.toString(),
                        "instructions" to recipeMainTextAdd.text.toString(),
                        "name" to recipeNameAdd.text.toString().trim(),
                        "photo" to photo,
                        "rating" to ratings,
                        "userID" to userID,
                        "savedByUsers" to savedByUsers
                    )

                    db.collection("recipes").document(recipeEditID)
                        .set(updateRecipe)
                        .addOnSuccessListener {
                            Toast.makeText(activity,"Zaktualizowano przepis!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(activity,"Błąd podczas aktualizacji przepisu!", Toast.LENGTH_SHORT).show()
                        }
                }
        } else {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("recipes")
                .add(newRecipe)
                .addOnSuccessListener {
                    Toast.makeText(activity,"Dodano przepis!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(activity,"Błąd podczas dodawania przepisu!", Toast.LENGTH_SHORT).show()
                }

            recipeNameAdd.setText("")
            recipeDescAdd.setText("")
            recipeIngredientsAdd.setText("")
            recipeMainTextAdd.setText("")
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddRecipeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
