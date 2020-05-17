package com.example.coolrecipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nav_header.view.*
import java.text.DateFormat.getDateTimeInstance
import java.text.SimpleDateFormat
import java.util.*


class RecipeAdapter(options: FirestoreRecyclerOptions<Recipe>) :
    FirestoreRecyclerAdapter<Recipe, RecipeAdapter.RecipeHolder>(options) {

    private var listener: OnItemClickListener? = null

    override fun onBindViewHolder(
        holder: RecipeHolder,
        position: Int,
        model: Recipe
    ) {
        val timestamp = model.getDate()
        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("GMT+2")
        val netDate = Date(milliseconds)
        val date = sdf.format(netDate).toString()

        holder.RecipeTitleMain.text = model.getName()
        holder.RecipeDescriptionMain.text = model.getDescription()
        holder.RecipeRatingMain.rating = model.getRating().average().toFloat()
        holder.RecipeDateMain.text = date

        val imageURL = model.getPhoto()
        if (!imageURL.isNullOrEmpty()) {
            Picasso.get().load(imageURL).into(holder.RecipeImageMain)
        } else {
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/coolrecipes-f4e21.appspot.com/o/placeholders%2Frecipe_placeholder.png?alt=media&token=a23e9154-81c1-4d70-83a1-af110b2649c9").into(holder.RecipeImageMain)
        }

        val userRef = FirebaseFirestore.getInstance().collection("users").document(model.getUserID())

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userName = document.get("userName") as String
                    holder.RecipeUserNameMain.text = userName
                }
            }
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
        var RecipeUserNameMain: TextView = itemView.findViewById(R.id.recipe_addedby_main_list)
        var RecipeDateMain: TextView = itemView.findViewById(R.id.recipe_date_main_list)
        var RecipeImageMain: ImageView = itemView.findViewById(R.id.recipe_image_main_list)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener!!.onItemClick(snapshots.getSnapshot(position), position)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

}



