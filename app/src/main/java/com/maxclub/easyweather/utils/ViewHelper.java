package com.maxclub.easyweather.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import java.util.List;

public class ViewHelper {

    public static void switchView(View view, List<View> views) {
        for (View container : views) {
            if (container.equals(view)) {
                if (view.getVisibility() == View.VISIBLE && view.getAlpha() == 1.0f) {
                    continue;
                }

                container.setAlpha(0.1f);
                container.setVisibility(View.VISIBLE);
                container.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                container.setAlpha(1.0f);
                                container.setVisibility(View.VISIBLE);
                            }
                        });

            } else {
                if (container.getVisibility() == View.VISIBLE) {
                    container.setAlpha(0.9f);
                    container.animate()
                            .alpha(0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    container.setVisibility(View.GONE);
                                }
                            });
                } else {
                    container.setVisibility(View.GONE);
                }
            }
        }
    }
}
