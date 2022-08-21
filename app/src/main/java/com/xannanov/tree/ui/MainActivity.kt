package com.xannanov.tree.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.xannanov.tree.databinding.ActivityMainBinding
import com.xannanov.tree.models.Node
import com.xannanov.tree.models.Tree
import com.xannanov.tree.navigation.Navigation
import com.xannanov.tree.repository.SharedPreferencesRepository
import java.math.BigInteger
import java.util.*

const val PREF_KEY = "tree"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val spRepository by lazy(LazyThreadSafetyMode.NONE) {
        SharedPreferencesRepository(getPreferences(Context.MODE_PRIVATE))
    }
    private val tree by lazy(LazyThreadSafetyMode.NONE) {
        spRepository.getTreeState()
    }
    val navigation by lazy(LazyThreadSafetyMode.NONE) {
        Navigation(
            supportFragmentManager, binding.fcContainer.id, tree
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigation
    }

    override fun onStop() {
        super.onStop()
        spRepository.saveState(tree)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            navigation.onBackPressed()
        else
            finish()
    }
}