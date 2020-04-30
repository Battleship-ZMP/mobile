package com.example.coolrecipes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.coolrecipes.R
import com.example.coolrecipes.Recipe
import com.example.coolrecipes.RecipeAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private var db = FirebaseFirestore.getInstance()
    lateinit var updateProfile: Button
    lateinit var userRecipes: Button
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        val UserID = bundle?.getString("UserID")
        val userRef = UserID?.let { db.collection("users").document(it) }

        val userBundle = Bundle()
        userBundle.putString("UserID", UserID)

        if (userRef != null) {
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        userName.text = "${document.get("userName")}"
                        userBio.text = "${document.get("bio")}"
                    }
                }
        }

        updateProfile = updateProfileButton
        userRecipes = profileRecipesButton

        if(UserID != FirebaseAuth.getInstance().currentUser?.uid) {
            updateProfile.isVisible = false
        } else {
            userRecipes.isVisible = false
        }

        updateProfile.setOnClickListener{
            val updateProfile = ProfileUpdate()
            updateProfile.arguments = userBundle

            val fragmentManager: FragmentManager? = fragmentManager
            if (fragmentManager != null) {
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, updateProfile)
                    .addToBackStack(updateProfile.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }

        userRecipes.setOnClickListener{
            val viewUserRecipes = UserRecipesFragment()
            viewUserRecipes.arguments = userBundle

            val fragmentManager: FragmentManager? = fragmentManager
            if (fragmentManager != null) {
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, viewUserRecipes)
                    .addToBackStack(viewUserRecipes.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
