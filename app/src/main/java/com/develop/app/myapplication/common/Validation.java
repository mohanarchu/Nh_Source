package com.develop.app.myapplication.common;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Validation {
        private static final Validation validation = new Validation();

        public static Validation getInstance() {
            return validation;
        }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void clickAnimation(View v) {
        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, 1.1f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(v, "scaleY", 1.0f, 1.1f);
        scaleAnim.setDuration(100);
        scaleAnim.setRepeatCount(1);
        scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
        scaleYAnim.setDuration(100);
        scaleYAnim.setRepeatCount(1);
        scaleYAnim.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnim.start();
        scaleYAnim.start();
    }

}
