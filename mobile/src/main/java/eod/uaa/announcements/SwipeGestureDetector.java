package eod.uaa.announcements;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import eod.uaa.eod.R;

public class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener
{
    private ViewFlipper mViewFlipper;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private Animation.AnimationListener mAnimationListener;
    private Context mContext;

    private ImageView previous;
    private ImageView current;
    private ImageView next;

    public SwipeGestureDetector(Context context, Animation.AnimationListener animationListener, ViewFlipper viewFlipper)
    {
        mContext = context;
        mAnimationListener = animationListener;
        mViewFlipper = viewFlipper;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        try {
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
                // controlling animation
                mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                mViewFlipper.showNext();
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,R.anim.right_out));
                // controlling animation
                mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                mViewFlipper.showPrevious();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
