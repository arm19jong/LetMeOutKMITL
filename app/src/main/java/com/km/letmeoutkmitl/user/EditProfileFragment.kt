package com.km.letmeoutkmitl.user

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.km.letmeoutkmitl.R
import com.km.letmeoutkmitl.baseclass.BaseFragment
import com.km.letmeoutkmitl.firebase.ManageUser
import kotlinx.android.synthetic.main.edit_profile_fragment.*
import kotlinx.android.synthetic.main.edit_profile_fragment.view.*

/**
 * Created by jongzazaal on 2/10/2018.
 */
class EditProfileFragment :BaseFragment() {
    var user: User = User()
    var uid:String = ""
    var progress_spinner:ProgressDialog? = null

    companion object Factory {
        fun newInstance(): EditProfileFragment {
            var arg = Bundle()
            var flagment = EditProfileFragment()
            flagment.arguments = arg
            return flagment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.edit_profile_fragment, container, false)
        return view
    }

    var bindView:View? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView = view
        bindView!!.save.setOnClickListener {
            if (!check()){
                return@setOnClickListener
            }
            user = User(
                    email = bindView!!.email.text.toString(),
                    firstname = bindView!!.firstname.text.toString(),
                    lastname = bindView!!.lastname.text.toString(),
                    mobilephone1 = bindView!!.mobilephone1.text.toString(),
                    mobilephone2 = bindView!!.mobilephone2.text.toString(),
                    officephone = bindView!!.officephone.text.toString()
            )
            ViewModelProviders.of(this)
                    .get(ManageUser::class.java)
                    .addOrEditUser(uid, user)
                    .observe(this, Observer {
                        if (it==true){
                            Toast.makeText(this.context, "Saved", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this.context, "Save Failed", Toast.LENGTH_SHORT).show()
                        }
                    })
        }


    }
    fun check():Boolean{
        var bool = true
        if (email.text.toString() == ""){
            bool = false
            email.error = "not null"
        }
        else{email.error = null}

        if(firstname.text.toString() == ""){
            bool = false
            firstname.error = "not null"
        }
        else{firstname.error = null}

        if(lastname.text.toString() == ""){
            bool = false
            lastname.error = "not null"
        }
        else{lastname.error = null}

        if(mobilephone1.text.toString() == ""){
            bool = false
            mobilephone1.setError("not null")
        }
        else{mobilephone1.error = null}

        return bool

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = UserSP.getUid(this.context!!)

        progress_spinner = ProgressDialog(this.context)
        progress_spinner!!.setMessage("Loading...")
        progress_spinner!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress_spinner!!.show()
        ViewModelProviders.of(this@EditProfileFragment)
                .get(ManageUser::class.java)
                .getUser(uid)
                .observe(this@EditProfileFragment, Observer<User> {
                    progress_spinner!!.cancel()
                    if (it == User()){
                        bindView!!.email.setText(UserSP.getEmail(this.context!!))
                    }
                    else{
                        UserSP.setEmail(this.context!!, it!!.email)
                        bindView!!.email.setText(it.email)
                        bindView!!.firstname.setText(it.firstname)
                        bindView!!.lastname.setText(it.lastname)
                        bindView!!.mobilephone1.setText(it.mobilephone1)
                        bindView!!.mobilephone2.setText(it.mobilephone2)
                        bindView!!.officephone.setText(it.officephone)
                    }
                })

    }

}