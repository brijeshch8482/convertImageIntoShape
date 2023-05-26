package com.example.celebrare


import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.squareup.picasso.Transformation


class MaskTransformation(context: Context, maskId: Int) :
    Transformation {
    private val mContext: Context
    private val mMaskId: Int

    companion object {
        private val mMaskingPaint = Paint()

        init {
            mMaskingPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        }
    }

    override fun transform(source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val mask = getMaskDrawable(mContext, mMaskId)
        val canvas = Canvas(result)
        mask.setBounds(0, 0, width, height)
        mask.draw(canvas)
        canvas.drawBitmap(source, 0f, 0f, mMaskingPaint)
        source.recycle()
        return result
    }

    override fun key(): String {
        return ("MaskTransformation(maskId=" + mContext.resources.getResourceEntryName(mMaskId)
                + ")")
    }

    private fun getMaskDrawable(context: Context?, maskId: Int): Drawable {
        return ContextCompat.getDrawable(context!!, maskId)
            ?: throw IllegalArgumentException("maskId is invalid")
    }

    /**
     * @param maskId
     */
    init {
        mContext = context.applicationContext
        mMaskId = maskId
    }
}