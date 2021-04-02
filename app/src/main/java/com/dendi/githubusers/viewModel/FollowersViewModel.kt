package com.dendi.githubusers.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dendi.githubusers.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersViewModel: ViewModel() {
    val followersModel = MutableLiveData<ArrayList<User>>()

    fun setData( username: String){
        val listItem = ArrayList<User>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 19476a21e81d3a900099a629dd5054d51d493b19")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username/followers"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try{
                    val result = String(responseBody)
                    val jsonArray = JSONArray(result)
                    Log.d("resonse", jsonArray.toString())
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val user = User()
                        user.userName = jsonObject.getString("login")
                        user.photo = jsonObject.getString("avatar_url")

                        listItem.add(user)
                    }

                    followersModel.postValue(listItem)
                } catch (e: Exception) {
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

    fun getData(): LiveData<ArrayList<User>> {
        return followersModel
    }
}