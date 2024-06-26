package com.mobdeve.s12.fallarme.sophia.bookbuddy.collection

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
            0 -> {
                AllFragment()
            }

            1-> {
                CurrentlyReadingFragment()
            }

            2->{
                ToReadFragment()
            }

           3 -> {
               FinishedFragment()
           }

            else ->{
                AllFragment()
            }
        }
    }
}