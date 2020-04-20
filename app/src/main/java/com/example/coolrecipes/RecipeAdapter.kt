package com.example.coolrecipes

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class RecipeAdapter(options: FirestoreRecyclerOptions<Recipe>) :
    FirestoreRecyclerAdapter<Recipe, RecipeAdapter.RecipeHolder>(options) {
    override fun onBindViewHolder(
        holder: RecipeHolder,
        position: Int,
        model: Recipe
    ) {
        holder.RecipeTitleMain.text = model.getName()
        holder.RecipeDescriptionMain.text = model.getDescription()
        holder.RecipeRatingMain.rating = model.getRating()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.recipe_item,
            parent, false
        )
        return RecipeHolder(v)
    }

    inner class RecipeHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var RecipeTitleMain: TextView = itemView.findViewById(R.id.recipe_title_main_list)
        var RecipeDescriptionMain: TextView = itemView.findViewById(R.id.recipe_description_main_list)
        var RecipeRatingMain: RatingBar = itemView.findViewById(R.id.recipe_rating_main_list)

    }

}