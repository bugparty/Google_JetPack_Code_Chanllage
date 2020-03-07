package com.example.clapview

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Property
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout

class ClapView : FrameLayout {
    private val mDrawable: ClapDrawable
    private val mPaint = Paint()
    private val mStokeBackgroundColor: Int
    private val mClapBackgroundColor: Int
    private var mBackgroundColor = 0
    private var mWidth = 0
    private var mHeight = 0
    private var mTotalProgress  = 0

    constructor(context: Context) : super(context) {
        mClapBackgroundColor = Color.BLUE
        mStokeBackgroundColor = Color.CYAN
        mDrawable = ClapDrawable(resources)
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ClapView,
            0, 0
        )
        try {
            mClapBackgroundColor =
                a.getColor(R.styleable.ClapView_bg_color, Color.RED)
            mStokeBackgroundColor =
                a.getColor(R.styleable.ClapView_stoke_color, Color.CYAN)
            mTotalProgress = a.getInt(R.styleable.ClapView_total_progress, 100)

        } finally {
            a.recycle()
        }
        mDrawable = ClapDrawable(resources)
        init()
    }

    private fun init() {
        setWillNotDraw(false)
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL
        mDrawable.callback = this
        mBackgroundColor = mClapBackgroundColor
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = Math.min(width, height)
        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        )

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mDrawable.setBounds(0, 0, w, h)
        mWidth = w
        mHeight = h
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outlineProvider = object : ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                override fun getOutline(
                    view: View,
                    outline: Outline
                ) {
                    outline.setOval(0, 0, view.width, view.height)
                }
            }
            clipToOutline = true
        }
    }

    var color: Int
        get() = mBackgroundColor
        set(color) {
            mBackgroundColor = color
            invalidate()
        }

    override fun verifyDrawable(who: Drawable): Boolean {
        return who === mDrawable || super.verifyDrawable(who)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mDrawable.draw(canvas)

    }

    fun setProgress(progress: Float){
        if (progress > 0 && progress <= mTotalProgress )
            mDrawable.clapProgress = progress
    }

    companion object {
        private val COLOR: Property<ClapView, Int> =
            object : Property<ClapView, Int>(
                Int::class.java, "color"
            ) {
                override fun get(v: ClapView): Int {
                    return v.color
                }

                override fun set(v: ClapView, value: Int) {
                    v.color = value
                }
            }
        private const val PLAY_PAUSE_ANIMATION_DURATION: Long = 200
    }
}