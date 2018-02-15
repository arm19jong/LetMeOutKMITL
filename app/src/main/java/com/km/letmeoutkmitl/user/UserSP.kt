package com.km.letmeoutkmitl.user

import android.content.Context

/**
 * Created by jongzazaal on 2/15/2018.
 */
class UserSP {

    companion object {
        fun setUid(context: Context, uid: String){
            val sp = context.getSharedPreferences("LetMeOut", Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString("UID", uid)
            editor.commit()
        }
        fun getUid(context: Context):String{
            val sp = context.getSharedPreferences("LetMeOut", Context.MODE_PRIVATE)
            return sp.getString("UID", "")
        }

        fun setEmail(context: Context, email: String){
            val sp = context.getSharedPreferences("LetMeOut", Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString("EMAIL", email)
            editor.commit()
        }
        fun getEmail(context: Context):String{
            val sp = context.getSharedPreferences("LetMeOut", Context.MODE_PRIVATE)
            return sp.getString("EMAIL", "")
        }
    }
}