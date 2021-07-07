package com.example.submission3.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userconsumerapp.adapter.FollowingAdapter
import com.example.userconsumerapp.databinding.FragmentFollowingBinding
import com.example.userconsumerapp.model.User
import com.example.userconsumerapp.ui.activity.DetailUserActivity
import com.example.userconsumerapp.viewModel.FollowingViewModel

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var followingAdapter: FollowingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followingAdapter = FollowingAdapter()
        followingAdapter.notifyDataSetChanged()

        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.adapter = followingAdapter
        binding.rvFollowing.setHasFixedSize(true)

        val intent = activity?.intent?.getParcelableExtra<User>(DetailUserActivity.EXTRA_USER) as User
        val getUser = intent.username

        if (getUser != null) {
            getUserFollowing(getUser)
        }
    }

    private fun getUserFollowing(getUser: String) {
        val followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        followingViewModel.setUserFollowing(getUser, activity!!)
        followingViewModel.getUserFollowing().observe(activity!!, Observer {
            followingAdapter.setData(it)
            println(followingAdapter)
        })
    }
}