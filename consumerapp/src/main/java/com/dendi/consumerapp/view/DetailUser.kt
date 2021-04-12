package com.dendi.consumerapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dendi.consumerapp.databinding.ActivityDetailUserBinding
import com.dendi.consumerapp.model.User
import com.dendi.consumerapp.R
import com.dendi.consumerapp.adapter.SectionsPagerAdapter
import com.dendi.consumerapp.databinding.FragmentFollowersFollowingBinding
import com.dendi.consumerapp.viewModel.UserViewModel
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

        val person = intent.getParcelableExtra<User>(EXTRA_DATA) as User
        showLoading(true)
        binding.favBotton.visibility = View.GONE
        showDataUser(person.userName.toString())
        tabLayout(person.userName.toString())
        
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setActionBarTitle(person.userName.toString())
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.share -> {
                val person = intent.getParcelableExtra<User>(EXTRA_DATA) as User
                share(person)
            }
        }
    }

    private fun tabLayout(userName: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        sectionsPagerAdapter.username = userName
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
                view(userData[0])
                showLoading(false)
            }
        })
    }

    private fun view( user : User){
        user.name?.let { setActionBarTitle(it) }
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

    private fun share(user:User){
        val username = user.userName
        val url = user.htmlUrl
        val text = resources.getString(R.string.share,username, url)

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.type = "text/plain"

        startActivity(Intent.createChooser(intent, "Share using .."))
    }
}