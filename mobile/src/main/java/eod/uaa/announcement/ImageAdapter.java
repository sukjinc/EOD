package eod.uaa.announcement;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import eod.uaa.main.R;

public class ImageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<Bitmap> bitmaps;
    private LayoutInflater inflater;

    public ImageAdapter()
    {

    }

    public ImageAdapter(Activity activity, ArrayList<Bitmap> bitmaps)
    {
        this.activity = activity;
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return this.bitmaps.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;
        Button btnClose;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_image, container,false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);

        Bitmap bitmap = bitmaps.get(position);
        imgDisplay.setImageBitmap(bitmap);

        container.addView(viewLayout);

        return viewLayout;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public void unzoomImageView()
    {
        Log.d("", "Called!");
    }
}
