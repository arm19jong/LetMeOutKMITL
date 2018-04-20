package com.km.letmeoutkmitl.comment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.km.letmeoutkmitl.R
import com.km.letmeoutkmitl.baseclass.BaseFragment
import com.km.letmeoutkmitl.firebase.ManageUser
import com.km.letmeoutkmitl.user.Comment
import com.km.letmeoutkmitl.user.UserSP
import kotlinx.android.synthetic.main.comment_fragment.*

/**
 * Created by jongzazaal on 4/20/2018.
 */
class CommentFragment:BaseFragment() {
    companion object Factory {
        fun newInstance(): CommentFragment {
            var arg = Bundle()
            var flagment = CommentFragment()
            flagment.arguments = arg
            return flagment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.comment_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = UserSP.getUid(this.context!!)

        comment_button.setOnClickListener{

            ViewModelProviders.of(this)
                    .get(ManageUser::class.java)
                    .sendComment(uid, Comment(comment.text.toString()))
                    .observe(this, Observer {
                        if (it==true){
                            Toast.makeText(context, "ส่งเรียบร้อย", Toast.LENGTH_SHORT).show()
                            comment.text.clear()
                        }
                        else{
                            Toast.makeText(context, "Send Failed", Toast.LENGTH_SHORT).show()
                        }
                    })
        }
    }
}