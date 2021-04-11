package com.dendi.githubusers.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dendi.githubusers.R
import com.dendi.githubusers.adapter.UsersFavoriteAdapter
import com.dendi.githubusers.databinding.ActivityFavoriteBinding
import com.dendi.githubusers.db.UserHelper
import com.dendi.githubusers.helper.MappingHelper
import com.dendi.githubusers.model.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: UsersFavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val title:String = getString(R.string.favorite)
        setActionBarTitle(title)

        binding.tvUsers.layoutManager = LinearLayoutManager(this)
        binding.tvUsers.setHasFixedSize(true)
        adapter = UsersFavoriteAdapter(this)
        binding.tvUsers.adapter = adapter

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.dataUsers = list
            }
        }
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val noteHelper = UserHelper.getInstance(applicationContext)
            noteHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = noteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val notes = deferredNotes.await()
            if (notes.size > 0) {
                adapter.dataUsers = notes
                binding.emptyView.visibility = View.INVISIBLE
            } else {
                adapter.dataUsers = ArrayList()
                binding.emptyView.visibility = View.VISIBLE
                val text = getString(R.string.no_data)
                showSnackbarMessage(text)
            }
            noteHelper.close()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.dataUsers)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.tvUsers, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadNotesAsync()
    }
}