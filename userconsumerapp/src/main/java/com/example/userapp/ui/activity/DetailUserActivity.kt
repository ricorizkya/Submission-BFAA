package com.example.submission3.ui.activity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.userconsumerapp.R
import com.example.userconsumerapp.adapter.SectionPagerAdapter
import com.example.userconsumerapp.database.DatabaseContract
import com.example.userconsumerapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMNS_AVATAR
import com.example.userconsumerapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMNS_COMPANY
import com.example.userconsumerapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMNS_FAVORITE
import com.example.userconsumerapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMNS_LOCATION
import com.example.userconsumerapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMNS_REPOSITORY
import com.example.userconsumerapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMNS_USERNAME
import com.example.userconsumerapp.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.userconsumerapp.database.UserHelper
import com.example.userconsumerapp.databinding.ActivityDetailUserBinding
import com.example.userconsumerapp.model.Favorite
import com.example.userconsumerapp.model.User
import com.example.userconsumerapp.room.UserDB
import com.example.userconsumerapp.viewModel.DetailUserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
        const val EXTRA_TYPE = "extra_type"

        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.follower,
                R.string.following
        )
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var userHelper: UserHelper
    private var statusFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
        viewPagerConfig()

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val values = ContentValues()
        userHelper = UserHelper.getInstance(this)
        userHelper.open()
        binding.fabFavorite.setOnClickListener {
            if (statusFavorite) {
                userHelper.deleteDB(user.username.toString())
                setStatusFavorite(false)
                Toast.makeText(this, "Delete from Favorite", Toast.LENGTH_SHORT).show()
            } else {
                values.put(COLUMNS_USERNAME, user.username)
                values.put(COLUMNS_LOCATION, user.location)
                values.put(COLUMNS_REPOSITORY, user.repository)
                values.put(COLUMNS_COMPANY, user.company)
                values.put(COLUMNS_AVATAR, user.avatar)

                setStatusFavorite(true)
                Toast.makeText(this, "Add to Favorite", Toast.LENGTH_SHORT).show()
            }
        }

//        val detailUser = intent.getParcelableExtra<User>(EXTRA_USER) as User
//        val getUser = detailUser.username
//        supportActionBar?.title = getUser

//        userHelper = UserHelper.getInstance(applicationContext)
//        userHelper.open()

//        getDetailUser(getUser!!)

//        user = intent.getParcelableExtra(EXTRA_TYPE)
//        if (user != null) {
//            getDetailUser(getUser!!)
//            statusFavorite = true
//            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
//        }else {
//            getDetailUser(getUser!!)
//        }

        val select = userHelper.queryById(user.username.toString())
        if (select.moveToNext()) {
            setStatusFavorite(true)
        }else {
            setStatusFavorite(false)
        }

//        binding.fabFavorite.setOnClickListener {
//            if (statusFavorite) {
//                userHelper.deleteDB(user?.username.toString())
//                Toast.makeText(applicationContext, "Delete From Favorite", Toast.LENGTH_SHORT).show()
//                binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
//            } else {
//                val values = ContentValues()
//                values.put(COLUMNS_USERNAME, binding.tvUsername.text.toString())
////                values.put(COLUMNS_AVATAR, avatar)
//                values.put(COLUMNS_REPOSITORY, binding.tvRepository3.text.toString())
//                values.put(COLUMNS_COMPANY, binding.tvCompany3.text.toString())
//                values.put(COLUMNS_LOCATION, binding.tvLocation3.text.toString())
//                values.put(COLUMNS_FAVORITE, 1)
//
//                userHelper.insertDB(values)
//
//                statusFavorite = true
////                contentResolver.insert(CONTENT_URI, values)
//                Toast.makeText(applicationContext, "Add to Favorite", Toast.LENGTH_SHORT).show()
//                binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
//            }
//        }
    }

    private fun getDetailUser(getUser: String) {
        detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailUserViewModel::class.java)
        detailUserViewModel.setUserDetail(getUser, this)
        detailUserViewModel.getUserDetail().observe(this, Observer {
            binding.tvUsername.text   = it.username
            binding.tvType.text = it.type
            binding.tvFollower.text = it.follower
            binding.tvFollowing.text = it.following
            binding.tvCompany3.text = it.company
            binding.tvRepository3.text  = it.repository
            binding.tvLocation3.text  = it.location
            Glide.with(this)
                .load(it.avatar)
                .into(binding.imgAvatar)
//            avatar = it.avatar.toString()
            showLoading(false)
        })
    }

    private fun getData() {
        val extras = intent
        val user = extras.getParcelableExtra<User>(EXTRA_USER) as User
        supportActionBar?.title = user.username
        binding.tvUsername.text = user.username
        binding.tvType.text = user.type
        binding.tvCompany3.text = user.company
        binding.tvLocation3.text = user.location
        binding.tvRepository3.text = user.repository
        Glide.with(this)
                .load(user.avatar)
                .into(binding.imgAvatar)
        showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun viewPagerConfig() {
        val sectionPagerAdapter = SectionPagerAdapter(this, this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setStatusFavorite(favorite: Boolean) {
        if (favorite) {
            statusFavorite = true
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            Toast.makeText(this, "Add to Favorite", Toast.LENGTH_SHORT).show()
        }else{
            statusFavorite = false
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }
}