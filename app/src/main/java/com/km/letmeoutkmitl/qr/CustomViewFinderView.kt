package com.km.letmeoutkmitl.qr

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import com.km.letmeoutkmitl.R
import me.dm7.barcodescanner.core.ViewFinderView

/**
 * Created by jongzazaal on 2/22/2018.
 */
class CustomViewFinderView : ViewFinderView {
    val PAINT = Paint()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        PAINT.color = ContextCompat.getColor(context, R.color.orange)

        PAINT.isAntiAlias = true
        val textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                TRADE_MARK_TEXT_SIZE_SP.toFloat(), resources.displayMetrics)
        PAINT.textSize = textPixelSize
        setSquareViewFinder(true)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawTradeMark(canvas)
    }

    private fun drawTradeMark(canvas: Canvas) {
        val framingRect = framingRect
        val tradeMarkTop: Float
        val tradeMarkLeft: Float
        if (framingRect != null) {
            tradeMarkTop = framingRect.bottom.toFloat() + PAINT.textSize + 10f
            tradeMarkLeft = framingRect.left.toFloat()
        } else {
            tradeMarkTop = 10f
            tradeMarkLeft = canvas.height.toFloat() - PAINT.textSize - 10f
        }
        canvas.drawText(TRADE_MARK_TEXT, tradeMarkLeft, tradeMarkTop, PAINT)
    }

    companion object {
        val TRADE_MARK_TEXT = ""
        val TRADE_MARK_TEXT_SIZE_SP = 40
    }
}