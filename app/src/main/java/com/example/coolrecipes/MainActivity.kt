package com.example.coolrecipes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.example.coolrecipes.fragments.CookBookFragment
import com.example.coolrecipes.fragments.ProfileFragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var providers : List<AuthUI.IdpConfig>
    private val REQUEST_CODE: Int = 6969
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var cookbookFragment: CookBookFragment
    lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            //AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        cookbookFragment = CookBookFragment.newInstance()
        profileFragment = ProfileFragment.newInstance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE)
        {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK)
            {
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this,"Zalogowano pomyślnie!",Toast.LENGTH_SHORT).show()
                if (user != null) {
                    val username: TextView = findViewById(R.id.username)
                    username.text = "${user.displayName}"
                }
            }
            else
            {
                Toast.makeText(this, ""+response!!.error!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_cookbook -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, cookbookFragment)
                    .addToBackStack(cookbookFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.nav_profile -> {
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
                        val username: TextView = findViewById(R.id.username)
                        username.text = "Niezalogowano"
                    }
                    .addOnFailureListener {
                        e -> Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
                    }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}