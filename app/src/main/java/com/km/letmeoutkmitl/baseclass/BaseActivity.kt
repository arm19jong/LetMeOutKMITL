package com.km.letmeoutkmitl.baseclass

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.km.letmeoutkmitl.R

/**
 * Created by jongzazaal on 2/21/2018.
 */
open class BaseActivity: AppCompatActivity() {

    fun pushFragment(fragment:BaseFragment){
        val transation = supportFragmentManager
                .beginTransaction()

        transation.replace(R.id.fragment, fragment)
//        transation.addToBackStack(null)
        transation.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}