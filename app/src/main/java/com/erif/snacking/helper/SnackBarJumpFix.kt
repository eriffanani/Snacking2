package com.erif.snacking.helper

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar.SnackbarLayout


class SnackBarJumpFix: CoordinatorLayout.Behavior<View> {

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs) {

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
        val translationY = Math.min(0f, dependency.translationY - dependency.height)
        if (dependency.translationY != 0f) {
            child.translationY = translationY
        }
        return true
    }

}