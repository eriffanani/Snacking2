package com.erif.snacking.helper

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewPropertyAnimator
import androidx.annotation.Dimension
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.snackbar.Snackbar.SnackbarLayout


class ScrollAwareBehavior: CoordinatorLayout.Behavior<View> {

    protected val ENTER_ANIMATION_DURATION = 225
    protected val EXIT_ANIMATION_DURATION = 175

    private val STATE_SCROLLED_DOWN = 1
    private val STATE_SCROLLED_UP = 2

    private var height = 0
    private var currentState = STATE_SCROLLED_UP
    private var additionalHiddenOffsetY = 0

    private var currentAnimator: ViewPropertyAnimator? = null

    constructor() {}

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs) {

    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        val paramsCompat = child.layoutParams as MarginLayoutParams
        height = child.measuredHeight + paramsCompat.bottomMargin
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    /**
     * Sets an additional offset for the y position used to hide the view.
     *
     * @param child the child view that is hidden by this behavior
     * @param offset the additional offset in pixels that should be added when the view slides away
     */
    fun setAdditionalHiddenOffsetY(child: View, @Dimension offset: Int) {
        additionalHiddenOffsetY = offset
        if (currentState == STATE_SCROLLED_DOWN) {
            child.translationY = (height + additionalHiddenOffsetY).toFloat()
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: View,
        directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int
    ): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyConsumed > 0) {
            slideDown(child)
        } else if (dyConsumed < 0) {
            slideUp(child)
        }
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is SnackbarLayout
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: View, dependency: View) {
        super.onDependentViewRemoved(parent, child, dependency)
        child.translationY = 0f
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (dependency is SnackbarLayout) {
            Log.d(
                "QuickSnackBar",
                "Y: " + dependency.getTranslationY() + " Height: " + dependency.getHeight()
            )
            Log.d("QuickSnackBar", "ChildY: " + child.translationY)
            val translationY = Math.min(0f, dependency.getTranslationY() - dependency.getHeight())
            if (dependency.getTranslationY() != 0f) {
                child.translationY = translationY
            }
            return true
        }
        return false
    }

    /** Returns true if the current state is scrolled up.  */
    fun isScrolledUp(): Boolean {
        return currentState == STATE_SCROLLED_UP
    }

    /**
     * Performs an animation that will slide the child from it's current position to be totally on the
     * screen.
     */
    fun slideUp(child: View) {
        slideUp(child,  /*animate=*/true)
    }

    /**
     * Slides the child with or without animation from its current position to be totally on the
     * screen.
     *
     * @param animate `true` to slide with animation.
     */
    @SuppressLint("RestrictedApi")
    fun slideUp(child: View, animate: Boolean) {
        if (isScrolledUp()) {
            return
        }
        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            child.clearAnimation()
        }
        currentState = STATE_SCROLLED_UP
        val targetTranslationY = 0
        if (animate) {
            animateChildTo(
                child,
                targetTranslationY,
                ENTER_ANIMATION_DURATION.toLong(),
                AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR
            )
        } else {
            child.translationY = targetTranslationY.toFloat()
        }
    }

    /** Returns true if the current state is scrolled down.  */
    fun isScrolledDown(): Boolean {
        return currentState == STATE_SCROLLED_DOWN
    }

    /**
     * Performs an animation that will slide the child from it's current position to be totally off
     * the screen.
     */
    fun slideDown(child: View) {
        slideDown(child,  /*animate=*/true)
    }

    /**
     * Slides the child with or without animation from its current position to be totally off the
     * screen.
     *
     * @param animate `true` to slide with animation.
     */
    @SuppressLint("RestrictedApi")
    fun slideDown(child: View, animate: Boolean) {
        if (isScrolledDown()) {
            return
        }
        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            child.clearAnimation()
        }
        currentState = STATE_SCROLLED_DOWN
        val targetTranslationY = height + additionalHiddenOffsetY
        if (animate) {
            animateChildTo(
                child,
                targetTranslationY,
                EXIT_ANIMATION_DURATION.toLong(),
                AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR
            )
        } else {
            child.translationY = targetTranslationY.toFloat()
        }
    }

    private fun animateChildTo(
        child: View, targetY: Int, duration: Long, interpolator: TimeInterpolator
    ) {
        currentAnimator = child
            .animate()
            .translationY(targetY.toFloat())
            .setInterpolator(interpolator)
            .setDuration(duration)
            .setListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        currentAnimator = null
                    }
                }
            )
    }

}