package com.example.coolrecipes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class CookBookAddedFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cook_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        adapter.setOnItemClickListener(object : RecipeAdapter.OnItemClickListener {
            override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int) {
                val recipe = documentSnapshot.toObject(Recipe::class.java)
                val recipeid = documentSnapshot.id
                val path = documentSnapshot.reference.path

                val recipeBundle = Bundle()
                recipeBundle.putString("RecipeID", recipeid)

                val viewRecipe = ViewRecipe()
                viewRecipe.arguments = recipeBundle

                val fragmentManager: FragmentManager? = fragmentManager
                if (fragmentManager != null) {
                    fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, viewRecipe)
                        .addToBackStack(ViewRecipe().toString())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }
        })
    }

    private fun setUpRecyclerView() {
        val recipeRef = db.collection("recipes")
        val query: Query = recipeRef
            .orderBy("rating", Query.Direction.DESCENDING)
            .whereEqualTo("userID", FirebaseAuth.getInstance().currentUser?.uid)

        val options = FirestoreRecyclerOptions.Builder<Recipe>()
            .setQuery(query, Recipe::class.java)
            .build()

        adapter = RecipeAdapter(options)

        recyclerview_main_list.setHasFixedSize(true)
        recyclerview_main_list.layoutManager = LinearLayoutManager(this.context)
        recyclerview_main_list.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CookBookAddedFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}