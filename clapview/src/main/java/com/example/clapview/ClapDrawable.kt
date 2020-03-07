package com.example.clapview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Property
import androidx.annotation.ColorInt
import androidx.annotation.NonNull


class ClapDrawable(@NonNull resources: Resources) : Drawable() {

    private val mPaint = Paint()
    private val mBounds = RectF()
    private var mWidth = 0f
    private var mHeight = 0f
    private var mClapProgress = 0f
    private var mResources: Resources
    private var mBitmap: Bitmap? = null
    private var mStokePaint: Paint = Paint()
    private var mStokeRect: RectF = RectF()
    private val growSpeed = 2f

    private val clapSize = 120
    private val blackCircleRadius = 85f

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        mBounds.set(bounds)
        mWidth = mBounds.width()
        mHeight = mBounds.height()

    }
    private fun initDrawRes(){
        mStokePaint.style = Paint.Style.STROKE
        mStokePaint.color = Color.BLUE
        mStokePaint.strokeWidth = 15f
        mStokePaint.isAntiAlias = true
        val b = BitmapFactory.decodeResource(mResources, R.drawable.clap)
        mBitmap = Bitmap.createScaledBitmap(b, clapSize, clapSize, false)

    }
    override fun draw(@NonNull canvas: Canvas) {
        val y = (bounds.height() - mBitmap!!.height) / 2f
        val x  = (bounds.width() - mBitmap!!.width) / 2f
        val paint = Paint()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        val distance = 18f + mClapProgress*growSpeed
        mStokeRect.left  = x-distance
        mStokeRect.top = y-distance
        mStokeRect.right = x+clapSize +distance
        mStokeRect.bottom = y+clapSize +distance
        canvas.drawArc(mStokeRect,
            0f, 360f, true, mStokePaint)
        canvas.drawCircle(x+clapSize/2, y+clapSize/2 , blackCircleRadius, paint)
        canvas.drawBitmap(mBitmap!!, x,y, paint)

    }

    var clapProgress : Float
        get() = mClapProgress
        set(progress){
            mClapProgress = progress
            invalidateSelf()
        }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint.colorFilter = cf
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }


    init {

        mResources = resources
        initDrawRes()
    }
}