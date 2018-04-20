package com.km.letmeoutkmitl.secretcode

import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.BroadcastReceiver
import android.content.Context
import android.support.v4.content.ContextCompat.startActivity

/**
 * Created by jongzazaal on 4/17/2018.
 */
class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == "android.provider.Telephony.SECRET_CODE") {
            val i = Intent(context, SecretCodeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }
}