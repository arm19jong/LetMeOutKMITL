package com.km.letmeoutkmitl.qr

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.km.letmeoutkmitl.BuildConfig
import com.km.letmeoutkmitl.R
import com.km.letmeoutkmitl.baseclass.BaseFragment
import com.km.letmeoutkmitl.user.UserSP
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.color.SimpleColorDialog
import kotlinx.android.synthetic.main.gen_qr_fragment.view.*
import net.glxn.qrgen.android.QRCode
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jongzazaal on 2/16/2018.
 */
class GenQrFragment :BaseFragment(),  SimpleDialog.OnDialogResultListener {
    var onColor:Int = 0xFFE7E7E7.toInt()
    var offColor:Int = 0xFFFF6000.toInt()
    var uid:String = ""
    var bitmap:Bitmap? = null
    val BackgroundTag: String = "backgroundTag"
    val ForegroundTag: String = "foregroungTag"

    companion object Factory {
        fun newInstance(): GenQrFragment {
            var arg = Bundle()
            var flagment = GenQrFragment()
            flagment.arguments = arg
            return flagment
        }
    }
    override fun onResult(dialogTag: String, which: Int, extras: Bundle): Boolean {

        if (which != -1){
            return false
        }
        if(dialogTag==ForegroundTag){
            onColor = extras.getInt(SimpleColorDialog.COLOR)
            bindView!!.pic.setImageBitmap(genQr(onColor, offColor))
            bindView!!.buttonFourground.background.setColorFilter(onColor, PorterDuff.Mode.SRC_IN)
//            bindView!!.textIn.setTextColor(onColor)
        }
        else if (dialogTag==BackgroundTag){
            offColor = extras.getInt(SimpleColorDialog.COLOR)
            bindView!!.pic.setImageBitmap(genQr(onColor, offColor))
            bindView!!.buttonBackground.background.setColorFilter(offColor, PorterDuff.Mode.SRC_IN)
//            bindView!!.textOut.setTextColor(offColor)
        }

        return true

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.gen_qr_fragment, container, false)
        return view
    }

    var bindView:View? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView = view
        bindView!!.layoutForeground.setOnClickListener {
            selectColor(ForegroundTag, "Foreground Color")
        }
        bindView!!.layoutBackground.setOnClickListener {
            selectColor(BackgroundTag, "Background Color")
        }
        bindView!!.buttonFourground.background.setColorFilter(onColor, PorterDuff.Mode.SRC_IN)

        bindView!!.buttonBackground.background.setColorFilter(offColor, PorterDuff.Mode.SRC_IN)

        bindView!!.pic.setImageBitmap(genQr(onColor, offColor))
        bindView!!.layoutShare.setOnClickListener {
            try {
                val file = File(this.context!!.cacheDir, "qr.png")
                val fOut = FileOutputStream(file)
                bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                fOut.flush()
                fOut.close()
                file.setReadable(true, false)
                val intent = Intent(Intent.ACTION_SEND)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                val photoURI = FileProvider.getUriForFile(this.context!!,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file)
                intent.putExtra(Intent.EXTRA_STREAM, photoURI)
                intent.type = "image/png"
                startActivity(Intent.createChooser(intent, "Share image via"))
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
        bindView!!.layoutSave.setOnClickListener {
            Dexter.withActivity(this.activity)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            saveQR()
                        }

                        override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                            token!!.continuePermissionRequest()
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            Toast.makeText(context,"permission not granted",Toast.LENGTH_SHORT).show()
                        }
                    }).check()
        }


    }

    fun saveQR(){
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

            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush()
            fOut.close()

            MediaStore.Images.Media.insertImage(this.context!!.contentResolver, file.absolutePath, file.name, file.name)
            updateImage(file)
            Toast.makeText(this.context!!, getString(R.string.save_done), Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    fun updateImage(file: File) {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.fromFile(file)
        this.context!!.sendBroadcast(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = UserSP.getUid(this.context!!)

    }

    fun selectColor(tag:String, title:String) {
        SimpleColorDialog.build()
                .title(title)
                .colorPreset(Color.RED)
                .allowCustom(true)
                .show(this, tag)
    }

    fun genQr(onColor: Int, offColor: Int): Bitmap {
        val myBitmap = QRCode.from(uid).withSize(720, 720)
                .withColor(onColor, offColor).bitmap()
        this.bitmap = myBitmap
        return myBitmap
    }
}