package com.example.breeze.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

object AnimationUtils{
    fun playSequentialFadeInAnimations(vararg views: View, duration: Long, startDelay: Long = 0) {
        val fadeInAnimators = views.map { createFadeInAnimator(it, duration) }

        AnimatorSet().apply {
            playSequentially(*fadeInAnimators.toTypedArray())
            this.startDelay = startDelay
        }.start()
    }

    private fun createFadeInAnimator(view: View, duration: Long): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 1f).setDuration(duration)
    }
}
