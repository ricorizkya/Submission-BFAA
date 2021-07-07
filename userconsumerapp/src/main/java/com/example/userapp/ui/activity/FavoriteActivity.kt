package com.example.submission3.ui.activity

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userconsumerapp.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.userconsumerapp.database.MappingHelper
import com.example.userconsumerapp.model.User
import com.example.userconsumerapp.adapter.FavoriteAdapter
import com.example.userconsumerapp.databinding.ActivityFavoriteBinding
import kotlinx.coroutines.*

class FavoriteActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var binding: ActivityFavoriteBinding
    lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        backgroundThread(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        loadUser()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_USER, favoriteAdapter.listFavorite)
    }

    private fun backgroundThread(savedInstanceState: Bundle?) {
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val observer = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUser()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, observer)
        if (savedInstanceState == null) {
            loadUser()
        }else {
            savedInstanceState.getParcelableArrayList<User>(EXTRA_USER)?.also {
                favoriteAdapter.listFavorite = it
            }
        }
    }

    private fun loadUser() {
        GlobalScope.launch(Dispatchers.Main) {
            val user = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favoriteUser = user.await()
            if (favoriteUser.size > 0) {
                favoriteAdapter.listFavorite = favoriteUser
            }else {
                favoriteAdapter.listFavorite = ArrayList()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        favoriteAdapter = FavoriteAdapter(this)
        binding.rvFavorite.adapter = favoriteAdapter
    }
}