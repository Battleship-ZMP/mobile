package com.example.coolrecipes.fragments

import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coolrecipes.R
import com.example.coolrecipes.Recipe
import com.example.coolrecipes.RecipeAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private val recipeRef = db.collection("recipes")
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            setUpRecyclerView()
        }

    private fun setUpRecyclerView() {
        val query: Query = recipeRef.orderBy("rating", Query.Direction.DESCENDING)

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
            MainFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
