package com.example.contentprovider

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceActivity
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity(), ListUserAdapter.DataClickCallback{

    private var listUser: ArrayList<User> = ArrayList()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecyclerView()
        getSearchUser()
        getUserGithub()
    }

    override fun onClick(view: View, listData: User) {
        val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_USER, listData)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_favorite -> {
                val intentFavorite = Intent(this, FavoriteActivity::class.java)
                startActivity(intentFavorite)
                true
            }
            R.id.menu_setting -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getUserGithub() {
        val token = "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"
        val url = "https://api.github.com/users"
        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                showLoading(false)
                val result = String(responseBody!!)
                try {
                    val user = JSONArray(result)
                    for (i in 0 until user.length()) {
                        val userObject = user.getJSONObject(i)
                        val username = userObject.getString("login")
                        getDataDetail(username)
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    private fun getDataDetail(username: String) {
        showLoading(false)
        val token = "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"
        val url = "https://api.github.com/users/$username"
        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                showLoading(false)
                val result = String(responseBody!!)
                try {
                    val user = JSONObject(result)
                    val username = user.getString("login").toString()
                    val name = user.getString("name").toString()
                    val avatar = user.getString("avatar_url").toString()
                    val company = user.getString("company").toString()
                    val location = user.getString("location").toString()
                    val repository = user.getString("public_repos")
                    val follower = user.getString("followers")
                    val following = user.getString("following")
                    val type = user.getString("type").toString()

                    listUser.add(User(
                        username, name, avatar, company, location, repository, follower, following, type
                    ))
                    setRecyclerView()
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    private fun getSearchUser() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.editUser

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        binding.editUser.queryHint = resources.getString(R.string.search_hint)
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    listUser.clear()
                    return true
                }else {
                    listUser.clear()
                    getSearchData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun getSearchData(username: String) {
        showLoading(true)
        binding.rvGithub.visibility = View.GONE

        val token = "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"
        val url = "https://api.github.com/search/users?q=$username"
        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                showLoading(false)
                binding.rvGithub.visibility = View.VISIBLE
                val result = String(responseBody!!)

                try {
                    val userObject = JSONObject(result)
                    val user = userObject.getJSONArray("items")
                    for (i in 0 until user.length()) {
                        val dataUser = user.getJSONObject(i)
                        val username = dataUser.getString("login")
                        getDataDetail(username)
                    }
                }catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out PreferenceActivity.Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    private fun setRecyclerView() {        binding.rvGithub.layoutManager = LinearLayoutManager(this)
        val adapter = ListUserAdapter(listUser, this)
        binding.rvGithub.adapter = adapter
    }
}