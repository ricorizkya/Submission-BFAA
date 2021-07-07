package com.example.userconsumer.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.userconsumer.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailUserViewModel: ViewModel() {

    private val userDetail = MutableLiveData<User>()
    private var listData: ArrayList<User> = ArrayList()

    fun setUserDetail(getUser: String?, context: Context) {

        val token = "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"
        val url = "https://api.github.com/users/$getUser"
        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val user = User()
                    user.username = responseObject.getString("login")
                    user.type = responseObject.getString("type")
                    user.follower = responseObject.getInt("followers").toString()
                    user.following = responseObject.getInt("following").toString()
                    user.company = responseObject.getString("company")
                    user.location = responseObject.getString("location")
                    user.repository = responseObject.getInt("public_repos").toString()
                    user.avatar = responseObject.getString("avatar_url")

                    userDetail.postValue(user)
                    listData.add(user)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getUserDetail(): LiveData<User> {
        return userDetail
    }


}