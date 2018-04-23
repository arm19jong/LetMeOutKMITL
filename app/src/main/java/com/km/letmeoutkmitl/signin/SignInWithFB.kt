package com.km.letmeoutkmitl.signin

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.os.Bundle
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import android.widget.Toast
//import jdk.nashorn.internal.runtime.ECMAException.getException
//import org.junit.experimental.results.ResultMatchers.isSuccessful
import android.support.annotation.NonNull
import android.util.Log
import com.facebook.AccessToken
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.km.letmeoutkmitl.user.UserSP
import java.util.*
import com.facebook.GraphResponse
import org.json.JSONObject
import com.facebook.GraphRequest
import com.google.firebase.iid.FirebaseInstanceId
import com.km.letmeoutkmitl.MainActivity
import org.json.JSONException


/**
 * Created by jongzazaal on 2/9/2018.
 */
class SignInWithFB : LifecycleObserver {
    private val RC_SIGN_IN_FB: Int = 2
    private var activity: Activity? = null
    private var lifecycle: Lifecycle? = null
    private var mAuth: FirebaseAuth? = null
    private var mCallbackManager: CallbackManager? = null

    constructor(lifecycle: Lifecycle, activity:Activity){
        this.activity = activity
        this.lifecycle = lifecycle
        lifecycle!!.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        mAuth = FirebaseAuth.getInstance()
        mCallbackManager = CallbackManager.Factory.create()

    }

    fun signIn(){
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "public_profile"))
        LoginManager.getInstance().registerCallback(mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        // App code
                        Log.d("tag2", "sucess")
                        accessToken(loginResult, activity!!)
                        handleFacebookAccessToken(loginResult.accessToken)
                    }

                    override fun onCancel() {
                        // App code
                        Log.d("tag2", "cancel")
                    }

                    override fun onError(exception: FacebookException) {
                        // App code
                        Log.d("tag2", "error")
                    }
                })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        val currentUser = mAuth!!.getCurrentUser()
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token)

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(activity!!, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success")
                            val user = mAuth!!.currentUser
//                            updateUI(user)
                            UserSP.setUid(activity!!, user!!.uid)
                            Toast.makeText(activity, "สำเร็จ",
                                    Toast.LENGTH_SHORT).show()
                            val m = activity as MainActivity
                            m.checkLogin()
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException())
//
                            Toast.makeText(activity, "ล้มเหลว: ",
                                    Toast.LENGTH_SHORT).show()
//                            updateUI(null)

                        }

                        // ...
                    }
                })
    }
    fun accessToken(loginResult: LoginResult, activity: Activity){
        var paramiter = Bundle()
        paramiter.putString("fields","email")
        val request = GraphRequest.newMeRequest(loginResult.accessToken) { jsonObject, graphResponse ->
            try {
//                Log.e("Test",jsonObject.getJSONObject("picture").getJSONObject("data").getString("url")+"")
                UserSP.setEmail(activity!!, jsonObject.getString("email"))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        request.parameters = paramiter
        request.executeAsync()
    }

    fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent):Boolean{
        return mCallbackManager!!.onActivityResult(requestCode, resultCode, data)
    }
}