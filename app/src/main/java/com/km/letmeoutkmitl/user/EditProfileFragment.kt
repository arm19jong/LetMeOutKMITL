package com.km.letmeoutkmitl.user

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.Toast
import com.km.letmeoutkmitl.R
import com.km.letmeoutkmitl.baseclass.BaseFragment
import com.km.letmeoutkmitl.firebase.ManageUser
import kotlinx.android.synthetic.main.edit_profile_fragment.*
import kotlinx.android.synthetic.main.edit_profile_fragment.view.*
import android.view.KeyEvent.KEYCODE_ENTER
import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.content.Context.INPUT_METHOD_SERVICE




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
        toSave()
        bindView = view
        bindView!!.save.setOnClickListener {
            if (check()){
                save()
            }
            return@setOnClickListener

        }
        bindView!!.officephone.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if ((event.action == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    // Perform action on key press
//                    Toast.makeText(this@EditProfileFragment.context, bindView!!.officephone.text.toString(), Toast.LENGTH_SHORT).show()
                    if (check()){
                        save()
                    }
                    return true
                }
                return false
            }
        })

    }

    fun check():Boolean{
        var bool = true
        if(isSave){return bool}
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
    var isSave = true
    fun save(){
        if (isSave){
            toEdit()
            return
        }
        else{
            toSave()
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
                    toSave()
                })

    }
    fun toSave(){
        save.text = "EDIT"
        isSave = true
        email.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        firstname.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        lastname.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        mobilephone1.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        mobilephone2.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        officephone.setTextColor(ContextCompat.getColor(context!!, R.color.black))

//        email.clearFocus()

//        email.isCursorVisible = false
        email.isEnabled = false
        firstname.isEnabled = false
        lastname.isEnabled = false
        mobilephone1.isEnabled = false
        mobilephone2.isEnabled = false
        officephone.isEnabled = false

        email.clearFocus()
        layout.requestFocus()
//        email.c(ContextCompat.getColor(context!!, R.color.black))

    }
    fun toEdit(){
        save.text = "SAVE"
        isSave = false
        email.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
        firstname.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
        lastname.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
        mobilephone1.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
        mobilephone2.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
        officephone.setTextColor(ContextCompat.getColor(context!!, R.color.blue))


        email.isEnabled = true
        firstname.isEnabled = true
        lastname.isEnabled = true
        mobilephone1.isEnabled = true
        mobilephone2.isEnabled = true
        officephone.isEnabled = true
    }


}