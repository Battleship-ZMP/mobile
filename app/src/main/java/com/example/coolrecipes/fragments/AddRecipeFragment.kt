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
import java.time.LocalDateTime
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [CookBookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddRecipeFragment : Fragment() {

    private val TAG = "AddRecipeFragment"
    lateinit var buttonUpload: Button

    val sdf = SimpleDateFormat("dd.MM.yyyy hh:mm")
    val currentDate = sdf.format(Date())

    @SuppressLint("NewApi")
    private fun addRecipe() {
        val recipe = hashMapOf(
            "date" to currentDate.toString(),
            "description" to recipeDescAdd.text.toString(),
            "ingredients" to recipeIngredientsAdd.text.toString(),
            "instructions" to recipeMainTextAdd.text.toString(),
            "name" to recipeNameAdd.text.toString().trim(),
            "photo" to null,
            "rating" to 0,
            "userName" to FirebaseAuth.getInstance().currentUser!!.displayName
        )

        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("recipes")
            .add(recipe)
            .addOnSuccessListener {
                Toast.makeText(activity,"Dodano przepis!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity,"Błąd podczas dodawania przepisu!", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CookBookFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            AddRecipeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
