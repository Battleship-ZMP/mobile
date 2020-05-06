package com.example.coolrecipes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.coolrecipes.fragments.*
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val REQUEST_CODE: Int = 6969

    lateinit var providers : List<AuthUI.IdpConfig>
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var headerView: View
    lateinit var navMenu: Menu
    lateinit var username: TextView
    lateinit var mainFragment: MainFragment
    lateinit var cookbookFavoritesFragment: CookBookFavoritesFragment
    lateinit var cookbookAddedFragment: CookBookAddedFragment
    lateinit var profileFragment: ProfileFragment
    lateinit var addRecipeFragment: AddRecipeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navMenu = nav_view.menu
        headerView = navView.getHeaderView(0)
        username = headerView.findViewById(R.id.username)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        providers = listOf<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            //AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        mainFragment = MainFragment.newInstance()
        cookbookAddedFragment = CookBookAddedFragment.newInstance()
        cookbookFavoritesFragment = CookBookFavoritesFragment.newInstance()
        profileFragment = ProfileFragment.newInstance()
        addRecipeFragment = AddRecipeFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, mainFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            loggedIn()

        } else {
            loggedOut()
        }
    }

    private fun loggedOut() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, mainFragment)
            .addToBackStack(mainFragment.toString())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        supportFragmentManager
            .popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        navMenu.findItem(R.id.nav_logout).isVisible = false
        navMenu.findItem(R.id.nav_cookbook).isVisible = false
        navMenu.findItem(R.id.nav_profile).isVisible = false
        navMenu.findItem(R.id.nav_add_recipe).isVisible = false
        navMenu.findItem(R.id.nav_login).isVisible = true
        username.text = "Niezalogowano"
    }
    private fun loggedIn() {
        navMenu.findItem(R.id.nav_logout).isVisible = true
        navMenu.findItem(R.id.nav_cookbook).isVisible = true
        navMenu.findItem(R.id.nav_profile).isVisible = true
        navMenu.findItem(R.id.nav_add_recipe).isVisible = true
        navMenu.findItem(R.id.nav_login).isVisible = false
        getProfile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK) {
                Toast.makeText(this,"Zalogowano pomyślnie!",Toast.LENGTH_SHORT).show()
                loggedIn()
            } else {
                if (response == null) {
                    finishActivity(requestCode)
                }
                else {
                    Toast.makeText(this, "" + response!!.error!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_main -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .addToBackStack(mainFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.nav_add_recipe -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, addRecipeFragment)
                    .addToBackStack(addRecipeFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.nav_cookbook_added -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, cookbookAddedFragment)
                    .addToBackStack(cookbookAddedFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.nav_cookbook_favorited -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, cookbookFavoritesFragment)
                    .addToBackStack(cookbookFavoritesFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.nav_profile -> {
                val userBundle = Bundle()
                userBundle.putString("UserID", FirebaseAuth.getInstance().currentUser!!.uid)
                profileFragment.arguments = userBundle

                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, profileFragment)
                    .addToBackStack(profileFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.nav_login -> {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme_NoActionBar)
                    .build(),REQUEST_CODE)
            }
            R.id.nav_logout -> {
                AuthUI.getInstance().signOut(this@MainActivity)
                    .addOnCompleteListener {
                        Toast.makeText(this,"Wylogowano!",Toast.LENGTH_SHORT).show()
                        loggedOut()
                    }
                    .addOnFailureListener {
                        e -> Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
                    }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun createProfile() {
        val profile = hashMapOf(
            "userName" to FirebaseAuth.getInstance().currentUser!!.displayName,
            "email" to FirebaseAuth.getInstance().currentUser!!.email,
            "bio" to "Biografia",
            "photo" to null
        )

        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .set(profile)
    }

    private fun getProfile() {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
        userRef.get()
            .addOnSuccessListener { document ->
                    if (document.data == null) {
                        createProfile()
                    }
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    username.text = "${user.displayName}"
                }
            }
    }

}