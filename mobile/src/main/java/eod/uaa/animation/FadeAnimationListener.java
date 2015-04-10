package eod.uaa.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RadioGroup;

/**
 * Created by Brent on 3/28/2015.
 */
public class FadeAnimationListener implements AnimationListener {
    private View viewToFade;
    public FadeAnimationListener(View viewToFade) {
        this.viewToFade = viewToFade;


    }

    public void onAnimationStart(Animation arg0) {

    }

    public void onAnimationRepeat(Animation arg0) {
    }

    public void onAnimationEnd(Animation arg0) {

        viewToFade.setVisibility(View.VISIBLE);

    }
}
