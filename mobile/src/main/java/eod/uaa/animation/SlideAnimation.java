package eod.uaa.animation;

import android.view.View;
import android.view.animation.TranslateAnimation;

public class SlideAnimation {
    // To animate view slide in from top to view
    public void slideFromTop(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, -view.getWidth(), 0);
        animate.setDuration(700);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    // To animate view slide out from view to bottom
    public void slideToBottom(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
        animate.setDuration(700);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.INVISIBLE);
    }
}

/*
        import android.view.View;
        import android.view.animation.Animation;
        import android.view.animation.Animation.AnimationListener;
        import android.widget.RadioGroup;


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
*/
