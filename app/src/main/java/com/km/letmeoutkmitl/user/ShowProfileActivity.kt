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
import android.util.Log
import android.view.Menu
import android.widget.Toast
import com.google.gson.JsonObject
import com.km.letmeoutkmitl.firebase.SendNotificationAPI
import com.km.letmeoutkmitl.firebase.model.SendNotificationModel
import okhttp3.MediaType
import retrofit2.Retrofit
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import android.view.MenuItem


/**
 * Created by jongzazaal on 2/22/2018.
 */
class ShowProfileActivity:BaseActivity() {
    var user: User = User()
    var uid:String = ""
    var progress_spinner: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var backButton = getDrawable(R.drawable.ic_back)
//        supportActionBar!!.setHomeAsUpIndicator(backButton)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.hide()

        setContentView(R.layout.show_profile_activity)
        val retrofit = Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/fcm/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var sendNotificationAPI: SendNotificationAPI = retrofit.create(SendNotificationAPI::class.java)
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
                        firstname.text = it!!.firstname
                        lastname.text = it.lastname
                        student_id.text = it.student_id
                        faculty.text = it.faculty
                        mobilephone1.text = it.mobilephone1
                        line_id.text = it.line_id

                    }
                    user = it
                })
        mobilephone1_layout.setOnClickListener{
            dialContactPhone(user.mobilephone1)
        }
        noti.setOnClickListener{
            var notification = SendNotificationModel()
            notification.to += uid
            var t :Call<JsonObject> = sendNotificationAPI.sendNotification(notification)

            t.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val s = response.body()!!.toString()
                    Log.d("TAG2", "onResponse: " + s)
                    Toast.makeText(this@ShowProfileActivity, "ส่งเรียบร้อย", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("TAG2", "onFailure: " + t.message)
                }
            })

        }
//        comment_button.setOnClickListener{
//
//            ViewModelProviders.of(this@ShowProfileActivity)
//                    .get(ManageUser::class.java)
//                    .sendComment(uid, Comment(comment.text.toString()))
//                    .observe(this, Observer {
//                        if (it==true){
//                            Toast.makeText(this, "ส่งเรียบร้อย", Toast.LENGTH_SHORT).show()
//                        }
//                        else{
//                            Toast.makeText(this, "Send Failed", Toast.LENGTH_SHORT).show()
//                        }
//                    })
//        }
        toolbar_home.setOnClickListener {
            onBackPressed()
        }
    }


    private fun dialContactPhone(phoneNumber: String) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)))
    }

    private fun sendEmail(email:String){
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null))
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "address")
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "กรุณามาเลื่อนรถ")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "รถของคุณขวางทางอยู่กรุณามาเลื่อนรถออกไปด้วย")
        startActivity(Intent.createChooser(emailIntent, "Send Email..."))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
        // This is the up button
            android.R.id.home -> {
//                NavUtils.navigateUpFromSameTask(this)
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                onBackPressed()
                return true
            }

            R.id.toolbar_home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}