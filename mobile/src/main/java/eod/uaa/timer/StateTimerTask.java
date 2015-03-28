package eod.uaa.timer;

import android.app.Activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

/**
 * Created by Brent on 3/28/2015.
 */
public class StateTimerTask extends TimerTask
{
    private Activity activity;

    public StateTimerTask(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
        final String strDate = simpleDateFormat.format(calendar.getTime());

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {


            }
        });
    }
}
