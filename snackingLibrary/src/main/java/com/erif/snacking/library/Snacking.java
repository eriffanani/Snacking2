package com.erif.snacking.library;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Snacking {

    // Properties
    private final View parentView;
    private String message;
    @DrawableRes
    private int iconRes;
    @DrawableRes
    private int backgroundRes = 0;
    private int backgroundColor = 0;
    float borderSize = 0f;
    int borderColor = 0;
    private int durationSnackBar = Snackbar.LENGTH_SHORT;
    private boolean useMargin = false;
    private int position = BOTTOM;
    private int landscapeStyle = MATCH;
    private int landscapeWidth = 0;
    private Handler handler;

    private float cornerTopLeft = 0f;
    private float cornerTopRight = 0f;
    private float cornerBottomLeft = 0f;
    private float cornerBottomRight = 0f;

    // SnackBar
    private Snackbar snackBar;

    // Views
    private RelativeLayout parent;
    private ImageView imgIcon;
    private TextView textMessage;
    private TextView textAction;

    // Callback
    private Callback callback;
    private View fab = null;

    @IntDef({NORMAL, BOLD, ITALIC, BOLD_ITALIC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextStyle {}
    public static final int NORMAL = 0;
    public static final int BOLD = 1;
    public static final int ITALIC = 2;
    public static final int BOLD_ITALIC = 3;

    @IntDef({MATCH, LEFT, CENTER, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LandscapeStyle {}
    public static final int MATCH = 0;
    public static final int LEFT = 1;
    public static final int CENTER = 2;
    public static final int RIGHT = 3;

    @IntDef({TOP, BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Position {}
    public static final int TOP = 0;
    public static final int BOTTOM = 1;

    private int rotation = 0;

    @IntDef({SORT, LONG, INDEFINITE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {}
    public static final int SORT = 0;
    public static final int LONG = 1;
    public static final int INDEFINITE = 2;

    public Snacking(@NonNull View parentView) {
        this.parentView = parentView;
        init();
    }

    public Snacking(@NonNull View parentView, String message) {
        this.parentView = parentView;
        this.message = message;
        init();
    }

    public Snacking(@NonNull View parentView, String message, @DrawableRes int iconRes) {
        this.parentView = parentView;
        this.message = message;
        this.iconRes = iconRes;
        init();
    }

    private void init() {
        Context context = parentView.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup mParent = (ViewGroup) parentView;
        snackBar = Snackbar.make(parentView, "", durationSnackBar);
        rotation = getRotation(context);

        View customView = inflater.inflate(R.layout.quick_snack_bar_custom_layout, mParent, false);
        initView(customView);
        String mMessage = message == null ? "This is null message" : message;
        textMessage.setText(mMessage);
        applyIcon();
        textAction.setOnClickListener(view -> {
            if (callback != null) {
                try {
                    Thread.sleep(300);
                    callback.onActionClick(this);
                } catch (InterruptedException e) {
                    log("Callback thread interrupted");
                }
            }
        });
        if (snackBar != null) {
            Drawable getDrawable = getDrawables(R.drawable.bg_custom_quick_snack_bar);
            GradientDrawable drawable = (GradientDrawable) getDrawable;
            setSnackBarElevation(0f);
            if (drawable != null) {
                drawable.setCornerRadius(0f);
                drawable.setStroke(0, null);
                drawable.setColor(getColor(R.color.quick_snack_bar_default_background));
            }
            setSnackBarBackground(drawable);
            Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackBar.getView();
            snackBarLayout.addView(customView);
            snackBar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);

            if (parentView instanceof CoordinatorLayout layout) {
                for (int i=0; i<layout.getChildCount(); i++) {
                    View child = layout.getChildAt(i);
                    if (child instanceof FloatingActionButton) {
                        fab = child;
                        break;
                    } else if (child instanceof ExtendedFloatingActionButton) {
                        fab = child;
                        break;
                    }
                }
            }
        }
    }

    private void initView(View layout) {
        parent = layout.findViewById(R.id.snackBar_custom_parent);
        /* Calculate ActionBar height
        Context context = layout.getContext();
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(
                android.R.attr.actionBarSize, tv, true)
        ) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data, context.getResources().getDisplayMetrics()
            );
            ViewGroup.LayoutParams param = parent.getLayoutParams();
            float cuttingSize = actionBarHeight / 8f;
            param.height = actionBarHeight - (int) cuttingSize;
        }*/
        imgIcon = layout.findViewById(R.id.snackBar_custom_imgIcon);
        textMessage = layout.findViewById(R.id.snackBar_custom_txtMessage);
        textAction = layout.findViewById(R.id.snackBar_custom_btnAction);
    }

    public void message(@StringRes int messageRes) {
        this.message = getString(messageRes);
        textMessage.setText(message);
    }

    public void message(String message) {
        this.message = message;
        textMessage.setText(message);
    }

    public void messageMaxLines(int lines) {
        if (lines > 0) {
            textMessage.setMaxLines(lines);
            textMessage.setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    public void textColor(@ColorRes int colorRes) {
        int color = getColor(colorRes);
        if (color != 0)
            textMessage.setTextColor(color);
    }

    public void textColor(@NonNull String colorCode) {
        int color = parseColor(colorCode);
        if (color != 0)
            textMessage.setTextColor(color);
    }

    public void textStyle(@TextStyle int style) {
        if (style == BOLD) {
            textMessage.setTypeface(textMessage.getTypeface(), Typeface.BOLD);
        } else if (style == ITALIC) {
            textMessage.setTypeface(textMessage.getTypeface(), Typeface.ITALIC);
        } else if (style == BOLD_ITALIC) {
            textMessage.setTypeface(textMessage.getTypeface(), Typeface.BOLD_ITALIC);
        } else {
            textMessage.setTypeface(textMessage.getTypeface(), Typeface.NORMAL);
        }
    }

    public void icon(@DrawableRes int iconRes) {
        this.iconRes = iconRes;
        applyIcon();
    }

    public void icon(@DrawableRes int iconRes, @ColorRes int colorRes) {
        this.iconRes = iconRes;
        applyIcon();
        int color = getColor(colorRes);
        if (color != 0) imgIcon.setColorFilter(color);
    }

    public void icon(@DrawableRes int iconRes, @NonNull String colorCode) {
        this.iconRes = iconRes;
        int color = parseColor(colorCode);
        if (color != 0) imgIcon.setColorFilter(color);
        applyIcon();
    }

    private void applyIcon() {
        if (iconRes != 0) {
            imgIcon.setVisibility(View.VISIBLE);
            imgIcon.setImageResource(iconRes);
        } else {
            imgIcon.setVisibility(View.GONE);
        }
    }

    public void action(@NonNull String actionText, @NonNull Callback callback) {
        this.callback = callback;
        textAction.setVisibility(View.VISIBLE);
        textAction.setText(actionText);
    }

    public void action(@StringRes int actionTextRes, @NonNull Callback callback) {
        this.callback = callback;
        textAction.setVisibility(View.VISIBLE);
        textAction.setText(getString(actionTextRes));
    }

    public void action(@NonNull String actionText, @ColorRes int textColorRes, @NonNull Callback callback) {
        action(actionText, callback);
        int color = getColor(textColorRes);
        if (color != 0) textAction.setTextColor(color);
    }

    public void action(@StringRes int actionTextRes, @ColorRes int textColorRes, @NonNull Callback callback) {
        action(actionTextRes, callback);
        int color = getColor(textColorRes);
        if (color != 0) textAction.setTextColor(color);
    }

    public void action(@NonNull String actionText, @NonNull String textColorCode, @NonNull Callback callback) {
        action(actionText, callback);
        int color = parseColor(textColorCode);
        if (color != 0) textAction.setTextColor(color);
    }

    public void action(@StringRes int actionTextRes, @NonNull String textColorCode, @NonNull Callback callback) {
        action(actionTextRes, callback);
        int color = parseColor(textColorCode);
        if (color != 0) textAction.setTextColor(color);
    }

    public void cornerRadius(float cornerRadius) {
        if (cornerRadius != 0) {
            cornerTopLeft = cornerRadius;
            cornerTopRight = cornerRadius;
            cornerBottomLeft = cornerRadius;
            cornerBottomRight = cornerRadius;
        }
    }

    public void cornerRadius(@DimenRes int cornerRadiusRes) {
        int cornerRadius = getDimenInt(cornerRadiusRes);
        if (cornerRadius != 0) {
            cornerTopLeft = cornerRadius;
            cornerTopRight = cornerRadius;
            cornerBottomLeft = cornerRadius;
            cornerBottomRight = cornerRadius;
        }
    }

    public void cornerRadius(
            float topLeft, float topRight,
            float bottomLeft, float bottomRight
    ) {
        cornerTopLeft = topLeft;
        cornerTopRight = topRight;
        cornerBottomLeft = bottomLeft;
        cornerBottomRight = bottomRight;
    }

    public void cornerRadius(
            @DimenRes int topLeftRes,
            @DimenRes int topRightRes,
            @DimenRes int bottomLeftRes,
            @DimenRes int bottomRightRes
    ) {
        cornerTopLeft = getDimen(topLeftRes);
        cornerTopRight = getDimen(topRightRes);
        cornerBottomLeft = getDimen(bottomLeftRes);
        cornerBottomRight = getDimen(bottomRightRes);
    }

    private float[] cornerRadii(
            float topLeft, float topRight,
            float bottomLeft, float bottomRight
    ) {
        return new float[] {
                topLeft, topLeft, topRight, topRight,
                bottomRight, bottomRight, bottomLeft, bottomLeft
        };
    }

    private void applyBackgroundBitmap(
            BitmapDrawable bitmapDrawable,
            float topLeft, float topRight, float bottomLeft, float bottomRight
    ) {
        if (bitmapDrawable.getBitmap() != null) {
            parent.post(() -> {

                Bitmap scaledBitmap = null;
                int sWidth = parent.getContext().getResources().getDisplayMetrics().widthPixels;
                int padding = getDimenInt(R.dimen.quick_snack_bar_margin_start_bottom_end);
                int totalWidth = sWidth - (useMargin ? (padding * 2) : 0);
                try {
                    scaledBitmap = matchBitmap(bitmapDrawable, totalWidth);
                } catch (Exception | OutOfMemoryError e) {
                    log("Bitmap size error: "+e.getMessage());
                }
                if (scaledBitmap != null) {

                    int snackBarTotalHeight = snackBar.getView().getHeight();
                    float halfBitmap = (float) scaledBitmap.getHeight() / 2f;
                    float halfSnackBarHeight = (float) snackBarTotalHeight / 2f;
                    float startY = halfBitmap - halfSnackBarHeight;

                    Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, 0, (int) startY, totalWidth, snackBarTotalHeight);

                    Bitmap output = Bitmap.createBitmap(totalWidth, snackBarTotalHeight, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(output);

                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setColor(0xff424242);
                    float[] arrCorner = cornerRadii(topLeft, topRight, bottomLeft, bottomRight);
                    Rect rect = new Rect(0, 0, output.getWidth(), output.getHeight());
                    RectF rectF = new RectF();
                    rectF.set(0, 0, totalWidth, snackBar.getView().getHeight());
                    Path path = new Path();
                    path.addRoundRect(rectF, arrCorner, Path.Direction.CW);
                    canvas.drawARGB(0, 0, 0, 0);
                    canvas.drawPath(path, paint);

                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    canvas.drawBitmap(croppedBitmap, rect, rectF, paint);
                    if (borderSize > 0 && borderColor != 0) {
                        Paint paintBorder = new Paint();
                        paintBorder.setStyle(Paint.Style.STROKE);
                        paintBorder.setColor(borderColor);
                        paintBorder.setStrokeWidth(borderSize);
                        paintBorder.setAntiAlias(true);
                        paintBorder.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                        canvas.drawPath(path, paintBorder);
                    }
                    Drawable finalDrawable = new BitmapDrawable(parent.getContext().getResources(), output);
                    setSnackBarBackground(finalDrawable);
                }
            });
        }
    }

    private Bitmap matchBitmap(Drawable drawable, int outputWidth) {
        // Get Drawable Size
        int getDrawableWidth = drawable.getIntrinsicWidth();
        int drawableWidth = getDrawableWidth > 0 ? getDrawableWidth : 1;
        int getDrawableHeight = drawable.getIntrinsicHeight();
        int drawableHeight = getDrawableHeight > 0 ? getDrawableHeight : 1;
        // Create Canvas from drawable
        Bitmap bitmap = Bitmap.createBitmap(drawableWidth, drawableHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        // Bitmap Size
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        // Output Size
        int outputHeight = bitmapHeight * outputWidth / bitmapWidth;
        // Create New Bitmap
        return Bitmap.createScaledBitmap(bitmap, outputWidth, outputHeight, false);
    }

    public void border(@DimenRes int borderRes, @ColorRes int borderColorRes) {
        int size = getDimenInt(borderRes);
        if (size > 0) {
            this.borderSize = size;
            int color = getColor(borderColorRes);
            if (color != 0)
                this.borderColor = color;
        }
    }

    public void border(float borderSize, @NonNull String borderColorCode) {
        if (borderSize > 0) {
            this.borderSize = borderSize;
            int color = parseColor(borderColorCode);
            if (color != 0)
                this.borderColor = color;
        }
    }

    public void background(@DrawableRes int drawableRes) {
        this.backgroundRes = drawableRes;
    }

    public void backgroundColor(@ColorRes int colorRes) {
        int color = getColor(colorRes);
        if (color != 0)
            this.backgroundColor = color;
    }

    public void backgroundColor(@NonNull String colorCode) {
        int color = parseColor(colorCode);
        if (color != 0)
            this.backgroundColor = color;
    }

    public void useMargin(boolean isUseMargin) {
        this.useMargin = isUseMargin;
    }

    private void applyMargin() {
        if (snackBar != null) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackBar.getView().getLayoutParams();
            if (!useMargin) {
                params.setMargins(0, 0, 0, 0);
                Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackBar.getView();
                snackBarLayout.setPadding(0, 0, 0, 0);
            } else {
                int size = getDimenInt(R.dimen.quick_snack_bar_margin_start_bottom_end);
                params.setMargins(size, 0, size, size);
                setSnackBarElevation(getDimen(R.dimen.quick_snack_bar_default_elevation));
            }
            snackBar.getView().setLayoutParams(params);
        }
    }

    private void applyPosition() {
        if (snackBar != null) {
            if (position == TOP) {
                if (rotation == 90 || rotation == -90) {
                    if (landscapeStyle != MATCH) {
                        setOnLandscape(0);
                    } else {
                        setOnPosition();
                    }
                } else {
                    setOnPosition();
                }
            } else {
                if (rotation == 90 || rotation == -90) {
                    if (landscapeWidth > 0 && landscapeStyle == MATCH)
                        landscapeStyle = LEFT;
                    if (landscapeStyle != MATCH)
                        setOnLandscape(1);
                }
            }

            textMessage.post(() -> {
                if (textMessage.getLineCount() > 2 || textAction.length() > 10) {

                    RelativeLayout.LayoutParams paramsIcon = (RelativeLayout.LayoutParams) imgIcon.getLayoutParams();
                    paramsIcon.removeRule(RelativeLayout.CENTER_VERTICAL);
                    paramsIcon.addRule(RelativeLayout.ALIGN_TOP, textMessage.getId());

                    int getMarginTop = getDimenInt(R.dimen.quick_snack_bar_vertical_padding);
                    float countMarginTop = getMarginTop / 6f;
                    int marginTop = getMarginTop - (int) countMarginTop;
                    paramsIcon.setMargins(
                            getDimenInt(R.dimen.quick_snack_bar_icon_margin_start),
                            marginTop, 0, 0
                    );
                    imgIcon.setLayoutParams(paramsIcon);

                    RelativeLayout.LayoutParams paramsMessage = (RelativeLayout.LayoutParams) textMessage.getLayoutParams();
                    paramsMessage.removeRule(RelativeLayout.START_OF);
                    textMessage.setLayoutParams(paramsMessage);
                    textMessage.setPadding(
                            textMessage.getPaddingStart(), textMessage.getPaddingTop(),
                            textMessage.getPaddingEnd(), getDimenInt(R.dimen.quick_snack_bar_default_elevation)
                    );

                    RelativeLayout.LayoutParams paramsAction = (RelativeLayout.LayoutParams) textAction.getLayoutParams();
                    paramsAction.removeRule(RelativeLayout.ALIGN_TOP);
                    paramsAction.removeRule(RelativeLayout.ALIGN_BOTTOM);
                    paramsAction.addRule(RelativeLayout.BELOW, textMessage.getId());

                    textAction.setLayoutParams(paramsAction);
                    int getActionPaddingVertical = textAction.getPaddingTop();
                    float count = getActionPaddingVertical / 2.5f;
                    int actionPaddingVertical = getActionPaddingVertical - (int) count;
                    textAction.setPadding(
                            textAction.getPaddingStart(), actionPaddingVertical,
                            textAction.getPaddingEnd(), actionPaddingVertical
                    );
                    parent.setPadding(0, 0, 0, getDimenInt(R.dimen.quick_snack_bar_icon_margin_start));
                }
            });

        }
    }

    private void setOnPosition() {
        ViewGroup.LayoutParams param = snackBar.getView().getLayoutParams();
        snackBar.setDuration(Snackbar.LENGTH_INDEFINITE);
        if (param != null) {
            if (param instanceof CoordinatorLayout.LayoutParams) {
                paramCoordinator(param).gravity = Gravity.TOP;
            } else if (param instanceof FrameLayout.LayoutParams) {
                paramFrame(param).gravity = Gravity.TOP;
            } else if (param instanceof RelativeLayout.LayoutParams) {
                paramRelative(param).addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            }
            snackBar.getView().setLayoutParams(param);
            snackBar.getView().startAnimation(
                    AnimationUtils.loadAnimation(parent.getContext(), R.anim.anim_quick_snack_bar_slide_bottom)
            );
        }
    }

    private void setOnLandscape(int type) {
        ViewGroup.LayoutParams param = snackBar.getView().getLayoutParams();
        int screenWidth = getWidthScreen(parent.getContext());
        if (landscapeWidth > 0) {
            if (landscapeWidth < screenWidth)
                param.width = landscapeWidth;
        } else {
            param.width = screenWidth - (int) (screenWidth / 2.5f);
        }
        if (param instanceof CoordinatorLayout.LayoutParams) {
            if (landscapeStyle == LEFT) {
                if (type == 1)
                    paramCoordinator(param).gravity = Gravity.BOTTOM;
                else
                    paramCoordinator(param).gravity = Gravity.TOP;
            } else if (landscapeStyle == RIGHT) {
                if (type == 1)
                    paramCoordinator(param).gravity = Gravity.BOTTOM | Gravity.END;
                else
                    paramCoordinator(param).gravity = Gravity.TOP | Gravity.END;
            } else {
                if (type == 1)
                    paramCoordinator(param).gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                else
                    paramCoordinator(param).gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            }
        } else if (param instanceof FrameLayout.LayoutParams) {
            if (landscapeStyle == LEFT) {
                if (type == 1)
                    paramFrame(param).gravity = Gravity.BOTTOM;
                else
                    paramFrame(param).gravity = Gravity.TOP;
            } else if (landscapeStyle == RIGHT) {
                if (type == 1)
                    paramFrame(param).gravity = Gravity.BOTTOM | Gravity.END;
                else
                    paramFrame(param).gravity = Gravity.TOP | Gravity.END;
            } else {
                if (type == 1)
                    paramFrame(param).gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                else
                    paramFrame(param).gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            }
        } else if (param instanceof RelativeLayout.LayoutParams) {
            if (landscapeStyle == LEFT) {
                if (type == 1)
                    paramRelative(param).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                else
                    paramRelative(param).addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            } else if (landscapeStyle == RIGHT) {
                if (type == 1)
                    paramRelative(param).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                else
                    paramRelative(param).addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                paramRelative(param).addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            } else {
                if (type == 1)
                    paramRelative(param).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                else
                    paramRelative(param).addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                paramRelative(param).addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            }
        }
        snackBar.getView().setLayoutParams(param);
        if (type == 0)
            snackBar.getView().startAnimation(
                    AnimationUtils.loadAnimation(parent.getContext(), R.anim.anim_quick_snack_bar_slide_bottom)
            );
    }

    public void heightRes(@DimenRes int heightRes) {
        int mHeight = getDimenInt(heightRes);
        if (mHeight > 0) {
            ViewGroup.LayoutParams param = parent.getLayoutParams();
            param.height = mHeight;
            parent.setLayoutParams(param);
        }
    }

    public void height(int height) {
        if (height > 0) {
            ViewGroup.LayoutParams param = parent.getLayoutParams();
            param.height = height;
            parent.setLayoutParams(param);
        }
    }

    public void anchorView(View anchorView) {
        if (anchorView != null)
            snackBar.setAnchorView(anchorView);
    }

    public void duration(@Duration int duration) {
        if (duration == LONG) {
            durationSnackBar = Snackbar.LENGTH_LONG;
        } else if (duration == INDEFINITE) {
            durationSnackBar = Snackbar.LENGTH_INDEFINITE;
        } else {
            durationSnackBar = Snackbar.LENGTH_SHORT;
        }
        if (snackBar != null)
            snackBar.setDuration(durationSnackBar);
    }

    public void position(@Position int position) {
        this.position = position;
    }

    public void landscapeStyle(@LandscapeStyle int landscapeStyle) {
        this.landscapeStyle = landscapeStyle;
    }

    public void landscapeWidthRes(@DimenRes int widthRes) {
        float width = getDimen(widthRes);
        if (width > 0)
            this.landscapeWidth = (int) width;
    }

    public void landscapeWidth(int width) {
        this.landscapeWidth = width;
    }

    public void fontFamily(@FontRes int fontRes) {
        try {
            Typeface typeface;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                typeface = parent.getContext().getResources().getFont(fontRes);
            else
                typeface = ResourcesCompat.getFont(parent.getContext(), fontRes);
            textMessage.setTypeface(typeface);
            textAction.setTypeface(typeface);
        } catch (Resources.NotFoundException e) {
            log("Font resource not found");
        }
    }

    public void fontFamily(@FontRes int fontRes, @DimenRes int fontSizeRes) {
        fontFamily(fontRes);
        float size = getDimen(fontSizeRes);
        if (size > 0) {
            textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            textAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
    }

    public void fontFamily(@FontRes int font, float fontSize) {
        fontFamily(font);
        if (fontSize > 0) {
            textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
            textAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        }
    }

    public void swipeToDismiss(boolean swipeToDismiss) {
        if (!swipeToDismiss)
            snackBar.setBehavior(
                    new BaseTransientBottomBar.Behavior() {
                        @Override
                        public boolean canSwipeDismissView(View child) {return false;}
                    }
            );
    }

    private void applyBackground() {
        snackBar.getView().setBackgroundTintList(null);
        Drawable getDrawable = getDrawable(backgroundRes);
        if (getDrawable instanceof BitmapDrawable) {
            applyBackgroundBitmap((BitmapDrawable) getDrawable,
                    cornerTopLeft, cornerTopRight, cornerBottomLeft, cornerBottomRight
            );
        } else if (getDrawable instanceof VectorDrawable || getDrawable instanceof VectorDrawableCompat) {
            Bitmap bitmap = Bitmap.createBitmap(
                    getDrawable.getIntrinsicWidth(), getDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888
            );
            Canvas canvas = new Canvas(bitmap);
            getDrawable.setBounds(0, 0, getDrawable.getIntrinsicWidth(), getDrawable.getIntrinsicHeight());
            getDrawable.draw(canvas);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(parent.getContext().getResources(), bitmap);
            applyBackgroundBitmap(
                    bitmapDrawable, cornerTopLeft, cornerTopRight, cornerBottomLeft, cornerBottomRight
            );
        } else {
            getDrawable = getDrawables(R.drawable.bg_custom_quick_snack_bar);
            GradientDrawable drawable = (GradientDrawable) getDrawable;
            if (drawable != null) {
                if (backgroundColor != 0)
                    drawable.setColor(backgroundColor);
                float[] corners = cornerRadii(cornerTopLeft, cornerTopRight, cornerBottomLeft, cornerBottomRight);
                drawable.setCornerRadii(corners);
                if (borderSize > 0 && borderColor != 0)
                    drawable.setStroke((int) borderSize, borderColor);
                setSnackBarBackground(drawable);
            }
        }
        Drawable getDrawableButton = textAction.getBackground();
        if (getDrawableButton != null) {
            if (getDrawableButton instanceof RippleDrawable drawableButton) {
                Drawable getDrawableRipple = drawableButton.getDrawable(0);
                if (getDrawableRipple != null) {
                    if (getDrawableRipple instanceof GradientDrawable drawableRipple) {
                        drawableRipple.setCornerRadii(
                                cornerRadii(cornerTopLeft, cornerTopRight, cornerBottomLeft, cornerBottomRight)
                        );
                        textAction.setBackground(drawableButton);
                    }
                }
            }
        }
    }

    public void show() {
        if (!snackBar.isShown()) {
            applyMargin();
            applyPosition();
            applyBackground();
            if (position == TOP) {
                showWithTopPosition();
            } else {
                snackBar.show();
            }
        }
    }

    private void showWithTopPosition() {
        if (parentView instanceof CoordinatorLayout layout) {
            for (int i=0; i<layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                fab = child instanceof FloatingActionButton ? (FloatingActionButton) child : child instanceof ExtendedFloatingActionButton ? (ExtendedFloatingActionButton) child : null;
                if (fab != null) {
                    Animation anim = AnimationUtils.loadAnimation(
                            parent.getContext(), R.anim.anim_quick_snack_bar_hide_fab
                    );
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            fab.setVisibility(View.INVISIBLE);
                            snackBar.show();
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    fab.startAnimation(anim);
                    break;
                }
            }
            if (fab == null)
                snackBar.show();
        } else {
            snackBar.show();
        }
        if (durationSnackBar != Snackbar.LENGTH_INDEFINITE) {
            snackBar.addCallback(new Snackbar.Callback(){
                @Override
                public void onShown(Snackbar sb) {
                    super.onShown(sb);
                    int duration = durationSnackBar == Snackbar.LENGTH_SHORT ? 2000 : 3250;
                    handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> {
                        if (snackBar.isShown()) {
                            snackBar.getView().startAnimation(
                                    AnimationUtils.loadAnimation(
                                            parent.getContext(), R.anim.anim_quick_snack_bar_slide_top
                                    )
                            );
                            snackBar.dismiss();
                        }
                    }, duration);
                }

                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if (fab != null) {
                        fab.setVisibility(View.VISIBLE);
                        Animation anim = AnimationUtils.loadAnimation(
                                parent.getContext(), R.anim.anim_quick_snack_bar_show_fab
                        );
                        fab.startAnimation(anim);
                    }
                }
            });
        }
    }

    public void dismiss() {
        if (snackBar.isShown()) {
            if (position == TOP) {
                snackBar.getView().startAnimation(AnimationUtils.loadAnimation(
                        parent.getContext(), R.anim.anim_quick_snack_bar_slide_top
                ));
            }
            snackBar.dismiss();
            if (handler != null)
                handler.removeCallbacksAndMessages(null);
        }

    }

    private int getDimenInt(int id) {
        int finalValue = 0;
        try {
            finalValue = parent.getContext().getResources().getDimensionPixelSize(id);
        } catch (Resources.NotFoundException e) {
            log("Dimen not found");
        }
        return finalValue;
    }

    private float getDimen(int id) {
        float finalValue = 0f;
        try {
            finalValue = parent.getContext().getResources().getDimension(id);
        } catch (Resources.NotFoundException e) {
            log("Dimen not found");
        }
        return finalValue;
    }

    private int getColor(int id) {
        int finalColor = 0;
        try {
            finalColor = ContextCompat.getColor(parent.getContext(), id);
        } catch (Resources.NotFoundException e) {
            log("Color resource not found");
        }
        return finalColor;
    }

    private int parseColor(String colorCode) {
        int color = 0;
        try {
            color = Color.parseColor(colorCode);
        } catch (NumberFormatException e) {
            Log.d("QuickSnackBar", "Parse color error");
        }
        return color;
    }

    private Drawable getDrawable(@DrawableRes int id) {
        Drawable drawable = getDrawables(R.drawable.bg_custom_quick_snack_bar);
        if (id == 0 || id == -1)
            return null;
        try {
            drawable = getDrawables(id);
        } catch (Resources.NotFoundException e) {
            log("Drawable resource not found.");
        }
        return drawable;
    }

    private Drawable getDrawables(int id) {
        return ResourcesCompat.getDrawable(parent.getContext().getResources(), id, null);
    }

    private void setSnackBarBackground(Drawable drawable) {
        if (snackBar != null)
            snackBar.getView().setBackground(drawable);
    }

    private void setSnackBarElevation(float elevation) {
        if (snackBar != null)
            snackBar.getView().setElevation(elevation);
    }

    private void log(String message) {
        String mMessage = "Default message";
        if (message != null)
            mMessage = message;
        Log.d("QuickSnackBar", mMessage);
    }

    private String getString(int id) {
        String message = "null";
        try {
            message = parent.getContext().getResources().getString(id);
        } catch (Resources.NotFoundException e) {
            log("String resource not found");
        }
        return message;
    }

    private int getRotation(Context context) {
        int angle = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_90:
                angle = -90;
                break;
            case Surface.ROTATION_180:
                angle = 180;
                break;
            case Surface.ROTATION_270:
                angle = 90;
                break;
            case Surface.ROTATION_0:
                break;
        }
        return angle;
    }

    private CoordinatorLayout.LayoutParams paramCoordinator(ViewGroup.LayoutParams param) {
        return (CoordinatorLayout.LayoutParams) param;
    }

    private FrameLayout.LayoutParams paramFrame(ViewGroup.LayoutParams param) {
        return (FrameLayout.LayoutParams) param;
    }

    private RelativeLayout.LayoutParams paramRelative(ViewGroup.LayoutParams param) {
        return (RelativeLayout.LayoutParams) param;
    }

    private int getWidthScreen(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public interface Callback {
        void onActionClick(Snacking snackBar);
    }

    // Builder Class
    public static class Builder extends SnackingBuilder {
        public Builder(@NonNull View parentView) {
            super(parentView);
        }
        public Builder(@NonNull View parentView, String message) {
            super(parentView, message);
        }
        public Builder(@NonNull View parentView, String message, int iconRes) {super(parentView, message, iconRes);}
    }

    // State Class
    public static class State extends SnackingState {
        public State(@NonNull View parentView, int state) {
            super(parentView, state);
        }
    }

}
