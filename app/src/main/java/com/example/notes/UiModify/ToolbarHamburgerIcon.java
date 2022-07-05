package com.example.notes.UiModify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;

import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;

import com.example.notes.R;

public class ToolbarHamburgerIcon extends DrawerArrowDrawable {
    /**
     * @param context used to get the configuration for the drawable from
     */
    public ToolbarHamburgerIcon(Context context) {
        super(context);
        setColor(context.getResources().getColor(R.color.black));

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        setBarLength(45f);
        setBarThickness(7f);
        setGapSize(6f);
    }
}
