package com.km.letmeoutkmitl.secretcode

import android.os.Bundle
import com.km.letmeoutkmitl.R
import com.km.letmeoutkmitl.baseclass.BaseActivity
import kotlinx.android.synthetic.main.secret_code_activity.*

/**
 * Created by jongzazaal on 4/17/2018.
 */
class SecretCodeActivity:BaseActivity() {
    var easterList:MutableList<Int> = arrayListOf(R.drawable.eastegg_earnge,R.drawable.eastegg_gloy, R.drawable.eastegg_jj)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.secret_code_activity)
        val adapter = PagerAdapter(supportFragmentManager, easterList)
        pager.adapter = adapter
        indicator.setViewPager(pager)

    }
}