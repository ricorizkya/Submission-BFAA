package com.example.submission3.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.userconsumerapp.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel: ViewModel() {

    private val userList = MutableLiveData<ArrayList<User>>()

    fun setUserGithub(username: String) {

        val listUser = ArrayList<User>()
        val token = "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"
        val url = "https://api.github.com/search/users?q=$username"
        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val item = list.getJSONObject(i)
                        val user = User()
                        user.username = item.getString("login")
                        user.type = item.getString("type")
                        user.avatar = item.getString("avatar_url")

                        listUser.add(user)
                    }
                    userList.postValue(listUser)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getUserGithub(): LiveData<ArrayList<User>> {
        return userList
    }
}