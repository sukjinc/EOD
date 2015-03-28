package eod.uaa.animation;

import android.view.View;
import android.view.animation.Animation;

import android.view.animation.Animation.AnimationListener;

/**
 * Created by Brent on 3/28/2015.
 */
public class FadeAnimationListener implements AnimationListener
{
    private View view;
    private boolean isFadeIn;

    public FadeAnimationListener(View view, boolean isFadeIn)
    {
        this.view = view;
        this.isFadeIn = isFadeIn;
    }

    public void onAnimationStart(Animation arg0)
    {

    }

    public void onAnimationRepeat(Animation arg0)
    {
    }

    public void onAnimationEnd(Animation arg0)
    {
        if(isFadeIn)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);

    }
}
