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
import com.google.firebase.messaging.FirebaseMessaging
import com.km.letmeoutkmitl.signin.SignInWithGoogle
//import android.support.test.espresso.core.internal.deps.guava.io.ByteStreams.toByteArray
import com.km.letmeoutkmitl.signin.SignInWithFB
import com.km.letmeoutkmitl.user.UserSP
import java.util.*


class MainActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int = 1
    private val RC_SIGN_IN_FB: Int = 2
    private var signInWithGoogle: SignInWithGoogle? = null
    private var signInWithFB: SignInWithFB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLogin()
        signInWithGoogle = SignInWithGoogle(lifecycle, this)
        signInWithFB = SignInWithFB(lifecycle, this)
        setContentView(R.layout.activity_main)
        sign_in_button.setOnClickListener {
            signInWithGoogle!!.signIn()
        }
        sign_in_button.setSize(SignInButton.SIZE_WIDE)

        sign_in_button_fb.setOnClickListener{
            signInWithFB!!.signIn()
        }
        sign_in_button_fb.setReadPermissions(Arrays.asList("email", "public_profile"))

    }

//    override fun onStart() {
//        super.onStart()
//        printHashKey(this)
//
//    }

//    fun printHashKey(pContext: Context) {
//        try {
//            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                val hashKey = String(Base64.encode(md.digest(), 0))
//                Log.i("tag2", "printHashKey() Hash Key: " + hashKey)
//            }
//        } catch (e: NoSuchAlgorithmException) {
//            Log.e("tag2", "printHashKey()", e)
//        } catch (e: Exception) {
//            Log.e("tag2", "printHashKey()", e)
//        }
//
//    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                UserSP.setEmail(this, account.email!!)
                signInWithGoogle!!.firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
//                Log.w(FragmentActivity.TAG, "Google sign in failed", e)
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT)
                // ...
            }
            Toast.makeText(this, "Google sign in failed2", Toast.LENGTH_SHORT)
        }
        else if(signInWithFB!!.onActivityResult(requestCode, resultCode, data)){
//            Log.d("tag2", "activity")
//            signInWithFB!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    public fun checkLogin(){
        if (!UserSP.getUid(this).equals("")){
//            Login Sucess
            FirebaseMessaging.getInstance().subscribeToTopic(UserSP.getUid(this))
            val intent = Intent(this, HomeActivityy::class.java)
            startActivity(intent)
            finish()
            return
        }
    }


}
