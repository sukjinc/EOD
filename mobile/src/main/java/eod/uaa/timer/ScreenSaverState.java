package eod.uaa.timer;

/**
 * Created by Brent on 3/28/2015.
 */
public class ScreenSaverState
{
    public enum State
    {
        ELECTRICITY, ANNOUNCEMENTS, NONE
    }

    public static State currentState = State.NONE;
    public static int numberOfAnnouncements = -1;
    public static int currentAnnouncement = -1;
}
