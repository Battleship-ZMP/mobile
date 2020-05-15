package com.example.coolrecipes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import com.example.coolrecipes.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_view_recipe.*

class ViewRecipe : Fragment() {

    private var db = FirebaseFirestore.getInstance()
    lateinit var buttonFavorite: Button
    lateinit var buttonEdit: Button
    lateinit var buttonDelete: Button
    lateinit var recipeUser: TextView
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        val RecipeID = bundle?.getString("RecipeID")
        val recipeRef = RecipeID?.let { db.collection("recipes").document(it) }


        if (recipeRef != null) {
            recipeRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userID = document.get("userID")
                        var userName=""
                        val userRef = FirebaseFirestore.getInstance().collection("users").document(userID as String)
                        userRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    userName = document.get("userName") as String
                                    recipeUser.text = userName
                                }
                            }

                        recipeName.text = "${document.get("name")}"
                        recipeDateAdded.text = "${document.get("date")}"

                        val ratings = document.get("rating") as List<Int>
                        val averageRating = ratings.average().toFloat()
                        recipeRating.rating = averageRating

                        recipeDesc.text = "${document.get("description")}"
                        recipeIngredients.text = "${document.get("ingredients")}"
                        recipeTextMain.text = "${document.get("instructions")}"

                        val recipeImageSrc = "${document.get("photo")}"
                        if (recipeImageSrc != "" && recipeImageSrc != "null") {
                            Picasso.get().load(recipeImageSrc).into(recipeImage)
                        } else {
                            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/coolrecipes-f4e21.appspot.com/o/placeholders%2Frecipe_placeholder.png?alt=media&token=a23e9154-81c1-4d70-83a1-af110b2649c9").into(recipeImage)
                        }

                        buttonFavorite = favoriteButton
                        if (currentUserID == null) {
                            buttonFavorite.isVisible = false
                        }
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

                        recipeUser = recipeAddedBy
                        recipeUser.setOnClickListener{
                            val userBundle = Bundle()
                            userBundle.putString("UserID", userID)

                            val profileFragment = ProfileFragment()
                            profileFragment.arguments = userBundle

                            val fragmentManager: FragmentManager? = fragmentManager
                            if (fragmentManager != null) {
                                fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.container, profileFragment)
                                    .addToBackStack(ViewRecipe().toString())
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit()
                            }
                        }

                        buttonEdit = editButton
                        if (userID != currentUserID) {
                            buttonEdit.isVisible = false
                        } else {
                            buttonEdit.isVisible = true

                            buttonEdit.setOnClickListener {
                                val recipeEditBundle = Bundle()
                                recipeEditBundle.putString("RecipeEditID", RecipeID)

                                val recipeEdit = AddRecipeFragment()
                                recipeEdit.arguments = recipeEditBundle

                                val fragmentManager: FragmentManager? = fragmentManager
                                if (fragmentManager != null) {
                                    fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.container, recipeEdit)
                                        .addToBackStack(recipeEdit.toString())
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .commit()
                                }
                            }
                        }

                        buttonDelete = deleteButton
                        if (userID != currentUserID) {
                            buttonDelete.isVisible = false
                        } else {
                            buttonDelete.isVisible = true

                            buttonDelete.setOnClickListener {
                                db.collection("recipes").document(RecipeID).delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(activity,"Usunięto przepis!", Toast.LENGTH_SHORT).show()
                                        val fragmentManager: FragmentManager? = fragmentManager
                                        if (fragmentManager != null) {
                                            fragmentManager
                                                .beginTransaction()
                                                .replace(R.id.container, MainFragment())
                                                .addToBackStack(MainFragment().toString())
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                .commit()
                                            fragmentManager
                                                .popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(activity,"Błąd podczas usuwania przepisu!", Toast.LENGTH_SHORT).show()
                                    }
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