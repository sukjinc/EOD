package eod.uaa.graph;

/**
 * Created by Brent on 3/27/2015.
 */
public class GraphHelper
{
    public static float getMin(float[] in) {
        float min;
        int size = in.length;
        float temp;
        temp = in[0];
        for (int i = 1; i < size; i++) {
            if ( temp >= (int) in[i]) {
                temp = in[i];
            }
        }
        min = temp;

        return min;
    }
    public static float getMax(float[] in) {
        float max;
        int size = in.length;
        float temp;
        temp = in[0];
        for (int i = 1; i < size; i++) {
            if (temp >= in[i]) {
                temp = in[i];
            }
        }
        max = temp;

        return max;
    }
}
