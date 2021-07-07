package com.example.consumerapp.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.consumerapp.ui.fragment.FollowerFragment
import com.example.consumerapp.ui.fragment.FollowingFragment

class SectionPagerAdapter(private val mContext: Context, fragmentActivity: FragmentActivity):
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment()
            1 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }
}