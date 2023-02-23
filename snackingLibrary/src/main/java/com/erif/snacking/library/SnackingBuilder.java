package com.erif.snacking.library;

import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public class SnackingBuilder {

    private final Snacking snackBar;

    public SnackingBuilder(@NonNull View parentView) {
        snackBar = new Snacking(parentView);
    }

    public SnackingBuilder(@NonNull View parentView, String message) {
        snackBar = new Snacking(parentView, message);
    }

    public SnackingBuilder(@NonNull View parentView, String message, @DrawableRes int iconRes) {
        snackBar = new Snacking(parentView, message, iconRes);
    }

    public SnackingBuilder message(@StringRes int messageRes) {
        snackBar.message(messageRes);
        return this;
    }

    public SnackingBuilder message(String message) {
        snackBar.message(message);
        return this;
    }

    public SnackingBuilder messageMaxLines(int lines) {
        snackBar.messageMaxLines(lines);
        return this;
    }

    public SnackingBuilder textColor(@ColorRes int colorRes) {
        snackBar.textColor(colorRes);
        return this;
    }

    public SnackingBuilder textColor(@NonNull String colorCode) {
        snackBar.textColor(colorCode);
        return this;
    }

    public SnackingBuilder icon(@DrawableRes int iconRes) {
        snackBar.icon(iconRes);
        return this;
    }

    public SnackingBuilder icon(@DrawableRes int iconRes, @ColorRes int colorRes) {
        snackBar.icon(iconRes, colorRes);
        return this;
    }

    public SnackingBuilder icon(@DrawableRes int iconRes, @NonNull String colorCode) {
        snackBar.icon(iconRes, colorCode);
        return this;
    }

    public SnackingBuilder background(@DrawableRes int drawableRes) {
        snackBar.background(drawableRes);
        return this;
    }

    public SnackingBuilder action(@StringRes int actionTextRes, @NonNull Snacking.Callback callback) {
        snackBar.action(actionTextRes, callback);
        return this;
    }

    public SnackingBuilder action(@NonNull String actionText, @NonNull Snacking.Callback callback) {
        snackBar.action(actionText, callback);
        return this;
    }

    public SnackingBuilder action(@NonNull String actionText, @ColorRes int textColorRes, @NonNull Snacking.Callback callback) {
        snackBar.action(actionText, textColorRes, callback);
        return this;
    }

    public SnackingBuilder action(@StringRes int actionTextRes, @ColorRes int textColorRes, @NonNull Snacking.Callback callback) {
        snackBar.action(actionTextRes, textColorRes, callback);
        return this;
    }

    public SnackingBuilder action(@NonNull String actionText, @NonNull String textColorCode, @NonNull Snacking.Callback callback) {
        snackBar.action(actionText, textColorCode, callback);
        return this;
    }

    public SnackingBuilder action(@StringRes int actionTextRes, @NonNull String textColorCode, @NonNull Snacking.Callback callback) {
        snackBar.action(actionTextRes, textColorCode, callback);
        return this;
    }

    public SnackingBuilder cornerRadius(@DimenRes int cornerRadiusRes) {
        snackBar.cornerRadius(cornerRadiusRes);
        return this;
    }

    public SnackingBuilder cornerRadius(float cornerRadius) {
        snackBar.cornerRadius(cornerRadius);
        return this;
    }

    public SnackingBuilder cornerRadius(
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

    public SnackingBuilder cornerRadius(
            float topLeft, float topRight,
            float bottomLeft, float bottomRight
    ) {
        snackBar.cornerRadius(
                topLeft, topRight, bottomLeft, bottomRight
        );
        return this;
    }

    public SnackingBuilder border(@DimenRes int borderRes, @ColorRes int colorRes) {
        snackBar.border(borderRes, colorRes);
        return this;
    }

    public SnackingBuilder border(float borderSize, @NonNull String colorCode) {
        snackBar.border(borderSize, colorCode);
        return this;
    }

    public SnackingBuilder backgroundColor(@ColorRes int colorRes) {
        snackBar.backgroundColor(colorRes);
        return this;
    }

    public SnackingBuilder backgroundColor(@NonNull String colorCode) {
        snackBar.backgroundColor(colorCode);
        return this;
    }

    public SnackingBuilder useMargin(boolean isUseMargin) {
        snackBar.useMargin(isUseMargin);
        return this;
    }

    public SnackingBuilder heightRes(@DimenRes int heightRes) {
        snackBar.heightRes(heightRes);
        return this;
    }

    public SnackingBuilder height(int height) {
        snackBar.height(height);
        return this;
    }

    public SnackingBuilder anchorView(View anchorView) {
        snackBar.anchorView(anchorView);
        return this;
    }

    public SnackingBuilder duration(@Snacking.Duration int duration) {
        snackBar.duration(duration);
        return this;
    }

    public SnackingBuilder fontFamily(@FontRes int fontRes) {
        snackBar.fontFamily(fontRes);
        return this;
    }

    public SnackingBuilder fontFamily(@FontRes int fontRes, @DimenRes int fontSizeRes) {
        snackBar.fontFamily(fontRes, fontSizeRes);
        return this;
    }

    public SnackingBuilder fontFamily(@FontRes int fontRes, float fontSize) {
        snackBar.fontFamily(fontRes, fontSize);
        return this;
    }

    public SnackingBuilder position(@Snacking.Position int position) {
        snackBar.position(position);
        return this;
    }

    public SnackingBuilder landscapeStyle(@Snacking.LandscapeStyle int landscapeStyle) {
        snackBar.landscapeStyle(landscapeStyle);
        return this;
    }

    public SnackingBuilder landscapeWidthRes(@DimenRes int landscapeWidthRes) {
        snackBar.landscapeWidthRes(landscapeWidthRes);
        return this;
    }

    public SnackingBuilder landscapeWidth(int landscapeWidth) {
        snackBar.landscapeWidth(landscapeWidth);
        return this;
    }

    public SnackingBuilder swipeToDismiss(boolean swipeToDismiss) {
        snackBar.swipeToDismiss(swipeToDismiss);
        return this;
    }

    public Snacking build() {
        return snackBar;
    }

}
