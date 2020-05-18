package com.example.coolrecipes.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.Intent.getIntent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.coolrecipes.MainActivity

import com.example.coolrecipes.R
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile_update.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*

class ProfileUpdate : Fragment() {

    private val PICK_IMAGE_REQUEST: Int = 5001
    private var imageUri: Uri? = null

    private var db = FirebaseFirestore.getInstance()
    private var storageRef: StorageReference? = null
    private var uploadTask: StorageTask<*>? = null

    lateinit var updateImage: ImageButton
    lateinit var updateProfile: Button
    lateinit var deleteProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storageRef = FirebaseStorage.getInstance().getReference("avatars");

        val bundle = this.arguments
        val UserID = bundle?.getString("UserID")
        val userRef = UserID?.let { db.collection("users").document(it) }

        if (userRef != null) {
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        userNameUpdate.setText("${document.get("userName")}")
                        userBioUpdate.setText("${document.get("bio")}")
                        val profile_pic = "${document.get("photo")}"
                        if (profile_pic.isNotEmpty() && profile_pic != "null") {
                            Picasso.get().load(profile_pic).into(updateImage)
                        }
                    }
                }
        }

        updateImage = userImageUpdate
        updateProfile = userProfileUpdateButton
        deleteProfile = userProfileDeleteButton

        updateImage.setOnClickListener {
            chooseFile()
        }

        updateProfile.setOnClickListener {
            if (userPasswordUpdateOld.text.toString().isEmpty() && userPasswordUpdateNew.text.toString().isEmpty() && userPasswordUpdateConfirmNew.text.toString().isEmpty()) {
                updateProfileWithoutPassword()
                Toast.makeText(activity,"Zaktualizowano profil!", Toast.LENGTH_SHORT).show()
            } else {
                if (userPasswordUpdateOld.text.toString().isNotEmpty() && userPasswordUpdateNew.text.toString().isNotEmpty() && userPasswordUpdateConfirmNew.text.toString().isNotEmpty()) {
                    if (userPasswordUpdateNew.text.toString() == userPasswordUpdateConfirmNew.text.toString()) {
                        updateProfileWithPassword()
                        Toast.makeText(activity,"Zaktualizowano profil!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity,"Nowe hasło nie równa się potwierdzeniu hasła!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity,"Uzupełnij wszystkie pola!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        deleteProfile.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("USUWANIE PROFILU")
            builder.setMessage("Jesteś pewny(-a), że chcesz usunąć profil? Ta operacja jest nieodwracalna! (Nie spowoduje to usunięcia Twoich przepisów!)")

            builder.setPositiveButton("Tak"){dialog, which ->
                if (userPasswordUpdateOld.text.toString().isEmpty())
                {
                    Toast.makeText(activity,"Ta operacja wymaga podania hasła! (Pole 'Stare hasło')",Toast.LENGTH_SHORT).show()
                } else {
                    val user = FirebaseAuth.getInstance().currentUser

                    val credential = user!!.email?.let {
                        EmailAuthProvider
                            .getCredential(it, userPasswordUpdateOld.text.toString())
                    }

                    if (credential != null) {
                        user.reauthenticate(credential)
                            .addOnSuccessListener {
                                if (UserID != null) {
                                    db.collection("users").document(UserID).delete()
                                }

                                user.delete()
                                    .addOnCompleteListener {
                                        this.context?.let { it1 -> AuthUI.getInstance().signOut(it1) }

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
                            }
                            .addOnFailureListener {
                                Toast.makeText(activity,"Niepoprawne hasło!", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

            }

            builder.setNegativeButton("Nie"){dialog,which ->
                Toast.makeText(activity,"Anulowano",Toast.LENGTH_SHORT).show()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }

    private fun updateProfileWithoutPassword() {
        val bundle = this.arguments
        val UserID = bundle?.getString("UserID")

        val profileData = hashMapOf(
            "userName" to userNameUpdate.text.toString(),
            "bio" to userBioUpdate.text.toString(),
            "photo" to null,
            "email" to FirebaseAuth.getInstance().currentUser!!.email
        )

        if (userNameUpdate.text.isEmpty() || userBioUpdate.text.isEmpty())
        {
            Toast.makeText(activity,"Uzupełnij wszystkie pola!", Toast.LENGTH_SHORT).show()
            return
        }

        if (UserID != null) {
            db.collection("users").document(UserID)
                .set(profileData)
                .addOnSuccessListener {
                    if (imageUri != null) {
                        val fileReference: StorageReference = storageRef!!.child(UserID + "/" + System.currentTimeMillis().toString())
                        uploadTask = imageUri?.let { fileReference.putFile(it) }

                        val urlTask = uploadTask!!.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            fileReference.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val downloadUri = task.result
                                db.collection("users").document(UserID).update("photo", downloadUri.toString())
                            }
                        }
                    }
                }

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(userNameUpdate.text.toString()).build()


            val user = FirebaseAuth.getInstance().currentUser
            user!!.updateProfile(profileUpdates)
        }

    }

    private fun updateProfileWithPassword() {
        val user = FirebaseAuth.getInstance().currentUser

        updateProfileWithoutPassword()

        val credential = user!!.email?.let {
            EmailAuthProvider
                .getCredential(it, userPasswordUpdateOld.text.toString())
        }

        if (credential != null) {
            user.reauthenticate(credential)
                .addOnSuccessListener { user!!.updatePassword(userPasswordUpdateNew.text.toString()) }
                .addOnFailureListener { Toast.makeText(activity,"Niepoprawne hasło!", Toast.LENGTH_SHORT).show() }
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null
        ) {
            imageUri = data.data
            Picasso.get().load(imageUri).into(updateImage)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileUpdate().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
