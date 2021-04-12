package com.dendi.consumerapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import com.dendi.consumerapp.databinding.ActivityDetailUserBinding
import com.dendi.consumerapp.model.User
import com.dendi.consumerapp.R

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

        showLoading(true)
        
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.imgPhoto.visibility = View.GONE
            binding.imgLocation.visibility = View.GONE
            binding.imgCompany.visibility = View.GONE
            binding.favBotton.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.imgPhoto.visibility = View.VISIBLE
            binding.imgLocation.visibility = View.VISIBLE
            binding.imgCompany.visibility = View.VISIBLE
            binding.favBotton.visibility = View.VISIBLE
        }
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