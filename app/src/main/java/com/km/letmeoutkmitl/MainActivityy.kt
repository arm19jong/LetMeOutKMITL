package com.km.letmeoutkmitl

import android.os.Bundle
import android.widget.Toast
import com.km.letmeoutkmitl.baseclass.BaseActivity
import com.km.letmeoutkmitl.qr.GenQrFragment
import com.km.letmeoutkmitl.qr.ScanQrFragment
import com.km.letmeoutkmitl.user.EditProfileFragment
import kotlinx.android.synthetic.main.main_activity.*

/**
 * Created by jongzazaal on 2/21/2018.
 */
class MainActivityy:BaseActivity() {
    private val TIME_DELAY = 2000
    private var back_pressed: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_scan -> {pushFragment(ScanQrFragment.newInstance())}
                R.id.item_gen ->{ pushFragment(GenQrFragment.newInstance()) }
                R.id.item_profile ->{pushFragment(EditProfileFragment.newInstance())}

            }
            true
        }
        bottomNavigationView.selectedItemId = R.id.item_gen
    }
    override fun onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(baseContext, "Press once again to exit!",
                    Toast.LENGTH_SHORT).show()
        }
        back_pressed = System.currentTimeMillis()
    }

}