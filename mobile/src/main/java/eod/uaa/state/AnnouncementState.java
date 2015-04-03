package eod.uaa.state;

/**
 * Created by Brent on 3/31/2015.
 */
public class AnnouncementState extends State
{
    public int current;

    public AnnouncementState(int current)
    {
        stateType = StateType.ANNOUNCEMENTS;
        this.current = current;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
