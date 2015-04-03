package eod.uaa.state;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Brent on 3/28/2015.
 */
public class ScreenSaver
{
    public static State currentState = null;
    public static ArrayList<State> stateList = new ArrayList<State>();
    public static int numberOfAnnouncements = -1; // will change upon initialization
    public static int currentAnnouncement = -1;
    private static int index = -1;

    public static void changeState()
    {
        // fill up state list if it's empty
        if(stateList.isEmpty())
        {
            // hard coded for now

            if(numberOfAnnouncements == -1) // error
                return;

            stateList.add(new State(StateType.ELECTRICITY));
            stateList.add(new State(StateType.WATER));
            stateList.add(new State(StateType.TEMPERATURE));
            stateList.add(new State(StateType.GAS));
            for(int i = 0; i < numberOfAnnouncements; i++)
            {
                stateList.add(new AnnouncementState(i));
            }
        }

        if(currentState == null)
        {
            // first initialization of state
            currentState = stateList.get(0);
            index = 0;
        }
        else
        {
            // all other changes of state
            int sizeOfList = stateList.size();
            if(currentState == stateList.get(sizeOfList - 1))
            {
                // last state passed, start back at the beginning
                currentState = stateList.get(0);
                index = 0;
            }
            else
            {
                // iterate to next state
                currentState = stateList.get(index + 1);
                index++;
            }

            // update what the current announcement is
            if(currentState instanceof AnnouncementState)
                currentAnnouncement = ((AnnouncementState) currentState).getCurrent();
            else
                currentAnnouncement = -1;
        }

        // logging
        if(currentState.stateType == StateType.ANNOUNCEMENTS)
        {
            Log.d("", "ann");
        }
        else if(currentState.stateType == StateType.ELECTRICITY)
        {
            Log.d("", "elec");
        }
    }
}
