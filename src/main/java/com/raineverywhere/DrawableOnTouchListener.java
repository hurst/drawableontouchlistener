package com.raineverywhere;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

public abstract class DrawableOnTouchListener implements View.OnTouchListener {
    private static final int DEFAULT_FUZZ = 10;
    private int fuzz;

    // Matches array indexes of array returned by TextView.getCompoundDrawables()
    private enum Position {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }

    public DrawableOnTouchListener() {
        this(DEFAULT_FUZZ);
    }

    public DrawableOnTouchListener(int fuzz) {
        this.fuzz = fuzz;
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        boolean isCompoundButton = v instanceof CompoundButton;
        boolean isTextView = v instanceof TextView;
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && (isCompoundButton || isTextView) ) {

            Drawable[] drawables;
            if(isCompoundButton) {
                drawables = ((CompoundButton)v).getCompoundDrawables();
            } else {
                drawables = ((TextView)v).getCompoundDrawables();
            }

            final int x = (int) event.getX();
            final int y = (int) event.getY();

            for(Position pos : Position.values()) {
                if(drawables[pos.ordinal()] == null) continue;

                final Rect bounds = drawables[pos.ordinal()].getBounds();

                if (pos == Position.LEFT
                        && x >= (v.getPaddingLeft() - fuzz) && x <= (bounds.width() + v.getPaddingLeft() + fuzz)
                        && y >= ((v.getHeight() / 2) - (bounds.height() / 2) - fuzz) && y <= ((v.getHeight() / 2) + (bounds.height() / 2) + fuzz)) {
                    return onLeftDrawableTouch(event);

                } else if(pos == Position.TOP
                        && x >= ((v.getWidth() / 2) - (bounds.width() / 2) - fuzz) && x <= ((v.getWidth() / 2) + (bounds.width() / 2) + fuzz)
                        && y >= (v.getPaddingTop() - fuzz) && y <= (bounds.height() + v.getPaddingTop() + fuzz)) {
                    return onTopDrawableTouch(event);

                } else if(pos == Position.RIGHT
                        && x >= (v.getWidth() - bounds.width() - v.getPaddingRight() - fuzz) && x <= (v.getWidth() - v.getPaddingRight() + fuzz)
                        && y >= ((v.getHeight() / 2) - (bounds.height() / 2) - fuzz) && y <= ((v.getHeight() / 2) + (bounds.height() / 2) + fuzz)) {
                    return onRightDrawableTouch(event);

                } else if(pos == Position.BOTTOM
                        && x >= ((v.getWidth() / 2) - (bounds.width() / 2) - fuzz) && x <= ((v.getWidth() / 2) + (bounds.width() / 2) + fuzz)
                        && y >= (v.getHeight() - bounds.height() - v.getPaddingBottom() - fuzz) && y <= (v.getHeight() - v.getPaddingBottom() + fuzz)) {
                    return onBottomDrawableTouch(event);

                } else {
                    return onDefaultTouch(event);
                }
            }
        }

        return onDefaultTouch(event);
    }

    public abstract boolean onLeftDrawableTouch(final MotionEvent event);
    public abstract boolean onTopDrawableTouch(final MotionEvent event);
    public abstract boolean onRightDrawableTouch(final MotionEvent event);
    public abstract boolean onBottomDrawableTouch(final MotionEvent event);
    public abstract boolean onDefaultTouch(final MotionEvent event);
}