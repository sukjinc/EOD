package eod.uaa.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import eod.uaa.animation.AlphaAnimationHolder;
import eod.uaa.animation.FadeAnimationListener;
import eod.uaa.announcement.ImageAdapter;
import eod.uaa.announcement.InfinitePagerAdapter;
import eod.uaa.graph.GraphHelper;
import eod.uaa.state.ScreenSaver;
import eod.uaa.state.StateType;

public class MainActivity extends Activity {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    @SuppressWarnings("deprecation")

    private ViewFlipper mViewFlipper;

    private RadioGroup radioGroup;
    private RadioButton btnIntroduction;
    private RadioButton btnElectricity;
    private RadioButton btnWater;
    private RadioButton btnGas;
    private RadioButton btnTemperature;
    private RadioButton btnAnnouncements;

    private ImageAdapter imageAdapter;
    private InfinitePagerAdapter infinitePagerAdapter;
    private ViewPager viewPager;

    private XYPlot elecPlot;
    private XYPlot waterPlot;
    private XYPlot tempPlot;
    private XYPlot gasPlot;

    private Timer screenTimer;
    private Timer moverTimer;
    private TimerTask screenTimerTask;
    private TimerTask moverTimerTask;

    private MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = (RadioGroup) findViewById(R.id.states);
        btnIntroduction = (RadioButton) findViewById(R.id.btn_introduction);
        btnElectricity = (RadioButton) findViewById(R.id.btn_electricity);
        btnWater = (RadioButton) findViewById(R.id.btn_water);
        btnGas = (RadioButton) findViewById(R.id.btn_gas);
        btnTemperature = (RadioButton) findViewById(R.id.btn_temperature);
        btnAnnouncements = (RadioButton) findViewById(R.id.btn_announcements);

        initAnnouncementLayout();
        initElecGraphLayout();
        initTempGraphLayout();
        initGasGraphLayout();
        initWaterGraphLayout();
        initFadeAnimations();
        initTimers();

        viewPager.setVisibility(View.INVISIBLE);
        elecPlot.setVisibility(View.INVISIBLE);
        waterPlot.setVisibility(View.INVISIBLE);
        tempPlot.setVisibility(View.INVISIBLE);
        gasPlot.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        super.dispatchTouchEvent(ev);

        if(screenTimer != null)
            screenTimer.cancel();
        screenTimer = new Timer();
        if(screenTimerTask != null)
            screenTimerTask.cancel();

        screenTimerTask = new ScreenTimerTask();
        screenTimer.schedule(screenTimerTask, 5000);

        if(moverTimer != null)
            moverTimer.cancel();

