package com.km.letmeoutkmitl.user

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.Toast
import com.km.letmeoutkmitl.R
import com.km.letmeoutkmitl.baseclass.BaseFragment
import com.km.letmeoutkmitl.firebase.ManageUser
import kotlinx.android.synthetic.main.edit_profile_fragment.*
import kotlinx.android.synthetic.main.edit_profile_fragment.view.*
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.spinner_item.view.*


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
//        toSave()
        bindView = view
        bindView!!.save.setOnClickListener {
            if (check()){
                save()
            }
            return@setOnClickListener

        }
        bindView!!.edit.setOnClickListener {
            toEdit()
        }
        bindView!!.line_id.setOnKeyListener(object : View.OnKeyListener {
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
        val spinnerArrayAdapter = ArrayAdapter<String>(context, R.layout.spinner_item,
                facultyData)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_drop_down)
        bindView!!.faculty.adapter = spinnerArrayAdapter
//        bindView!!.faculty.background.setColorFilter(ContextCompat.getColor(context!!,R.color.blue), PorterDuff.Mode.SRC_ATOP);
//        bindView!!.faculty.tv_spinner_item.setTextColor(ContextCompat.getColor(context!!, R.color.blue))

    }

    fun check():Boolean{
        var bool = true
        if(isSave){return bool}

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
        if(mobilephone1.text.toString().length != 10){
            bool = false
            mobilephone1.setError("wrong pattern")
        }
        else{mobilephone1.error = null}

        return bool

    }
    var isSave = true
    fun save(){
        if (isSave){
//            toEdit()
            return
        }
        else{
//            toSave()
        }
        user = User(
                email = bindView!!.email.text.toString(),
                firstname = bindView!!.firstname.text.toString(),
                lastname = bindView!!.lastname.text.toString(),
                mobilephone1 = bindView!!.mobilephone1.text.toString(),
                student_id = bindView!!.student_id.text.toString(),
                faculty = bindView!!.faculty.selectedItem.toString(),
                line_id = bindView!!.line_id.text.toString()
        )

        ViewModelProviders.of(this)
                .get(ManageUser::class.java)
                .addOrEditUser(uid, user)
                .observe(this, Observer {
                    if (it==true){
                        userData = user
                        toSave()
                        Toast.makeText(this.context, "Saved", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this.context, "Save Failed", Toast.LENGTH_SHORT).show()
                    }
                })
    }
    var facultyData: MutableList<String> = arrayListOf("วิศวกรรมศาสตร์", "สถาปัตยกรรมศาสตร์", "ครุศาสตร์อุตสาหกรรมและเทคโนโลยี",
            "เทคโนโลยีการเกษตร", "วิทยาศาสตร์", "อุตสาหกรรมเกษตร","เทคโนโลยีสารสนเทศ","วิทยาลัยนานาชาติ", "วิทยาลัยนาโนเทคโนโลยีพระจอมเกล้าลาดกระบัง",
            "วิทยาลัยนวัตกรรมการผลิตขั้นสูง", "การบริหารและจัดการ", "วิทยาลัยอุตสาหกรรมการบินนานาชาติ", "ศิลปศาสตร์", "ไม่ระบุ")
    var userData:User? = null
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
//                    toSave()
                    if (it == User()){
//                        bindView!!.email.setText(UserSP.getEmail(this.context!!))
                        userData = it

                    }
                    else{
                        userData = it
                        UserSP.setEmail(this.context!!, it!!.email)
//                        bindView!!.email.setText(it.email)
                        bindView!!.firstname.setText(it.firstname)
                        bindView!!.lastname.setText(it.lastname)
                        bindView!!.mobilephone1.setText(it.mobilephone1)
                        bindView!!.student_id.setText(it.student_id)
                        if (facultyData.indexOf(it.faculty)==-1){it.faculty = "ไม่ระบุ"}
                        bindView!!.faculty.setSelection(facultyData.lastIndexOf(it.faculty))
                        bindView!!.line_id.setText(it.line_id)

                    }
                    toSave()
                })

    }
    fun toSave(){
//        save.text = "EDIT"
        isSave = true
        firstname.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        lastname.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        mobilephone1.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        student_id.setTextColor(ContextCompat.getColor(context!!, R.color.black))
//        faculty.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        line_id.setTextColor(ContextCompat.getColor(context!!, R.color.black))

        val spinnerArrayAdapter = ArrayAdapter<String>(context, R.layout.spinner_item,
                facultyData)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_drop_down)
        faculty.adapter = spinnerArrayAdapter
        faculty.background.setColorFilter(ContextCompat.getColor(context!!,R.color.black), PorterDuff.Mode.SRC_ATOP)
        if (facultyData.indexOf(userData!!.faculty)==-1){userData!!.faculty = "ไม่ระบุ"}
        faculty.setSelection(facultyData.lastIndexOf(userData!!.faculty))

        firstname.isEnabled = false
        lastname.isEnabled = false
        mobilephone1.isEnabled = false
        student_id.isEnabled = false
        faculty.isEnabled = false
        line_id.isEnabled = false

//        email.clearFocus()
        layout.requestFocus()

    }
    fun toEdit(){
//        save.text = "SAVE"
        isSave = false
        firstname.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
        lastname.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
        mobilephone1.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
        student_id.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
//        faculty.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
        line_id.setTextColor(ContextCompat.getColor(context!!, R.color.blue))

        val spinnerArrayAdapter = ArrayAdapter<String>(context, R.layout.spinner_item_blue,
                facultyData)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_drop_down_blue)
        faculty.adapter = spinnerArrayAdapter
        faculty.background.setColorFilter(ContextCompat.getColor(context!!,R.color.blue), PorterDuff.Mode.SRC_ATOP)
        if (facultyData.indexOf(userData!!.faculty)==-1){userData!!.faculty = "ไม่ระบุ"}
        faculty.setSelection(facultyData.lastIndexOf(userData!!.faculty))


        firstname.isEnabled = true
        lastname.isEnabled = true
        mobilephone1.isEnabled = true
        student_id.isEnabled = true
        faculty.isEnabled = true
        line_id.isEnabled = true


    }


}