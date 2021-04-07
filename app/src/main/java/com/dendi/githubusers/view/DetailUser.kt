package com.dendi.githubusers.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dendi.githubusers.R
import com.dendi.githubusers.adapter.SectionsPagerAdapter
import com.dendi.githubusers.databinding.ActivityDetailUserBinding
import com.dendi.githubusers.model.User
import com.dendi.githubusers.viewModel.UserViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailUser : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var getUserModel : UserViewModel

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

        showLoading(true)
        showDataUser(userName.toString())

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

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.imgPhoto.visibility = View.GONE
            binding.imgLocation.visibility = View.GONE
            binding.imgCompany.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.imgPhoto.visibility = View.VISIBLE
            binding.imgLocation.visibility = View.VISIBLE
            binding.imgCompany.visibility = View.VISIBLE
        }
    }

    private fun showDataUser(username:String){
        getUserModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        getUserModel.setUser(username)
        getUserModel.getUser().observe(this,{userData ->
            if (userData != null) {
                Log.d("tag",userData[0].toString())
                view(userData[0])
                showLoading(false)
            }
        })
    }

    private fun view( user : User){
        Glide.with(this)
                .load(user.photo)
                .apply(RequestOptions().override(150,150))
                .into(binding.imgPhoto)
        binding.tvName.text = user.name
        binding.tvUsername.text = user.userName
        binding.tvLocation.text = user.location
        binding.tvCompany.text = user.company

        val followers = user.followers
        val following = user.following
        val repo = user.repository
        val text = resources.getString(R.string.user_sum,followers,following,repo)
        binding.tvDetailSum.text = text
    }
}