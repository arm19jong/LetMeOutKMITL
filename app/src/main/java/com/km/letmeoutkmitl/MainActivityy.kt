package com.km.letmeoutkmitl

import android.os.Bundle
import com.km.letmeoutkmitl.baseclass.BaseActivity
import com.km.letmeoutkmitl.qr.GenQrFragment
import com.km.letmeoutkmitl.user.EditProfileFragment
import kotlinx.android.synthetic.main.main_activity.*

/**
 * Created by jongzazaal on 2/21/2018.
 */
class MainActivityy:BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_scan -> {}
                R.id.item_gen ->{ pushFragment(GenQrFragment.newInstance()) }
                R.id.item_profile ->{pushFragment(EditProfileFragment.newInstance())}

            }
            true
        }
        bottomNavigationView.selectedItemId = R.id.item_gen
    }


}