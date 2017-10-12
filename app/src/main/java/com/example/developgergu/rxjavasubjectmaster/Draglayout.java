package com.example.developgergu.rxjavasubjectmaster;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

/** Created by developgergu on 2017/10/13. */
public class Draglayout extends FrameLayout {
    TextView dragItem;
    private int width, height;

    private ViewDragHelper mHelper;

    public Draglayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        dragItem = new Button(context);
        dragItem.setText("drag me test !");
        dragItem.setTextColor(Color.WHITE);
        dragItem.setGravity(Gravity.CENTER);
        dragItem.setBackgroundColor(Color.BLUE);
        addView(dragItem);
        RxView.clicks(dragItem).subscribe(o -> Log.e("TAG", "Draglayout: ITEM CLICKED!"));
        RxView.drags(dragItem)
                .subscribe(
                        dragEvent ->
                                Log.e("TAG", "Draglayout: drags event = " + dragEvent.getAction()));
        mHelper =
                ViewDragHelper.create(
                        this,
                        new ViewDragHelper.Callback() {
                            @Override
                            public boolean tryCaptureView(View child, int pointerId) {
                                return dragItem == child;
                            }

                            @Override
                            public int clampViewPositionHorizontal(View child, int left, int dx) {
                                return left;
                            }

                            @Override
                            public int clampViewPositionVertical(View child, int top, int dy) {
                                return top;
                            }

                            @Override
                            public int getViewHorizontalDragRange(View child) {
                                return dragItem == child ? child.getWidth() : 0;
                            }

                            @Override
                            public int getViewVerticalDragRange(View child) {
                                return dragItem == child ? child.getHeight() : 0;
                            }
                        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = height = MeasureSpec.getSize(widthMeasureSpec) / 3;
        dragItem.getLayoutParams().width = width;
        dragItem.getLayoutParams().height = height;
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            dragItem.layout(width, height, width * 2, height * 2);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }
}
