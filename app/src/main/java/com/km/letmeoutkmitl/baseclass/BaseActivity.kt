package com.km.letmeoutkmitl.baseclass

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.km.letmeoutkmitl.R
import android.widget.Toast
import android.view.MenuInflater





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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main_actions, menu)

        return super.onCreateOptionsMenu(menu)
    }
}