        return true;
    }

    private void initTimers()
    {
        screenTimerTask = new ScreenTimerTask();
        moverTimerTask = new MoverTimerTask();
        screenTimer = new Timer();

        screenTimer.schedule(screenTimerTask, 5000);
    }

    private void initFadeAnimations()
    {
        AlphaAnimationHolder.fadeInElec.setDuration(1000);
        AlphaAnimationHolder.fadeInElec.setAnimationListener(new FadeAnimationListener(elecPlot, radioGroup));

        AlphaAnimationHolder.fadeInWater.setDuration(1000);
        AlphaAnimationHolder.fadeInWater.setAnimationListener(new FadeAnimationListener(waterPlot, radioGroup));

        AlphaAnimationHolder.fadeInTemp.setDuration(1000);
        AlphaAnimationHolder.fadeInTemp.setAnimationListener(new FadeAnimationListener(tempPlot, radioGroup));

        AlphaAnimationHolder.fadeInGas.setDuration(1000);
        AlphaAnimationHolder.fadeInGas.setAnimationListener(new FadeAnimationListener(gasPlot, radioGroup));

        AlphaAnimationHolder.fadeInAnnouncements.setDuration(1000);
        AlphaAnimationHolder.fadeInAnnouncements.setAnimationListener(new FadeAnimationListener(viewPager, radioGroup));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup rGroup, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)rGroup.findViewById(checkedId);

                if(checkedRadioButton == btnElectricity)
                {
                    clearGUI();
                    elecPlot.startAnimation(AlphaAnimationHolder.fadeInElec);
                }
                else if(checkedRadioButton == btnWater)
                {
                    clearGUI();
                    waterPlot.startAnimation(AlphaAnimationHolder.fadeInWater);
                }
                else if(checkedRadioButton == btnTemperature)
                {
                    clearGUI();
                    tempPlot.startAnimation(AlphaAnimationHolder.fadeInTemp);
                }
                else if(checkedRadioButton == btnGas)
                {
                    clearGUI();
                    gasPlot.startAnimation(AlphaAnimationHolder.fadeInGas);
                }
                else if(checkedRadioButton == btnAnnouncements)
                {
                    clearGUI();
                    viewPager.startAnimation(AlphaAnimationHolder.fadeInAnnouncements);
                }
            }
        });
    }

    private void initAnnouncementLayout()
    {
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        bitmaps.add(BitmapFactory.decodeResource(this.getResources(), R.raw.one));
        bitmaps.add(BitmapFactory.decodeResource(this.getResources(), R.raw.two));
        bitmaps.add(BitmapFactory.decodeResource(this.getResources(), R.raw.three));
        bitmaps.add(BitmapFactory.decodeResource(this.getResources(), R.raw.four));

        ScreenSaver.numberOfAnnouncements = bitmaps.size();

        viewPager = (ViewPager) findViewById(R.id.pager);
        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        imageAdapter = new ImageAdapter(MainActivity.this, bitmaps);
        infinitePagerAdapter = new InfinitePagerAdapter(imageAdapter);
        //viewPager.setAdapter(infinitePagerAdapter);
        viewPager.setAdapter(imageAdapter);
        viewPager.setCurrentItem(position);

        // infinite view page http://stackoverflow.com/questions/7546224/viewpager-as-a-circular-queue-wrapping

        viewPager.setOnPageChangeListener(new OnPageChangeListener()
        {

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                imageAdapter.unzoomImageView();
            }
        });

    }

    private void initElecGraphLayout()
    {
        //Must get data and generate arrays.
        float[] intreadings =  {46.5f,34f, 29.12f, 18.2f, 20.1512f,40.2f,90.51f,20.1f};
        float min = GraphHelper.getMin(intreadings);
        float max = GraphHelper.getMax(intreadings);
        Number[] readings = {46.5,34, 29.12, 18.2, 20.1512,40.2,90.51,20.1}; //Readings
        Number[] time = {1425688873, 1425689873,1425694802,1425695702,1425696602,1425697502,1425698702,1425699602 }; //TimeStamp

        // initialize our XYPlot reference:
        elecPlot = (XYPlot) findViewById(R.id.elecPlot);

        XYSeries myPlot = new SimpleXYSeries(Arrays.asList(time), Arrays.asList(readings),"Electricity Usage Plot");

        LineAndPointFormatter plotFormat = new LineAndPointFormatter();
        plotFormat.setPointLabelFormatter(new PointLabelFormatter());
        plotFormat.configure(getApplicationContext(), R.xml.line_point_formatter_with_elec);

        elecPlot.getLegendWidget().setWidth(0); //Disable Legend
        //------- Range Domain Format -----
        elecPlot.setRangeBoundaries(0, 100, BoundaryMode.FIXED); //RANGE Boundaries*********

        elecPlot.addSeries(myPlot, plotFormat);
        elecPlot.setRangeValueFormat(new DecimalFormat("0"));
        elecPlot.setDomainValueFormat(new Format() {
            // create a simple date format that draws on the year portion of our timestamp.
            // see http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
            // for a full description of SimpleDateFormat.

            private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M hh:mm");

            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

                // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
                // we multiply our timestamp by 1000:
                long timestamp = ((Number) obj).longValue() * 1000;
                Date date = new Date(timestamp);
                return dateFormat.format(date, toAppendTo, pos);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;

            }
        });


        Paint lineFill = new Paint(); //Shade Format b
        lineFill.setAlpha(100);
        lineFill.setShader(new LinearGradient(0,40,50,250, Color.YELLOW, Color.GREEN, Shader.TileMode.MIRROR));
        plotFormat.setFillPaint(lineFill);
    }

    private void initTempGraphLayout()
    {
        //Must get data and generate arrays.
        float[] intreadings =  {46.5f,34f, 29.12f, 18.2f, 20.1512f,40.2f,90.51f,20.1f};
        float min = GraphHelper.getMin(intreadings);
        float max = GraphHelper.getMax(intreadings);
        Number[] readings = {46.5,34, 29.12, 18.2, 20.1512,40.2,90.51,20.1}; //Readings
        Number[] time = {1425688873, 1425689873,1425694802,1425695702,1425696602,1425697502,1425698702,1425699602 }; //TimeStamp

        // initialize our XYPlot reference:
        tempPlot = (XYPlot) findViewById(R.id.tempPlot);

        XYSeries myPlot = new SimpleXYSeries(Arrays.asList(time), Arrays.asList(readings),"Temperature Plot");

        LineAndPointFormatter plotFormat = new LineAndPointFormatter();
        plotFormat.setPointLabelFormatter(new PointLabelFormatter());
        plotFormat.configure(getApplicationContext(), R.xml.line_point_formatter_with_temp);

        tempPlot.getLegendWidget().setWidth(0); //Disable Legend
        //------- Range Domain Format -----
        tempPlot.setRangeBoundaries(0, 100, BoundaryMode.FIXED); //RANGE Boundaries*********

        tempPlot.addSeries(myPlot, plotFormat);
        tempPlot.setRangeValueFormat(new DecimalFormat("0"));
        tempPlot.setDomainValueFormat(new Format() {
            // create a simple date format that draws on the year portion of our timestamp.
            // see http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
            // for a full description of SimpleDateFormat.

            private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M hh:mm");

            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

                // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
                // we multiply our timestamp by 1000:
                long timestamp = ((Number) obj).longValue() * 1000;
                Date date = new Date(timestamp);
                return dateFormat.format(date, toAppendTo, pos);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;

            }
        });


        Paint lineFill = new Paint(); //Shade Format b
        lineFill.setAlpha(100);
        lineFill.setShader(new LinearGradient(0,40,50,250, Color.YELLOW, Color.GREEN, Shader.TileMode.MIRROR));
        plotFormat.setFillPaint(lineFill);
    }
    private void initWaterGraphLayout()
    {
        //Must get data and generate arrays.
        float[] intreadings =  {46.5f,34f, 29.12f, 18.2f, 20.1512f,40.2f,90.51f,20.1f};
        float min = GraphHelper.getMin(intreadings);
        float max = GraphHelper.getMax(intreadings);
        Number[] readings = {46.5,34, 29.12, 18.2, 20.1512,40.2,90.51,20.1}; //Readings
        Number[] time = {1425688873, 1425689873,1425694802,1425695702,1425696602,1425697502,1425698702,1425699602 }; //TimeStamp

        // initialize our XYPlot reference:
        waterPlot = (XYPlot) findViewById(R.id.waterPlot);

        XYSeries myPlot = new SimpleXYSeries(Arrays.asList(time), Arrays.asList(readings),"Water Usage Plot");

        LineAndPointFormatter plotFormat = new LineAndPointFormatter();
        plotFormat.setPointLabelFormatter(new PointLabelFormatter());
        plotFormat.configure(getApplicationContext(), R.xml.line_point_formatter_with_water);

        waterPlot.getLegendWidget().setWidth(0); //Disable Legend
        //------- Range Domain Format -----
        waterPlot.setRangeBoundaries(0, 100, BoundaryMode.FIXED); //RANGE Boundaries*********

        waterPlot.addSeries(myPlot, plotFormat);
        waterPlot.setRangeValueFormat(new DecimalFormat("0"));
        waterPlot.setDomainValueFormat(new Format() {
            // create a simple date format that draws on the year portion of our timestamp.
            // see http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
            // for a full description of SimpleDateFormat.

            private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M hh:mm");

            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

                // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
                // we multiply our timestamp by 1000:
                long timestamp = ((Number) obj).longValue() * 1000;
                Date date = new Date(timestamp);
                return dateFormat.format(date, toAppendTo, pos);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;

            }
        });


        Paint lineFill = new Paint(); //Shade Format b
        lineFill.setAlpha(100);
        lineFill.setShader(new LinearGradient(0,40,50,250, Color.YELLOW, Color.GREEN, Shader.TileMode.MIRROR));
        plotFormat.setFillPaint(lineFill);
    }

    private void initGasGraphLayout()
    {
        //Must get data and generate arrays.
        float[] intreadings =  {46.5f,34f, 29.12f, 18.2f, 20.1512f,40.2f,90.51f,20.1f};
        float min = GraphHelper.getMin(intreadings);
        float max = GraphHelper.getMax(intreadings);
        Number[] readings = {46.5,34, 29.12, 18.2, 20.1512,40.2,90.51,20.1}; //Readings
        Number[] time = {1425688873, 1425689873,1425694802,1425695702,1425696602,1425697502,1425698702,1425699602 }; //TimeStamp

        // initialize our XYPlot reference:
        gasPlot = (XYPlot) findViewById(R.id.gasPlot);

        XYSeries myPlot = new SimpleXYSeries(Arrays.asList(time), Arrays.asList(readings),"Gas Usage Plot");

        LineAndPointFormatter plotFormat = new LineAndPointFormatter();
        plotFormat.setPointLabelFormatter(new PointLabelFormatter());
        plotFormat.configure(getApplicationContext(), R.xml.line_point_formatter_with_gas);

        gasPlot.getLegendWidget().setWidth(0); //Disable Legend
        //------- Range Domain Format -----
        gasPlot.setRangeBoundaries(0, 100, BoundaryMode.FIXED); //RANGE Boundaries*********

        gasPlot.addSeries(myPlot, plotFormat);
        gasPlot.setRangeValueFormat(new DecimalFormat("0"));
        gasPlot.setDomainValueFormat(new Format() {
            // create a simple date format that draws on the year portion of our timestamp.
            // see http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
            // for a full description of SimpleDateFormat.

            private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M hh:mm");

            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

                // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
                // we multiply our timestamp by 1000:
                long timestamp = ((Number) obj).longValue() * 1000;
                Date date = new Date(timestamp);
                return dateFormat.format(date, toAppendTo, pos);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;

            }
        });


        Paint lineFill = new Paint(); //Shade Format b
        lineFill.setAlpha(100);
        lineFill.setShader(new LinearGradient(0,40,50,250, Color.YELLOW, Color.GREEN, Shader.TileMode.MIRROR));
        plotFormat.setFillPaint(lineFill);
    }

    public void testDatabase(View view)
    {
        Log.d("", "Clicked");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ScreenTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            // start screen saver
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(moverTimer != null)
                        moverTimer.cancel();
                    moverTimer = new Timer();
                    moverTimerTask = new MoverTimerTask();
                    moverTimer.schedule(moverTimerTask, 2000, 2000);
                }
            });
        }
    }

    private class MoverTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            // screen saver code
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ScreenSaver.changeState();
                    int announcementToScrollTo = ScreenSaver.currentAnnouncement;

                    Log.d("", announcementToScrollTo + "");

                    if(ScreenSaver.currentState.stateType == StateType.ANNOUNCEMENTS)
                    {
                        // if scrolling to the first announcement, then the previous state was not announcements
                        if(announcementToScrollTo == 0)
                        {
                            uncheckButtons();
                            viewPager.setCurrentItem(0);
                            btnAnnouncements.setChecked(true);
                        }
                        else
                        {
                            // no animation needed, just change page
                            viewPager.setCurrentItem(announcementToScrollTo);
                        }
                    }
                    else if(ScreenSaver.currentState.stateType == StateType.ELECTRICITY)
                    {
                        uncheckButtons();
                        btnElectricity.setChecked(true);
                    }
                    else if(ScreenSaver.currentState.stateType == StateType.WATER)
                    {
                        uncheckButtons();
                        btnWater.setChecked(true);
                    }
                    else if(ScreenSaver.currentState.stateType == StateType.TEMPERATURE)
                    {
                        uncheckButtons();
                        btnTemperature.setChecked(true);
                    }
                    else if(ScreenSaver.currentState.stateType == StateType.GAS)
                    {
                        uncheckButtons();
                        btnGas.setChecked(true);
                    }


                }
            } );
        }
    }

    private void clearGUI()
    {
        viewPager.clearAnimation();
        viewPager.setAnimation(null);
        viewPager.setVisibility(View.GONE);
        AlphaAnimationHolder.fadeInAnnouncements.cancel();

        elecPlot.clearAnimation();
        elecPlot.setAnimation(null);
        elecPlot.setVisibility(View.GONE);
        AlphaAnimationHolder.fadeInElec.cancel();

        waterPlot.clearAnimation();
        waterPlot.setAnimation(null);
        waterPlot.setVisibility(View.GONE);
        AlphaAnimationHolder.fadeInWater.cancel();

        tempPlot.clearAnimation();
        tempPlot.setAnimation(null);
        tempPlot.setVisibility(View.GONE);
        AlphaAnimationHolder.fadeInWater.cancel();

        gasPlot.clearAnimation();
        gasPlot.setAnimation(null);
        gasPlot.setVisibility(View.GONE);
        AlphaAnimationHolder.fadeInGas.cancel();

    }

    private void uncheckButtons()
    {
        // uncheck all buttons
        for(int i = 0; i < radioGroup.getChildCount(); i++)
        {
            ((RadioButton) radioGroup.getChildAt(i)).setChecked(false);
        }
     }
}
