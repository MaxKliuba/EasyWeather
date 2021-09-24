package com.maxclub.easyweather.utils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.maxclub.easyweather.utils.TextDrawable;

import org.jetbrains.annotations.NotNull;

public class ItemTouchHelperSimpleCallback extends ItemTouchHelper.SimpleCallback {

    private ColorDrawable mSwipeBackground;
    private Drawable mIcon;
    private TextDrawable mText;

    public ItemTouchHelperSimpleCallback(int dragDirs, int swipeDirs,
                                         ColorDrawable swipeBackground, Drawable icon, TextDrawable text) {
        super(dragDirs, swipeDirs);

        mSwipeBackground = swipeBackground;
        mIcon = icon;
        mText = text;
    }

    @Override
    public boolean onMove(@NonNull @NotNull RecyclerView recyclerView,
                          @NonNull @NotNull RecyclerView.ViewHolder viewHolder,
                          @NonNull @NotNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView,
                            @NonNull @NotNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        int iconMargin = (itemView.getHeight() - mIcon.getIntrinsicHeight()) / 2;

        Rect leftRect = new Rect(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
        Rect rightRect = new Rect(itemView.getRight() + (int) dX, itemView.getTop(),
                itemView.getRight(), itemView.getBottom());

        if (dX > 0) {
            mSwipeBackground.setBounds(leftRect);
            mIcon.setBounds(itemView.getLeft() + iconMargin,
                    itemView.getTop() + iconMargin,
                    itemView.getLeft() + iconMargin + mIcon.getIntrinsicWidth(),
                    itemView.getBottom() - iconMargin);
            mText.setBounds(itemView.getLeft() + iconMargin * 2 + mIcon.getIntrinsicWidth(),
                    itemView.getTop(),
                    itemView.getLeft() + iconMargin * 2 + mIcon.getIntrinsicWidth()
                            + mText.getIntrinsicWidth(),
                    itemView.getBottom());
        } else {
            mSwipeBackground.setBounds(rightRect);
            mIcon.setBounds(itemView.getRight() - iconMargin - mIcon.getIntrinsicWidth(),
                    itemView.getTop() + iconMargin,
                    itemView.getRight() - iconMargin, itemView.getBottom() - iconMargin);
            mText.setBounds(itemView.getRight() - iconMargin * 2 - mIcon.getIntrinsicWidth()
                            - mText.getIntrinsicWidth(),
                    itemView.getTop(),
                    itemView.getRight() - iconMargin * 2 - mIcon.getIntrinsicWidth(),
                    itemView.getBottom());
        }

        mSwipeBackground.draw(c);

        c.save();

        if (dX > 0) {
            c.clipRect(leftRect);
        } else {
            c.clipRect(rightRect);
        }

        mIcon.draw(c);
        mText.draw(c);

        c.restore();

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
