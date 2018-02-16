package com.km.letmeoutkmitl.user

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.km.letmeoutkmitl.R
import com.km.letmeoutkmitl.firebase.ManageUser
import kotlinx.android.synthetic.main.edit_profile_activity.*

/**
 * Created by jongzazaal on 2/10/2018.
 */
class EditProfileActivity:AppCompatActivity() {
    var user: User = User()
    var uid:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = UserSP.getUid(this)
        setContentView(R.layout.edit_profile_activity)
        save.setOnClickListener {
            user = User(
                    email = email.text.toString(),
                    firstname = firstname.text.toString(),
                    lastname = lastname.text.toString(),
                    mobilephone1 = mobilephone1.text.toString(),
                    mobilephone2 = mobilephone2.text.toString(),
                    officephone = mobilephone2.text.toString()
                    )
            ViewModelProviders.of(this)
                    .get(ManageUser::class.java)
                    .addOrEditUser(uid, user)
                    .observe(this, Observer {
                        if (it==true){
                            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show()
                        }
                    })
        }
        ViewModelProviders.of(this)
                .get(ManageUser::class.java)
                .getUser(uid)
                .observe(this, Observer {
                    if (it == User()){

                    }
                    else{
                        email.setText(it!!.email)
                        firstname.setText(it.firstname)
                        lastname.setText(it.lastname)
                        mobilephone1.setText(it.mobilephone1)
                        mobilephone2.setText(it.mobilephone2)
                        officephone.setText(it.officephone)
                    }
                })

    }

}