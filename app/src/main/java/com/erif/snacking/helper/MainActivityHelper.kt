package com.erif.snacking.helper

import android.content.Context
import android.view.View
import android.widget.Toast
import com.erif.snacking.R
import com.erif.snacking.library.Snacking
import com.erif.snacking.library.SnackingState
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton


class MainActivityHelper(parentView: View, private var fab: ExtendedFloatingActionButton?) {

    private var parentView: View? = parentView
    private var context: Context? = null

    init {
        context = parentView.context
    }

    fun snackBarBasic() {
        parentView?.let {
            Snacking.Builder(it, "Hello! this is basic message")
                .build().show()
        }
    }

    fun snackBarIcon() {
       parentView?.let {
           Snacking.Builder(it, "This message with icon")
               .icon(R.drawable.ic_info, R.color.teal_200)
               .build().show()
       }
    }

    fun snackBarAction() {
        parentView?.let {
            Snacking.Builder(it, "Click to dismiss message")
                .action("Dismiss", R.color.teal_200) { snackBar ->
                    snackBar.dismiss()
                    toast("Action Click")
                }
                .build().show()
        }
    }

    fun snackBarCorner() {
        parentView?.let {
            Snacking.Builder(it, "This message with corner")
                .cornerRadius(R.dimen.snack_bar_corner_radius)
                .build().show()
        }
    }

    fun snackBarCornerCustom() {
        parentView?.let {
            Snacking.Builder(it, "This message with custom corner")
                .useMargin(true)
                .cornerRadius(R.dimen.snack_bar_corner_radius, 0, 0, R.dimen.snack_bar_corner_radius)
                .build()
                .show()
        }
    }

    fun snackBarMargin() {
        parentView?.let {
            Snacking.Builder(it, "This message with margin")
                .useMargin(true)
                .build().show()
        }
    }

    fun snackBarBackground() {
        parentView?.let {
            Snacking.Builder(it, "This is custom background color")
                .backgroundColor(R.color.purple_200)
                .build()
                .show()
        }
    }

    fun snackBarBackgroundImage() {
        parentView?.let {
            Snacking.Builder(it, "Custom background image, border, corner, padding")
                .background(R.mipmap.img_gradient)
                .useMargin(true)
                .cornerRadius(
                    R.dimen.snack_bar_corner_radius_small,
                    0,
                    0,
                    R.dimen.snack_bar_corner_radius_small
                )
                .textStyle(Snacking.BOLD_ITALIC)
                .border(R.dimen.snack_bar_border_size_xLarge, R.color.purple_500)
                .build()
                .show()
        }
    }

    fun snackBarBorder() {
        parentView?.let {
            Snacking.Builder(it, "This message with border")
                .border(R.dimen.snack_bar_border_size, R.color.teal_700)
                .useMargin(true)
                .cornerRadius(R.dimen.snack_bar_corner_radius_small)
                .build()
                .show()
        }
    }

    fun snackBarTextColor() {
        parentView?.let {
            Snacking.Builder(it, "This is custom text color")
                .textColor("#ffca28")
                .build()
                .show()
        }
    }

    fun snackBarFont() {
        parentView?.let {
            Snacking.Builder(it, "This is custom font family")
                .fontFamily(R.font.montserrat)
                .build()
                .show()
        }
    }

    fun snackBarBold() {
        parentView?.let {
            Snacking.Builder(it, "This is bold italic text")
                .fontFamily(R.font.montserrat)
                .textStyle(Snacking.BOLD_ITALIC)
                .build()
                .show()
        }
    }

    fun snackBarAnchor() {
        parentView?.let {
            Snacking.Builder(it, "This message with anchor view")
                .anchorView(fab)
                .build()
                .show()
        }
    }

    fun snackBarPosition() {
        parentView?.let {
            Snacking.State(it, SnackingState.WARNING)
                .message("This message is on top position")
                .icon(R.drawable.ic_info)
                .position(Snacking.TOP)
                .useMargin(true)
                .cornerRadius(R.dimen.snack_bar_corner_radius_large)
                .border(R.dimen.snack_bar_border_size)
                .action("Cancel") { toast("Action Click") }
                .show()
        }
    }

    fun snackBarState() {
        parentView?.let {
            Snacking.State(it, Snacking.State.INFO)
                .message("This message is using state")
                .icon(R.drawable.ic_info)
                .useMargin(true)
                .cornerRadius(R.dimen.snack_bar_corner_radius_small)
                .action("CLOSE") { snackBar: Snacking ->
                    toast("Action Clicked")
                    snackBar.dismiss()
                }
                .show()
        }
    }

    fun snackBarMaxLines() {
        parentView?.let {
            Snacking.Builder(
                it,
                "This is long message, this is long message, this is long message, this is long message, this is long message, this is long message"
            )
                .action("LONG BUTTON TEXT") { toast("Action Click") }
                .messageMaxLines(2)
                .build()
                .show()
        }
    }

    fun snackBarLandscape() {
        parentView?.let {
            Snacking.State(it, Snacking.State.INFO)
                .message("This message is on landscape screen")
                .icon(R.drawable.ic_info)
                .useMargin(true)
                .cornerRadius(R.dimen.snack_bar_corner_radius_small)
                .action("CLOSE") { snackBar: Snacking ->
                    toast("Action Click")
                    snackBar.dismiss()
                }
                .landscapeStyle(Snacking.CENTER)
                .border(R.dimen.snack_bar_border_size)
                .show()
        }
    }

    private fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}