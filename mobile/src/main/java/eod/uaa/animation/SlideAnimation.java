package eod.uaa.animation;

import android.view.View;
import android.view.animation.TranslateAnimation;

/**
 * Animates slide transitions.
 */
public class SlideAnimation {


    // To animate view slide in from top to view

    /**
     * Slide enters from top
     * @param view
     */
    public void slideFromTop(View view) {

        TranslateAnimation animate = new TranslateAnimation(0, 0, -view.getWidth(), 0);
        animate.setDuration(700);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
        view.bringToFront();
        view.setEnabled(true);


    }

    // To animate view slide out from view to bottom

    /**
     * Slide Exits to bottom.
     * @param view
     */
    public void slideToBottom(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
        animate.setDuration(700);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
        view.destroyDrawingCache();
        view.setEnabled(false);
        view.destroyDrawingCache();


    }
/*

    public void pagerSlideToBottom(ViewPager view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
        animate.setDuration(700);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(ViewPager.GONE);
        view.setEnabled(false);
        view.destroyDrawingCache();
    }


    public void imageSlideToBottom(ImageView view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
        animate.setDuration(700);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(ImageView.GONE);
        view.setEnabled(false);
        view.destroyDrawingCache();
        MainActivity.viewDestroyer(view);
    }
*/


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

//if(e == viewEnum.VIEWPAGER){
//
//        if(view != null)
//        if(view.getParent() != null)
//        ((ViewGroup) view.getParent()).removeView(view);
//
//        }