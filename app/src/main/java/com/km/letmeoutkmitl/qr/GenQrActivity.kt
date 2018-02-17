package com.km.letmeoutkmitl.qr

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.km.letmeoutkmitl.R
import com.km.letmeoutkmitl.user.UserSP
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.color.SimpleColorDialog
import kotlinx.android.synthetic.main.gen_qr_activity.*
import net.glxn.qrgen.android.QRCode

/**
 * Created by jongzazaal on 2/16/2018.
 */
class GenQrActivity :AppCompatActivity(),  SimpleDialog.OnDialogResultListener {
    var onColor:Int = 0xFF000000.toInt()
    var offColor:Int = 0xFFBDBDBD.toInt()
    var uid:String = ""

    override fun onResult(dialogTag: String, which: Int, extras: Bundle): Boolean {

        if (which != -1){
            return false
        }
        if(dialogTag=="123"){
            onColor = extras.getInt(SimpleColorDialog.COLOR)
            pic.setImageBitmap(genQr(onColor, offColor))
            buttonIn.setBackgroundColor(onColor)
        }
        else if (dialogTag=="456"){
            offColor = extras.getInt(SimpleColorDialog.COLOR)
            pic.setImageBitmap(genQr(onColor, offColor))
            buttonOut.setBackgroundColor(offColor)
        }

        return true

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gen_qr_activity)
        uid = UserSP.getUid(this)
        buttonIn.setOnClickListener {
            selectColor("123", "Color In")
        }
        buttonOut.setOnClickListener {
            selectColor("456", "Color Out")
        }
        buttonIn.setBackgroundColor(onColor)
        buttonOut.setBackgroundColor(offColor)
        pic.setImageBitmap(genQr(onColor, offColor))
    }

    fun selectColor(tag:String, title:String) {
        SimpleColorDialog.build()
                .title(title)
                .colorPreset(Color.RED)
                .allowCustom(true)
                .show(this, tag)
    }

    fun genQr(onColor: Int, offColor: Int): Bitmap {
        val myBitmap = QRCode.from(uid).withSize(300, 300)
                .withColor(onColor, offColor).bitmap()
        return myBitmap
    }
}