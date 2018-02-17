package com.km.letmeoutkmitl.qr

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.km.letmeoutkmitl.BuildConfig
import com.km.letmeoutkmitl.R
import com.km.letmeoutkmitl.user.UserSP
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.color.SimpleColorDialog
import kotlinx.android.synthetic.main.gen_qr_activity.*
import net.glxn.qrgen.android.QRCode
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jongzazaal on 2/16/2018.
 */
class GenQrActivity :AppCompatActivity(),  SimpleDialog.OnDialogResultListener {
    var onColor:Int = 0xFF000000.toInt()
    var offColor:Int = 0xFFBDBDBD.toInt()
    var uid:String = ""
    var bitmap:Bitmap? = null

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
        share.setOnClickListener {
            try {
                val file = File(this.cacheDir, "logicchip.png")
                val fOut = FileOutputStream(file)
                bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                fOut.flush()
                fOut.close()
                file.setReadable(true, false)
                val intent = Intent(Intent.ACTION_SEND)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                val photoURI = FileProvider.getUriForFile(this@GenQrActivity,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file)
                intent.putExtra(Intent.EXTRA_STREAM, photoURI)
                intent.type = "image/png"
                startActivity(Intent.createChooser(intent, "Share image via"))
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
        save.setOnClickListener {
            val filename: String
            val date = Date()
            val sdf = SimpleDateFormat("yyyyMMddHHmmss")
            filename = sdf.format(date)

            try {
                val path = Environment.getExternalStorageDirectory().toString()
                var fOut: OutputStream? = null
                val file = File(path+"/DCIM/LetMeOutKMITL/", "$filename.jpg")
                val folder = File(path+"/DCIM/", "LetMeOutKMITL")
                if (!folder.exists()) {
                    folder.mkdirs()
                }
                fOut = FileOutputStream(file)

                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
                fOut!!.flush()
                fOut!!.close()

                MediaStore.Images.Media.insertImage(contentResolver, file.getAbsolutePath(), file.getName(), file.getName())
                Toast.makeText(this, "save done", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
        this.bitmap = myBitmap
        return myBitmap
    }
}