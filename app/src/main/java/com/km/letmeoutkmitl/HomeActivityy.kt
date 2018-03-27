package com.km.letmeoutkmitl

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.km.letmeoutkmitl.baseclass.BaseActivity
import com.km.letmeoutkmitl.qr.GenQrFragment
import com.km.letmeoutkmitl.qr.ScanQrFragment
import com.km.letmeoutkmitl.user.EditProfileFragment
import kotlinx.android.synthetic.main.main_activity.*

/**
 * Created by jongzazaal on 2/21/2018.
 */
class HomeActivityy :BaseActivity() {
    private val TIME_DELAY = 2000
    private var back_pressed: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_scan -> {
                    Dexter.withActivity(this)
                            .withPermission(Manifest.permission.CAMERA)
                            .withListener(object : PermissionListener {
                                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                                    pushFragment(ScanQrFragment.newInstance())
                                }

                                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                                    token!!.continuePermissionRequest()
                                }

                                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                                    Toast.makeText(this@HomeActivityy,getString(R.string.permission_not_granted),Toast.LENGTH_SHORT).show()
                                }
                            }).check()
                }
                R.id.item_gen ->{ pushFragment(GenQrFragment.newInstance()) }
                R.id.item_profile ->{pushFragment(EditProfileFragment.newInstance())}

            }
            true
        }
        bottomNavigationView.selectedItemId = R.id.item_profile
    }
    override fun onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(baseContext, getString(R.string.press_once_again_to_exit),
                    Toast.LENGTH_SHORT).show()
        }
        back_pressed = System.currentTimeMillis()
    }

}