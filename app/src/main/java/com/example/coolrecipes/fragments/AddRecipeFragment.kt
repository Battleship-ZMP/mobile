package com.example.coolrecipes.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

import com.example.coolrecipes.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_recipe.*
import java.text.SimpleDateFormat
import java.util.*

class AddRecipeFragment : Fragment() {

    private val PICK_IMAGE_REQUEST: Int = 5000
    private var imageUri: Uri? = null

    private var db = FirebaseFirestore.getInstance()
    lateinit var buttonUpload: Button
    lateinit var pickImageButton: ImageButton

    private val sdf = SimpleDateFormat("dd.MM.yyyy hh:mm")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickImageButton = imageButtonAdd
        pickImageButton.setOnClickListener {
            chooseFile()
        }

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

    private fun chooseFile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null
        ) {
            imageUri = data.data
            Picasso.get().load(imageUri).into(pickImageButton)
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
