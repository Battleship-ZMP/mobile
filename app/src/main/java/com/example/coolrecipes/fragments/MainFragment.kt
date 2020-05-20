package com.example.coolrecipes.fragments

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coolrecipes.R
import com.example.coolrecipes.Recipe
import com.example.coolrecipes.RecipeAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_view_recipe.*


class MainFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private var recipeRef = db.collection("recipes")
    private lateinit var adapter: RecipeAdapter

    lateinit var sortByDateButton: RadioButton
    lateinit var sortByNameButton: RadioButton
    lateinit var sortByRatingButton: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortByDateButton = sortByDate
        sortByNameButton = sortByName
        sortByRatingButton = sortByRating

        sortByDateButton.setOnClickListener {
            val sortMode = "byDate"
            val sortModeBundle = Bundle()
            sortModeBundle.putString("SortMode", sortMode)

            val mainFragment = MainFragment()
            mainFragment.arguments = sortModeBundle

            val fragmentManager: FragmentManager? = fragmentManager
            if (fragmentManager != null) {
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .addToBackStack(mainFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }

        sortByNameButton.setOnClickListener {
            val sortMode = "byName"
            val sortModeBundle = Bundle()
            sortModeBundle.putString("SortMode", sortMode)

            val mainFragment = MainFragment()
            mainFragment.arguments = sortModeBundle

            val fragmentManager: FragmentManager? = fragmentManager
            if (fragmentManager != null) {
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .addToBackStack(mainFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }

        sortByRatingButton.setOnClickListener {
            val sortMode = "byRating"
            val sortModeBundle = Bundle()
            sortModeBundle.putString("SortMode", sortMode)

            val mainFragment = MainFragment()
            mainFragment.arguments = sortModeBundle

            val fragmentManager: FragmentManager? = fragmentManager
            if (fragmentManager != null) {
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .addToBackStack(mainFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }

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
        val bundle = this.arguments
        val SortMode = bundle?.getString("SortMode")

        if (SortMode == "byDate") {
            sortByRatingButton.isChecked = false; sortByNameButton.isChecked = false; sortByDateButton.isChecked = true
            val query: Query = recipeRef.orderBy("date", Query.Direction.DESCENDING)

            val options = FirestoreRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe::class.java)
                .build()

            adapter = RecipeAdapter(options)

            recyclerview_main_list.setHasFixedSize(true)
            recyclerview_main_list.layoutManager = LinearLayoutManager(this.context)
            recyclerview_main_list.adapter = adapter
        }

        if (SortMode == "byName") {
            sortByRatingButton.isChecked = false; sortByDateButton.isChecked = false; sortByNameButton.isChecked = true
            val query: Query = recipeRef.orderBy("name", Query.Direction.ASCENDING)

            val options = FirestoreRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe::class.java)
                .build()

            adapter = RecipeAdapter(options)

            recyclerview_main_list.setHasFixedSize(true)
            recyclerview_main_list.layoutManager = LinearLayoutManager(this.context)
            recyclerview_main_list.adapter = adapter
        }

        if (SortMode == "byRating" || SortMode == null) {
            sortByNameButton.isChecked = false; sortByDateButton.isChecked = false; sortByRatingButton.isChecked = true
            val query: Query = recipeRef.orderBy("averageRating", Query.Direction.DESCENDING)

            val options = FirestoreRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe::class.java)
                .build()

            adapter = RecipeAdapter(options)

            recyclerview_main_list.setHasFixedSize(true)
            recyclerview_main_list.layoutManager = LinearLayoutManager(this.context)
            recyclerview_main_list.adapter = adapter
        }
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
            MainFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}











