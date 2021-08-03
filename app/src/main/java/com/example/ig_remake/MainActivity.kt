package com.example.ig_remake

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ig_remake.databinding.ActivityMainBinding
import com.example.ig_remake.presentation.authentication.LoginActivity
import com.example.ig_remake.presentation.home.HomeFragment
import com.example.ig_remake.presentation.messages.MessagesFragment
import com.example.ig_remake.util.FirebaseUtil.auth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val homeFragment = HomeFragment()
    private val messagesFragment = MessagesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(homeFragment)
        controlBottomNav()
    }

    private fun controlBottomNav() {
        binding.bottomNavigatinView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
//                R.id.exploreFragment -> replaceFragment(exploreFragment)
                R.id.homeFragment -> replaceFragment(homeFragment)
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_main, fragment)
                .addToBackStack("${fragment.id}")
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_messages -> {
                replaceFragment(messagesFragment)
            }
            R.id.menu_sign_out -> {
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}