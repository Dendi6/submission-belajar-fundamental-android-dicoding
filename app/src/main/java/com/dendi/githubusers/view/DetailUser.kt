package com.dendi.githubusers.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dendi.githubusers.R
import com.dendi.githubusers.adapter.SectionsPagerAdapter
import com.dendi.githubusers.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailUser : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    companion object{
        const val EXTRA_DATA = "extra_data"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra(EXTRA_DATA)

        val actionbar = supportActionBar
        actionbar!!.title = userName.toString()
        actionbar.setDisplayHomeAsUpEnabled(true)

        getUser(userName.toString())

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs= binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
        sectionsPagerAdapter.username = userName.toString()
    }

    @SuppressLint("StringFormatMatches")
    private fun view(avatar:String, username: String, name:String, location:String, company:String, repo:Int, followers:Int, following:Int){
        Glide.with(this)
                .load(avatar)
                .apply(RequestOptions().override(150,150))
                .into(binding.imgPhoto)
        binding.tvName.text = name
        binding.tvUsername.text = username
        binding.tvLocation.text = location
        binding.tvCompany.text = company

        val text = resources.getString(R.string.user_sum,followers,following,repo)
        binding.tvDetailSum.text = text
    }

    private fun getUser(username:String){
        binding.progressBar.visibility = View.VISIBLE
        binding.imgPhoto.visibility = View.INVISIBLE
        binding.imgLocation.visibility = View.INVISIBLE
        binding.imgCompany.visibility = View.INVISIBLE

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 19476a21e81d3a900099a629dd5054d51d493b19")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/${username}"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                binding.progressBar.visibility = View.INVISIBLE
                binding.imgPhoto.visibility = View.VISIBLE
                binding.imgLocation.visibility = View.VISIBLE
                binding.imgCompany.visibility = View.VISIBLE

                val result = String(responseBody)
                Log.d("TAG", result)
                try {
                    val jsonObject = JSONObject(result)
                    val avatar: String = jsonObject.getString("avatar_url").toString()
                    val userName: String = jsonObject.getString("login").toString()
                    val name: String = jsonObject.getString("name").toString()
                    val location:String = jsonObject.getString("location").toString()
                    val company:String = jsonObject.getString("company").toString()
                    val repository:Int = jsonObject.getInt("public_repos")
                    val followers:Int = jsonObject.getInt("followers")
                    val following:Int = jsonObject.getInt("following")

                    view(avatar,userName,name,location,company,repository,followers,following)
                } catch (e: Exception) {
                    Log.d("Exception",e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable ) {
                binding.progressBar.visibility = View.INVISIBLE

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message + " DETAIL"}"
                }
                Log.d("error",errorMessage)
                Toast.makeText(this@DetailUser, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}