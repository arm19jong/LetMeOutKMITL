package com.km.letmeoutkmitl.firebase

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.km.letmeoutkmitl.user.User
import com.km.letmeoutkmitl.user.UserSP


/**
 * Created by jongzazaal on 2/15/2018.
 */
class ManageUser : ViewModel() {
    private val KEY_USER = "Users"
    private var mRootRef = FirebaseDatabase.getInstance().reference
    private var databaseUser = mRootRef.child(KEY_USER)
    private var addOrEditUserBool: MutableLiveData<Boolean>? = null
    private var user: MutableLiveData<User>? = null

    fun addOrEditUser(uid: String, user: User): LiveData<Boolean> {
        if (addOrEditUserBool == null) {
            addOrEditUserBool = MutableLiveData()
            databaseUser.child(uid).setValue(user)
                    .addOnCompleteListener {
                        addOrEditUserBool!!.value = true
                    }
                    .addOnFailureListener {
                        addOrEditUserBool!!.value = false
                    }
        }
        return addOrEditUserBool!!
    }

    fun getUser(uid: String): LiveData<User>{
        if(user == null){
            user = MutableLiveData()
            databaseUser.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    user!!.value = User()
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if(p0!!.value==null){
                        user!!.value = User()
                    }
                    else{
                        user!!.value = p0.getValue(User::class.java)
                    }
                }
            })

        }
        return user!!
    }
}