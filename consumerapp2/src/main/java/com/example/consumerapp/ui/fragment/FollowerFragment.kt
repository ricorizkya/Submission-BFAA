package com.example.consumerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.viewModel.FollowerViewModel
import com.example.consumerapp.model.User
import com.example.consumerapp.adapter.FollowerAdapter
import com.example.consumerapp.databinding.FragmentFollowerBinding
import com.example.consumerapp.ui.activity.DetailUserActivity

class FollowerFragment : Fragment() {

    private lateinit var binding: FragmentFollowerBinding
    private lateinit var followerAdapter: FollowerAdapter

    private val listFollower = ArrayList<User>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followerAdapter = FollowerAdapter()
        followerAdapter.setData(listFollower)
        followerAdapter.notifyDataSetChanged()

        binding.rvFollower.layoutManager = LinearLayoutManager(activity)
        binding.rvFollower.adapter = followerAdapter
        binding.rvFollower.setHasFixedSize(true)

        val intent = activity?.intent?.getParcelableExtra<User>(DetailUserActivity.EXTRA_USER) as User
        val getUser = intent.username

        if (getUser != null) {
            getUserFollower(getUser)
        }
    }

    private fun getUserFollower(getUser: String) {
        val followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowerViewModel::class.java)
        followerViewModel.setUserFollower(getUser, requireActivity())
        followerViewModel.getUserFollower().observe(requireActivity(), Observer {
            followerAdapter.setData(it)
            println(followerAdapter)
        })
    }
}