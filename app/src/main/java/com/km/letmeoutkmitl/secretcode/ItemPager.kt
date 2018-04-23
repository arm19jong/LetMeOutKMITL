package com.km.letmeoutkmitl.secretcode

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.km.letmeoutkmitl.R
import kotlinx.android.synthetic.main.item_pager.*

/**
 * Created by jongzazaal on 4/23/2018.
 */
class ItemPager:Fragment() {


    fun newInstance(img: Int): ItemPager {
        val fragment = ItemPager()
        val bundle = Bundle()
        bundle.putInt("img", img)
        fragment.arguments = bundle
        return fragment
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_pager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pic.setImageResource(arguments!!.getInt("img"))
    }
}