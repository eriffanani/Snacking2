package com.erif.snacking.library;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SnackingState {

    private final Snacking snackBar;

    @IntDef({SUCCESS, FAILED, WARNING, INFO, DISABLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface States {}
    public static final int SUCCESS = 0;
    public static final int FAILED = 1;
    public static final int WARNING = 2;
    public static final int INFO = 3;
    public static final int DISABLE = 4;

    private int currentState = SUCCESS;
    private final View parentView;

    public SnackingState(@NonNull View parentView, @States int state) {
        this.parentView = parentView;
        this.currentState = state;
        snackBar = new Snacking(parentView);
        state(currentState);
    }

    private void state(@States int state) {
        this.currentState = state;
        int color = R.color.quick_snack_bar_state_success_background;
        if (state == DISABLE) {
            color = R.color.quick_snack_bar_state_disable_background;
        } else if (state == INFO) {
            color = R.color.quick_snack_bar_state_info_background;
        } else if (state == WARNING) {
            color = R.color.quick_snack_bar_state_warning_background;
        } else if (state == FAILED) {
            color = R.color.quick_snack_bar_state_failed_background;
        }
        snackBar.backgroundColor(color);
        snackBar.textColor(getTextColor());
    }

    public SnackingState message(@StringRes int messageRes) {
        snackBar.message(messageRes);
        return this;
    }

    public SnackingState message(String message) {
        snackBar.message(message);
        return this;
    }

    public SnackingState messageMaxLines(int lines) {
        snackBar.messageMaxLines(lines);
        return this;
    }

    public SnackingState icon(@DrawableRes int iconRes) {
        int color = R.color.quick_snack_bar_state_success_icon;
        if (currentState == DISABLE) {
            color = R.color.quick_snack_bar_state_disable_icon;
        } else if (currentState == INFO) {
            color = R.color.quick_snack_bar_state_info_icon;
        } else if (currentState == WARNING) {
            color = R.color.quick_snack_bar_state_warning_icon;
        } else if (currentState == FAILED) {
            color = R.color.quick_snack_bar_state_failed_icon;
        }
        snackBar.icon(iconRes, color);
        return this;
    }

    public SnackingState action(@StringRes int actionTextRes, @NonNull Snacking.Callback callback) {
        int color = R.color.quick_snack_bar_state_success_action;
        if (currentState == DISABLE) {
            color = R.color.quick_snack_bar_state_disable_action;
        } else if (currentState == INFO) {
            color = R.color.quick_snack_bar_state_info_action;
        } else if (currentState == WARNING) {
            color = R.color.quick_snack_bar_state_warning_action;
        } else if (currentState == FAILED) {
            color = R.color.quick_snack_bar_state_failed_action;
        }
        snackBar.action(actionTextRes, color, callback);
        return this;
    }

    public SnackingState action(@NonNull String actionText, @NonNull Snacking.Callback callback) {
        int color = R.color.quick_snack_bar_state_success_action;
        if (currentState == DISABLE) {
            color = R.color.quick_snack_bar_state_disable_action;
        } else if (currentState == INFO) {
            color = R.color.quick_snack_bar_state_info_action;
        } else if (currentState == WARNING) {
            color = R.color.quick_snack_bar_state_warning_action;
        } else if (currentState == FAILED) {
            color = R.color.quick_snack_bar_state_failed_action;
        }
        snackBar.action(actionText, color, callback);
        return this;
    }

    public SnackingState cornerRadius(@DimenRes int cornerRadiusRes) {
        snackBar.cornerRadius(cornerRadiusRes);
        return this;
    }

    public SnackingState cornerRadius(float cornerRadius) {
        snackBar.cornerRadius(cornerRadius);
        return this;
    }

    public SnackingState cornerRadius(
            float topLeft, float topRight,
            float bottomLeft, float bottomRight
    ) {
        snackBar.cornerRadius(
                topLeft, topRight, bottomLeft, bottomRight
        );
        return this;
    }

    public SnackingState cornerRadius(
            @DimenRes int topLeftRes,
            @DimenRes int topRightRes,
            @DimenRes int bottomLeftRes,
            @DimenRes int bottomRightRes
    ) {
        snackBar.cornerRadius(
                topLeftRes, topRightRes, bottomLeftRes, bottomRightRes
        );
        return this;
    }

    public SnackingState border(@DimenRes int borderRes) {
        int color = R.color.quick_snack_bar_state_success_border;
        if (currentState == DISABLE) {
            color = R.color.quick_snack_bar_state_disable_border;
        } else if (currentState == INFO) {
            color = R.color.quick_snack_bar_state_info_border;
        } else if (currentState == WARNING) {
            color = R.color.quick_snack_bar_state_warning_border;
        } else if (currentState == FAILED) {
            color = R.color.quick_snack_bar_state_failed_border;
        }
        snackBar.border(borderRes, color);
        return this;
    }

    public SnackingState border(float borderSize) {
        int id = R.color.quick_snack_bar_state_success_border;
        if (currentState == DISABLE) {
            id = R.color.quick_snack_bar_state_disable_border;
        } else if (currentState == INFO) {
            id = R.color.quick_snack_bar_state_info_border;
        } else if (currentState == WARNING) {
            id = R.color.quick_snack_bar_state_warning_border;
        } else if (currentState == FAILED) {
            id = R.color.quick_snack_bar_state_failed_border;
        }
        int getColor = getColor(id);
        if (getColor != 0)
            snackBar.border(borderSize, "#" + Integer.toHexString(getColor));
        return this;
    }

    public SnackingState useMargin(boolean isUseMargin) {
        snackBar.useMargin(isUseMargin);
        return this;
    }

    public SnackingState heightRes(@DimenRes int heightRes) {
        snackBar.heightRes(heightRes);
        return this;
    }

    public SnackingState height(int height) {
        snackBar.height(height);
        return this;
    }

    public SnackingState setAnchorView(View anchorView) {
        snackBar.anchorView(anchorView);
        return this;
    }

    public SnackingState duration(@Snacking.Duration int duration) {
        snackBar.duration(duration);
        return this;
    }

    public SnackingState fontFamily(@FontRes int fontRes) {
        snackBar.fontFamily(fontRes);
        return this;
    }

    public SnackingState fontFamily(@FontRes int fontRes, @DimenRes int fontSizeRes) {
        snackBar.fontFamily(fontRes, fontSizeRes);
        return this;
    }

    public SnackingState fontFamily(@FontRes int fontRes, float fontSize) {
        snackBar.fontFamily(fontRes, fontSize);
        return this;
    }

    public SnackingState position(@Snacking.Position int position) {
        snackBar.position(position);
        return this;
    }

    public SnackingState landscapeStyle(@Snacking.LandscapeStyle int landscapeStyle) {
        snackBar.landscapeStyle(landscapeStyle);
        return this;
    }

    public SnackingState landscapeWidthRes(@DimenRes int landscapeWidthRes) {
        snackBar.landscapeWidthRes(landscapeWidthRes);
        return this;
    }

    public SnackingState landscapeWidth(int landscapeWidth) {
        snackBar.landscapeWidth(landscapeWidth);
        return this;
    }

    public SnackingState swipeToDismiss(boolean swipeToDismiss) {
        snackBar.swipeToDismiss(swipeToDismiss);
        return this;
    }

    public void show() {
        snackBar.show();
    }

    /*private int getTextColor() {
        return currentState == FAILED ? R.color.quick_snack_bar_color_white : R.color.quick_snack_bar_color_black;
    }*/

    private int getTextColor() {
        return R.color.quick_snack_bar_color_black;
    }

    private int getColor(int id) {
        int finalColor = 0;
        try {
            finalColor = ContextCompat.getColor(parentView.getContext(), id);
        } catch (Resources.NotFoundException e) {
            Log.d("QuickSnackBar","Color resource not found");
        }
        return finalColor;
    }

}
