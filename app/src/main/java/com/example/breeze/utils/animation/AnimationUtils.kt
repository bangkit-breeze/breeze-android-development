package com.example.breeze.utils.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

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

    val party = Party(
        speed = 0f,
        maxSpeed = 30f,
        damping = 0.9f,
        spread = 360,
        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
        emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
        position = Position.Relative(0.5, 0.3)
    )

}
