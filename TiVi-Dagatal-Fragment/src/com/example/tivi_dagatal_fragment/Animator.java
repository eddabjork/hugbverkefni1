/**
 * Nafn:	   Edda Bjork Konradsdottir
 * Dagsetning: 30. oktober 2014
 * Markmid:	   Birta View fallega
 */

package com.example.tivi_dagatal_fragment;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Animator extends Animation {
    private View animatedView;
    private int endHeight;
    private int type;

    public Animator(View view, int duration, int _type) {
        setDuration(duration);
        animatedView = view;
        endHeight = animatedView.getLayoutParams().height;
        type = _type;
        if(type == 0) {
        	animatedView.getLayoutParams().height = 0;
        	animatedView.setVisibility(View.VISIBLE);
        }
    }

    //Notkun:		 view.applyTransformation(time, transformation)
    //Eftirskilyrdi: Buid er ad birta view a timanum time inni hlutnum t
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f) {
            if(type == 0) {
            	animatedView.getLayoutParams().height = (int) (endHeight * interpolatedTime);
            } else {
            	animatedView.getLayoutParams().height = endHeight - (int) (endHeight * interpolatedTime);
            }
            animatedView.requestLayout();
        } else {
            if(type == 0) {
            	animatedView.getLayoutParams().height = endHeight;
            	animatedView.requestLayout();
            } else {
            	animatedView.getLayoutParams().height = 0;
            	animatedView.setVisibility(View.GONE);
            	animatedView.requestLayout();
            	animatedView.getLayoutParams().height = endHeight;
            }
        }
    }
   
    //Notkun: 		 view.setHeightForWrapContent(act, view)
    //Eftirskilyrdi: Buid er ad setja retta haed a view vid adgerdina activity
    public static void setHeightForWrapContent(Activity activity, View view) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int screenWidth = metrics.widthPixels;

        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.EXACTLY);

        view.measure(widthMeasureSpec, heightMeasureSpec);
        int height = view.getMeasuredHeight();
        view.getLayoutParams().height = height;
    }
}
