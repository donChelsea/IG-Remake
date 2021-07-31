package com.example.ig_remake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.ig_remake.databinding.ActivityMainBinding
import com.example.ig_remake.databinding.FragmentHomeBinding
import com.example.ig_remake.presentation.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val homeFragment = HomeFragment()

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

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}