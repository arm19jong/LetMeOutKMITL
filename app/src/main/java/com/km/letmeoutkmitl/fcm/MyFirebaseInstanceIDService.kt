package com.km.letmeoutkmitl.fcm

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by jongzazaal on 2/28/2018.
 */
class MyFirebaseInstanceIDService: FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().token
        sendRegistrationToServer(refreshedToken!!)
    }

    private fun sendRegistrationToServer(token: String) {
        // Add custom implementation, as needed.
//        var mFBViewModel = FBViewModel()
//        mFBViewModel.regisFCM(Contexter.context!!)
    }
}