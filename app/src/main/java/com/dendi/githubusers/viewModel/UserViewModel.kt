package com.dendi.githubusers.viewModel

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dendi.githubusers.BuildConfig
import com.dendi.githubusers.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class UserViewModel: ViewModel() {
    val userData = MutableLiveData<ArrayList<User>>()

    fun setUser(username:String) {
        val item = ArrayList<User>()
        val client = AsyncHttpClient()
        val key = BuildConfig.GITHUB_TOKEN
        client.addHeader("Authorization", "token $key")
        client.addHeader("User-Agent","request")
        val url = "https://api.github.com/users/${username}"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val jsonObject = JSONObject(result)
                    val user = User()
                    user.photo = jsonObject.getString("avatar_url")
                    user.userName = jsonObject.getString("login")
                    user.name = jsonObject.getString("name")
                    user.location = jsonObject.getString("location")
                    user.company = jsonObject.getString("company")
                    user.repository = jsonObject.getInt("public_repos")
                    user.followers = jsonObject.getInt("followers")
                    user.following = jsonObject.getInt("following")

                    item.add(user)
                    userData.postValue(item)
                } catch (e: Exception) {
                    Log.d("Exception",e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message + " DETAIL"}"
                }
                Log.d("error",errorMessage)
            }
        })
    }

    fun getUser(): LiveData<ArrayList<User>> {
        return userData
    }
}