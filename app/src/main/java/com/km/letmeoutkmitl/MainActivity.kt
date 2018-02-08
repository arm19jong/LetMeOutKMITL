package com.km.letmeoutkmitl

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.common.api.ApiException
//import android.support.test.orchestrator.junit.BundleJUnitUtils.getResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import android.content.Intent
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.common.SignInButton
import com.km.letmeoutkmitl.signin.SignInWithGoogle


class MainActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int = 1
    private var signInWithGoogle: SignInWithGoogle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInWithGoogle = SignInWithGoogle(lifecycle, this)
        setContentView(R.layout.activity_main)
        sign_in_button.setOnClickListener {
            signInWithGoogle!!.signIn()
        }
        sign_in_button.setSize(SignInButton.SIZE_WIDE)

    }



    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                signInWithGoogle!!.firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
//                Log.w(FragmentActivity.TAG, "Google sign in failed", e)
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT)
                // ...
            }
            Toast.makeText(this, "Google sign in failed2", Toast.LENGTH_SHORT)


        }
    }



}
