package com.km.letmeoutkmitl.user

import android.app.IntentService
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.km.letmeoutkmitl.R
import com.km.letmeoutkmitl.baseclass.BaseActivity
import com.km.letmeoutkmitl.firebase.ManageUser
import kotlinx.android.synthetic.main.edit_profile_fragment.view.*
import kotlinx.android.synthetic.main.show_profile_activity.*
import android.net.Uri.fromParts



/**
 * Created by jongzazaal on 2/22/2018.
 */
class ShowProfileActivity:BaseActivity() {
    var user: User = User()
    var uid:String = ""
    var progress_spinner: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_profile_activity)
//        uid = UserSP.getUid(this.context!!)

        val uid:String = intent.getStringExtra("UID")
        progress_spinner = ProgressDialog(this)
        progress_spinner!!.setMessage("Loading...")
        progress_spinner!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress_spinner!!.show()
        ViewModelProviders.of(this@ShowProfileActivity)
                .get(ManageUser::class.java)
                .getUser(uid)
                .observe(this@ShowProfileActivity, Observer<User> {
                    progress_spinner!!.cancel()
                    if (it == User()){
//                        email.text = UserSP.getEmail(this)
                    }
                    else{
                        email.text = it!!.email
                        firstname.text = it.firstname
                        lastname.text = it.lastname
                        mobilephone1.text = it.mobilephone1
                        mobilephone2.text = it.mobilephone2
                        officephone.text = it.officephone
                    }
                    user = it
                })
        mobilephone1.setOnClickListener{
            dialContactPhone(user.mobilephone1)
        }
        mobilephone2.setOnClickListener{
            dialContactPhone(user.mobilephone2)
        }
        officephone.setOnClickListener{
            dialContactPhone(user.officephone)
        }
    }


    private fun dialContactPhone(phoneNumber: String) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)))
    }
}