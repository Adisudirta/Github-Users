package com.example.githubuserdicodingproject

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserdicodingproject.adapters.FavoriteRecyclerViewAdapter
import com.example.githubuserdicodingproject.helper.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var recyclerViewAdapter: FavoriteRecyclerViewAdapter
    private lateinit var rvFavorite: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        val favoriteViewModel = obtainViewModel(this)
        favoriteViewModel.getAllFavoriteUser().observe(this){ favoriteUserList ->
            if(favoriteUserList != null){
                recyclerViewAdapter.setListFavorite(favoriteUserList)
            }
        }

        recyclerViewAdapter = FavoriteRecyclerViewAdapter(this)

        rvFavorite = findViewById(R.id.rv_favorite)
        rvFavorite.layoutManager = LinearLayoutManager(this)
        rvFavorite.setHasFixedSize(true)
        rvFavorite.adapter = recyclerViewAdapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }
}