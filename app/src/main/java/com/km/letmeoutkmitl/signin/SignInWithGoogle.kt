package com.km.letmeoutkmitl.signin

import android.app.Activity
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.km.letmeoutkmitl.R


/**
 * Created by jongzazaal on 2/8/2018.
 */
class SignInWithGoogle : LifecycleObserver {
    private val RC_SIGN_IN: Int = 1
    private var activity:Activity? = null
    private var lifecycle: Lifecycle? = null
    private var mAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null

    constructor(lifecycle: Lifecycle, activity:Activity){
        this.activity = activity
        this.lifecycle = lifecycle
        lifecycle!!.addObserver(this)
    }
//    init {
//
//    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity!!.resources.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity!!, gso)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        val currentUser = mAuth!!.getCurrentUser()
    }

    fun signIn() {
        val signInIntent = mGoogleSignInClient!!.getSignInIntent()
        activity!!.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
//        Log.d(FragmentActivity.TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
//                        Log.d(FragmentActivity.TAG, "signInWithCredential:success")
                        val user = mAuth!!.getCurrentUser()
                        Toast.makeText(activity, "สำเร็จ: "+user!!.email, Toast.LENGTH_SHORT).show()
//                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
//                        Log.w(FragmentActivity.TAG, "signInWithCredential:failure", task.exception)
//                        Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
//                        updateUI(null)
                        Toast.makeText(activity, "ล้มเหลว: ", Toast.LENGTH_SHORT).show()

                    }

                    // ...
                }
    }
}