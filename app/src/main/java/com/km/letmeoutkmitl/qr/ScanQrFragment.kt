package com.km.letmeoutkmitl.qr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.zxing.Result
import com.km.letmeoutkmitl.MainActivityy
import com.km.letmeoutkmitl.R
import com.km.letmeoutkmitl.baseclass.BaseFragment
import com.km.letmeoutkmitl.user.ShowProfileActivity
import kotlinx.android.synthetic.main.scan_qr_fragment.view.*
import me.dm7.barcodescanner.core.IViewFinder
import me.dm7.barcodescanner.zxing.ZXingScannerView

/**
 * Created by jongzazaal on 2/22/2018.
 */
class ScanQrFragment:BaseFragment(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null
    private var flash:Boolean = false
    companion object Factory {
        fun newInstance(): ScanQrFragment {
            var arg = Bundle()
            var flagment = ScanQrFragment()
            flagment.arguments = arg
            return flagment
        }
    }

    fun setFlash(){
        flash = !flash
        if (flash){
            bindView!!.flash.background = ContextCompat.getDrawable(context!!, R.drawable.ic_flash_on)
        }
        else{
            bindView!!.flash.background = ContextCompat.getDrawable(context!!, R.drawable.ic_flash_off)
        }
        mScannerView!!.flash = flash
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.scan_qr_fragment, container, false)
        return view
    }

    var bindView:View? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView = view
        bindView!!.content_frame.addView(mScannerView)
        bindView!!.flash.setOnClickListener{
            setFlash()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView = object : ZXingScannerView(this.context) {
            override fun createViewFinderView(context: Context?): IViewFinder {
                return CustomViewFinderView(context!!)
            }
        }
    }


//        content_frame
//        mScannerView = object : ZXingScannerView(this) {
//            override fun createViewFinderView(context: Context?): IViewFinder {
//                return CustomViewFinderView(context)
//            }
//        }
//         content_frame.addView(mScannerView)


    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
//        Toast.makeText(this.context, "Contents = " + rawResult.text +
//                ", Format = " + rawResult.barcodeFormat.toString(), Toast.LENGTH_SHORT).show()

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
//        val handler = Handler()
//        handler.postDelayed({ mScannerView!!.resumeCameraPreview(this@ScanQrFragment) }, 2000)
        val intent = Intent(this.context, ShowProfileActivity::class.java)
        intent.putExtra("UID", rawResult.text)
        startActivity(intent)
    }
}