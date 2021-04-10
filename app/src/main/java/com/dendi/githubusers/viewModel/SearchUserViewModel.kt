package com.dendi.githubusers.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dendi.githubusers.BuildConfig
import com.dendi.githubusers.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class SearchUserViewModel: ViewModel() {
    val listSearchUsers = MutableLiveData<ArrayList<User>>()

    fun setSearchUsers(username:String){
        val listItem = ArrayList<User>()
        val client = AsyncHttpClient()
        val key = BuildConfig.GITHUB_TOKEN
        client.addHeader("Authorization", "token $key")
        client.addHeader("User-Agent","request")
        val url = "https://api.github.com/search/users?q=${username}"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try{
                    val result = String(responseBody)
                    val jsonObject = JSONObject(result)
                    val dataArray = jsonObject.getJSONArray("items")
                    for (i in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(i)
                        val user = User()
                        user.userName = dataObject.getString("login")
                        user.photo = dataObject.getString("avatar_url")
                        listItem.add(user)
                    }

                    listSearchUsers.postValue(listItem)
                }catch (e: Exception) {
                    Log.d("Exception",e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("error",errorMessage)
            }
        })
    }

    fun getSearchUsers(): LiveData<ArrayList<User>> {
        return listSearchUsers
    }
}