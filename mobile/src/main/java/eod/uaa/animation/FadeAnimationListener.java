package eod.uaa.animation;

import android.view.View;
import android.view.animation.Animation;

import android.view.animation.Animation.AnimationListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import eod.uaa.state.ScreenSaver;

/**
 * Created by Brent on 3/28/2015.
 */
public class FadeAnimationListener implements AnimationListener
{
    private View viewToFade;
    private RadioGroup radioGroup;

    public FadeAnimationListener(View viewToFade, RadioGroup radioGroup)
    {
        this.viewToFade = viewToFade;
        this.radioGroup = radioGroup;
    }

    public void onAnimationStart(Animation arg0)
    {
        // disable buttons while animation is starting
        for(int i = 0; i < radioGroup.getChildCount(); i++)
        {
            ((RadioButton)radioGroup.getChildAt(i)).setClickable(false);
        }
    }

    public void onAnimationRepeat(Animation arg0)
    {
    }

    public void onAnimationEnd(Animation arg0)
    {
        // turn buttons back on
        for(int i = 0; i < radioGroup.getChildCount(); i++)
        {
            ((RadioButton)radioGroup.getChildAt(i)).setClickable(true);
        }

        // the actual animation
        viewToFade.setVisibility(View.VISIBLE);
    }
}
