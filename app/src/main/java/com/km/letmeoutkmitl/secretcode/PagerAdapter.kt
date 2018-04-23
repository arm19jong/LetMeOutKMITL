package com.km.letmeoutkmitl.secretcode

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.km.letmeoutkmitl.R

/**
 * Created by jongzazaal on 4/23/2018.
 */
class PagerAdapter(fragmentManager: FragmentManager, easterList: MutableList<Int>): FragmentPagerAdapter (fragmentManager){
    var easterList = easterList
    override fun getItem(position: Int): Fragment {
        return ItemPager().newInstance(easterList[position])
    }

    override fun getCount(): Int {
        return easterList.size
    }
}