package com.dendi.githubusers.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dendi.githubusers.R
import com.dendi.githubusers.adapter.UsersAdapter
import com.dendi.githubusers.viewModel.FollowersViewModel
import com.dendi.githubusers.viewModel.FollowingViewModel

class FollowerFollowingFragment : Fragment() {
    private lateinit var adapter: UsersAdapter
    private lateinit var getFollowersModel: FollowersViewModel
    private lateinit var getFollowingModel: FollowingViewModel

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val USERNAME = "section_username"

        @JvmStatic
        fun newInstance(index: Int, username: String) = FollowerFollowingFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, index)
                putString(USERNAME, username)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followers_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UsersAdapter()
        adapter.notifyDataSetChanged()

        val tvUsers: RecyclerView = view.findViewById(R.id.tvUsers)
        tvUsers.layoutManager = LinearLayoutManager(this.context)
        tvUsers.adapter = adapter

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(USERNAME)

        if (index == 0) {
            showFollowers(username.toString())
        } else {
            showFollowing(username.toString())
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun showFollowers(username: String) {
        getFollowersModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)
        getFollowersModel.setData(username)
        getFollowersModel.getData().observe(this, { listUsers ->
            if (listUsers != null) {
                adapter.setData(listUsers)
            }
        })
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun showFollowing(username: String) {
        getFollowingModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        getFollowingModel.setData(username)
        getFollowingModel.getData().observe(this, { listUsers ->
            if (listUsers != null) {
                adapter.setData(listUsers)
            }
        })
    }

}