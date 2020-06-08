package com.example.coolrecipes.fragments

import android.media.Rating
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import com.example.coolrecipes.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_view_recipe.*
import java.lang.reflect.Array.get
import java.text.SimpleDateFormat
import java.util.*

class ViewRecipe : Fragment() {

    private var db = FirebaseFirestore.getInstance()
    lateinit var buttonFavorite: Button
    lateinit var buttonEdit: Button
    lateinit var buttonDelete: Button
    lateinit var recipeUser: TextView
    lateinit var recipeRatingBar: RatingBar
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
                        val userRef = FirebaseFirestore.getInstance().collection("users").document(userID as String)
                        userRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    val userName = document.get("userName")
                                    if (userName != null) {
                                        recipeUser.text = userName as String
                                    } else {
                                        recipeUser.text = "PROFIL USUNIĘTY"
                                    }
                                }
                            }

                        recipeName.text = "${document.get("name")}"
                        val timestamp = document.get("date") as Timestamp
                        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
                        sdf.timeZone = TimeZone.getTimeZone("GMT+2")
                        val netDate = Date(milliseconds)
                        val date = sdf.format(netDate).toString()
                        recipeDateAdded.text = date

                        if (document.get("averageRating") is Long){
                            recipeRating.rating = (document.get("averageRating") as Long).toFloat()
                        } else {
                            if (document.get("averageRating") == null) {
                                recipeRating.rating = 0F
                            } else {
                                recipeRating.rating = (document.get("averageRating") as Double).toFloat()
                            }
                        }

                        recipeRatingBar = recipeRating
                        recipeRatingBar.setOnRatingBarChangeListener { _, rating, _ ->
                            val ratingInfo = hashMapOf(
                                "value" to rating,
                                "userID" to currentUserID
                            )

                            recipeRef.get()
                                .addOnSuccessListener { document ->
                                    val ratings = document["rating"] as List<Map<String, Any>>?
                                    if (ratings != null) {
                                        for (item in ratings) {
                                            if (item.getValue("userID") == currentUserID) {
                                                recipeRef.update(
                                                    "rating",
                                                    FieldValue.arrayRemove(item)
                                                )
                                            }
                                        }
                                    }
                                    if (currentUserID == null) {
                                        Toast.makeText(activity,"Zaloguj się, aby ocenić ten przepis!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        recipeRef.update("rating", FieldValue.arrayUnion(ratingInfo))
                                            .addOnSuccessListener {
                                                recipeRef.get()
                                                    .addOnSuccessListener { document ->
                                                        val ratings = document["rating"] as List<Map<String, Any>>?
                                                        val ratingValues: MutableList<Float> = arrayListOf()
                                                        if (ratings != null) {
                                                            for (item in ratings) {
                                                                if (item.getValue("value") is Long) {
                                                                    ratingValues.add((item.getValue("value") as Long).toFloat())
                                                                } else {
                                                                    ratingValues.add((item.getValue("value") as Double).toFloat())
                                                                }
                                                            }

                                                            val newAverage = ratingValues.average()
                                                            recipeRef.update("averageRating", newAverage)
                                                            Toast.makeText(activity,"Dodano ocenę!", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                            }
                                    }
                                }
                        }

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
                            db.collection("users").document(userID).get()
                                .addOnSuccessListener { document ->
                                    val username = document.get("userName")
                                    if (username != null) {
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
                                    } else {
                                        Toast.makeText(activity,"Profil nie istnieje!", Toast.LENGTH_SHORT).show()
                                    }
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

    internal class Rating(var userID: String, var value: Int)

    companion object {
        @JvmStatic
        fun newInstance() =
            ViewRecipe().apply {
                arguments = Bundle().apply {

                }
            }
    }
}