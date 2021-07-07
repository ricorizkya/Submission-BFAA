package com.example.mainapp.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mainapp.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowerViewModel: ViewModel() {

    private val userFollower = MutableLiveData<ArrayList<User>>()

    fun setUserFollower(getUser: String?, context: Context) {

        val listUserFollower = ArrayList<User>()
        val token = "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"
        val url = "https://api.github.com/users/$getUser/followers"
        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseArray = JSONArray(result)

                    for (i in 0 until responseArray.length()) {
                        val item = responseArray.getJSONObject(i)
                        val user = User()
                        user.username = item.getString("login")
                        user.type = item.getString("type")
                        user.avatar = item.getString("avatar_url")

                        listUserFollower.add(user)
                    }
                    userFollower.postValue(listUserFollower)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getUserFollower(): LiveData<ArrayList<User>> {
        return userFollower
    }
}