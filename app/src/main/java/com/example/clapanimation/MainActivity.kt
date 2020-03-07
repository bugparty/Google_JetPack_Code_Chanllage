package com.example.clapanimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.graphics.Path
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.animation.PathInterpolatorCompat
import kotlin.math.pow


class MainActivity : AppCompatActivity() {
    var progress = 0f
    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private var zoomAnimator: Animator? = null
    private var circleAnimator: Animator? = null
    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private var shortAnimationDuration: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val clapView = findViewById<com.example.clapview.ClapView>(R.id.clapView)

        clapView.setOnClickListener {
            progress += 1f
            clapView.setProgress(progress)
            doZoomAnimation()
            doCircleAnimation()

        }

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    private fun doCircleAnimation() {
        circleAnimator?.cancel()
        val expandedImageView: ImageView = findViewById(R.id.expanded_image2)
        val x = expandedImageView.x
        val y = expandedImageView.y
        expandedImageView.visibility = View.VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val path = Path()
            path.arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true)
            circleAnimator = ObjectAnimator.ofFloat(expandedImageView, View.X, View.Y, path)
            circleAnimator?.duration = shortAnimationDuration.toLong()

        } else {
            val myInterpolator = PathInterpolatorCompat.create(0.4f, 0.5f)
            val animatorX = ObjectAnimator.ofFloat(expandedImageView, View.X, 400f).apply {
                duration = shortAnimationDuration.toLong()
                interpolator = myInterpolator
            }
            val animatorY = ObjectAnimator.ofFloat(expandedImageView, View.Y, 400f).apply {
                duration = shortAnimationDuration.toLong()


            }
            circleAnimator = AnimatorSet().apply {
                play(animatorX).with(animatorY)
            }
        }

        circleAnimator?.apply {
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    expandedImageView.visibility = View.INVISIBLE
                    expandedImageView.x = x
                    expandedImageView.y = y
                    circleAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    expandedImageView.visibility = View.INVISIBLE
                    expandedImageView.x = x
                    expandedImageView.y = y
                    circleAnimator = null
                }
            })
            start()
        }
    }

    private fun doZoomAnimation() {
        zoomAnimator?.cancel()
        val expandedImageView: ImageView = findViewById(R.id.expanded_image)
        expandedImageView.visibility = View.VISIBLE
        val animatorZoomOutX = ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, 5f)
        val animatorZoomOutY = ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, 5f)
        val animatorZoomInX = ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, 1f)
        val animatorZoomInY = ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, 1f)

        zoomAnimator = AnimatorSet().apply {
            play(animatorZoomOutX).with(animatorZoomOutY)
            play(animatorZoomInX).with(animatorZoomInY).after(animatorZoomOutX)
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    expandedImageView.visibility = View.INVISIBLE
                    zoomAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    expandedImageView.visibility = View.INVISIBLE
                    zoomAnimator = null
                }
            })
            start()
        }
    }

}
