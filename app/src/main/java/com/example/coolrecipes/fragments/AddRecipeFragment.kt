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

    private val TAG = "AddRecipeFragment"
    lateinit var buttonUpload: Button

    private val sdf = SimpleDateFormat("dd.MM.yyyy hh:mm")

    @SuppressLint("NewApi")
    private fun addRecipe() {
        sdf.timeZone = TimeZone.getTimeZone("GMT+2")
        val currentDate = sdf.format(Date())

        val recipe = hashMapOf(
            "date" to currentDate.toString(),
            "description" to recipeDescAdd.text.toString(),
            "ingredients" to recipeIngredientsAdd.text.toString(),
            "instructions" to recipeMainTextAdd.text.toString(),
            "name" to recipeNameAdd.text.toString().trim(),
            "photo" to null,
            "rating" to 0,
            "userName" to FirebaseAuth.getInstance().currentUser!!.displayName,
            "userID" to FirebaseAuth.getInstance().currentUser!!.uid
        )

        if (recipeDescAdd.text.isEmpty() || recipeIngredientsAdd.text.isEmpty() || recipeMainTextAdd.text.isEmpty() || recipeNameAdd.text.isEmpty())
        {
            Toast.makeText(activity,"Uzupełnij wszystkie pola!", Toast.LENGTH_SHORT).show()
            return
        }

        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("recipes")
            .add(recipe)
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

        buttonUpload = recipeButtonUpload
        buttonUpload.setOnClickListener{
            addRecipe()
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
