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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

import eod.uaa.animation.SlideAnimation;
import eod.uaa.announcement.ImageAdapter;
import eod.uaa.graph.GraphHelper;
import eod.uaa.state.ScreenSaver;
import eod.uaa.state.StateType;

public class MainActivity extends Activity {


    viewEnum CurrentView = viewEnum.INTRO; //current view
    @SuppressWarnings("deprecation")


    private RadioGroup radioGroup;
    private RadioButton btnIntroduction;
    private RadioButton btnElectricity;
    private RadioButton btnWater;
    private RadioButton btnGas;
    private RadioButton btnTemperature;
    private RadioButton btnAnnouncements;
    private ImageAdapter imageAdapter;
    private ViewPager viewPager;
    private ImageView Introduction;
    private LinearLayout elecLayout;
    private LinearLayout tempLayout;
    private LinearLayout waterLayout;
    private LinearLayout gasLayout;
    private XYPlot elecUsagePlot;
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
        layoutInit();
        initFadeAnimations();
        initTimers();

        Introduction.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        elecLayout.setVisibility(View.INVISIBLE);
        waterLayout.setVisibility(View.INVISIBLE);
        tempLayout.setVisibility(View.INVISIBLE);
        gasLayout.setVisibility(View.INVISIBLE);

    }
    public void layoutInit(){
        elecLayout = (LinearLayout) findViewById(R.id.elecLayout);
        tempLayout = (LinearLayout) findViewById(R.id.tempLayout);
        waterLayout = (LinearLayout) findViewById(R.id.waterLayout);
        gasLayout = (LinearLayout) findViewById(R.id.gasLayout);
        initIntroduction();
        initAnnouncementLayout();
        initElecGraphLayout();
        initTempGraphLayout();
        initGasGraphLayout();
        initWaterGraphLayout();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);

        if (screenTimer != null)
            screenTimer.cancel();
        screenTimer = new Timer();
        if (screenTimerTask != null)
            screenTimerTask.cancel();

        screenTimerTask = new ScreenTimerTask();
        screenTimer.schedule(screenTimerTask, 30000);

        if (moverTimer != null)
            moverTimer.cancel();

        return true;
    }

    private void initTimers() {
        screenTimerTask = new ScreenTimerTask();
        moverTimerTask = new MoverTimerTask();
        screenTimer = new Timer();
        screenTimer.schedule(screenTimerTask, 30000);
    }


    private void initFadeAnimations() {
        SlideAnimation slideAnimation = new SlideAnimation();
        final SlideAnimation finalFadeAnimation = slideAnimation;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup rGroup, int checkedId) {
                Log.d("listener","caleedlister");

                if (checkedId == R.id.btn_electricity) {
                    finalFadeAnimation.slideFromTop(elecLayout);
                    if (CurrentView == viewEnum.INTRO) {
                        finalFadeAnimation.slideToBottom(Introduction);
                    } else if (CurrentView == viewEnum.WATERLAYOUT) {
                        finalFadeAnimation.slideToBottom(waterLayout);
                    } else if (CurrentView == viewEnum.GASLAYOUT) {
                        finalFadeAnimation.slideToBottom(gasLayout);
                    } else if (CurrentView == viewEnum.TEMPLAYOUT) {
                        finalFadeAnimation.slideToBottom(tempLayout);
                    } else {//announcement
                        finalFadeAnimation.slideToBottom(viewPager);
                    }
                    CurrentView = viewEnum.ELECLAYOUT;

                } else if (checkedId == R.id.btn_introduction) {
                    finalFadeAnimation.slideFromTop(Introduction);
                    if (CurrentView == viewEnum.ELECLAYOUT) {
                        finalFadeAnimation.slideToBottom(elecLayout);
                    } else if (CurrentView == viewEnum.WATERLAYOUT) {
                        finalFadeAnimation.slideToBottom(waterLayout);
                    } else if (CurrentView == viewEnum.GASLAYOUT) {
                        finalFadeAnimation.slideToBottom(gasLayout);
                    } else if (CurrentView == viewEnum.TEMPLAYOUT) {
                        finalFadeAnimation.slideToBottom(tempLayout);
                    } else {//announcement
                        finalFadeAnimation.slideToBottom(viewPager);
                    }
                    CurrentView = viewEnum.INTRO;

                } else if (checkedId == R.id.btn_water) {
                    finalFadeAnimation.slideFromTop(waterLayout);
                    if (CurrentView == viewEnum.INTRO) {
                        finalFadeAnimation.slideToBottom(Introduction);
                    } else if (CurrentView == viewEnum.ELECLAYOUT) {
                        finalFadeAnimation.slideToBottom(elecLayout);
                    } else if (CurrentView == viewEnum.GASLAYOUT) {
                        finalFadeAnimation.slideToBottom(gasLayout);
                    } else if (CurrentView == viewEnum.TEMPLAYOUT) {
                        finalFadeAnimation.slideToBottom(tempLayout);
                    } else {//announcement
                        finalFadeAnimation.slideToBottom(viewPager);
                    }
                    CurrentView = viewEnum.WATERLAYOUT;

                } else if (checkedId == R.id.btn_temperature) {
                    finalFadeAnimation.slideFromTop(tempLayout);
                    if (CurrentView == viewEnum.INTRO) {
                        finalFadeAnimation.slideToBottom(Introduction);
                    } else if (CurrentView == viewEnum.ELECLAYOUT) {
                        finalFadeAnimation.slideToBottom(elecLayout);
                    } else if (CurrentView == viewEnum.WATERLAYOUT) {
                        finalFadeAnimation.slideToBottom(waterLayout);
                    } else if (CurrentView == viewEnum.GASLAYOUT) {
                        finalFadeAnimation.slideToBottom(gasLayout);
                    } else {//announcement
                        finalFadeAnimation.slideToBottom(viewPager);
                    }
                    CurrentView = viewEnum.TEMPLAYOUT;

                } else if (checkedId == R.id.btn_gas) {
                    finalFadeAnimation.slideFromTop(gasLayout);
                    if (CurrentView == viewEnum.INTRO) {
                        finalFadeAnimation.slideToBottom(Introduction);
                    } else if (CurrentView == viewEnum.ELECLAYOUT) {
                        finalFadeAnimation.slideToBottom(elecLayout);
                    } else if (CurrentView == viewEnum.WATERLAYOUT) {
                        finalFadeAnimation.slideToBottom(waterLayout);
                    } else if (CurrentView == viewEnum.TEMPLAYOUT) {
                        finalFadeAnimation.slideToBottom(tempLayout);
                    } else {//announcement
                        finalFadeAnimation.slideToBottom(viewPager);
                    }
                    CurrentView = viewEnum.GASLAYOUT;

                } else if (checkedId == R.id.btn_announcements) {
                    finalFadeAnimation.slideFromTop(viewPager);
                    if (CurrentView == viewEnum.INTRO) {
                        finalFadeAnimation.pagerSlideToBottom(Introduction);
                    } else if (CurrentView == viewEnum.ELECLAYOUT) {
                        finalFadeAnimation.pagerSlideToBottom(elecLayout);
                    } else if (CurrentView == viewEnum.WATERLAYOUT) {
                        finalFadeAnimation.pagerSlideToBottom(waterLayout);
                    } else if (CurrentView == viewEnum.GASLAYOUT) {
                        finalFadeAnimation.pagerSlideToBottom(gasLayout);
                    } else { //tempPlot
                        finalFadeAnimation.pagerSlideToBottom(tempLayout);
                    }
                    CurrentView = viewEnum.VIEWPAGER;

                }
            }
        });
    }

    private void initIntroduction() {
        Introduction = (ImageView) findViewById(R.id.introduction);
    }

    private void initAnnouncementLayout() {
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
        //viewPager.setAdapter(infinitePagerAdapter);
        viewPager.setAdapter(imageAdapter);
        viewPager.setCurrentItem(position);


        // infinite view page http://stackoverflow.com/questions/7546224/viewpager-as-a-circular-queue-wrapping

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imageAdapter.unzoomImageView();
            }
        });

    }

    private void initElecGraphLayout() {


        //Must get data and generate arrays.
        float[] intreadings = {46.5f, 34f, 29.12f, 18.2f, 20.1512f, 40.2f, 90.51f, 20.1f};
        float min = GraphHelper.getMin(intreadings);
        float max = GraphHelper.getMax(intreadings);
        Number[] readings = {46.5, 34, 29.12, 18.2, 20.1512, 40.2, 90.51, 20.1}; //Readings
        Number[] time = {1425688873, 1425689873, 1425694802, 1425695702, 1425696602, 1425697502, 1425698702, 1425699602}; //TimeStamp

        // initialize our XYPlot reference:
        elecUsagePlot = (XYPlot) findViewById(R.id.elecUsagePlot);

        XYSeries myPlot = new SimpleXYSeries(Arrays.asList(time), Arrays.asList(readings), "Electricity Usage Plot");

        LineAndPointFormatter plotFormat = new LineAndPointFormatter();
        plotFormat.setPointLabelFormatter(new PointLabelFormatter());
        plotFormat.configure(getApplicationContext(), R.xml.line_point_formatter_with_elec);

        elecUsagePlot.getLegendWidget().setWidth(0); //Disable Legend
        //------- Range Domain Format -----
        elecUsagePlot.setRangeBoundaries(0, 100, BoundaryMode.FIXED); //RANGE Boundaries*********

        elecUsagePlot.addSeries(myPlot, plotFormat);
        elecUsagePlot.setRangeValueFormat(new DecimalFormat("0"));
        elecUsagePlot.setDomainValueFormat(new Format() {
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
        lineFill.setAlpha(180);
        lineFill.setShader(new LinearGradient(0, 0, 0, 250, Color.YELLOW, Color.GREEN, Shader.TileMode.MIRROR));
        plotFormat.setFillPaint(lineFill);
        //This gets rid of the black border (up to the graph) there is no black border around the labels
        elecUsagePlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        elecUsagePlot.getBackgroundPaint().setColor(Color.WHITE);


    }

    private void initTempGraphLayout() {
        //Must get data and generate arrays.

        float[] intreadings = {46.5f, 34f, 29.12f, 18.2f, 20.1512f, 40.2f, 90.51f, 20.1f};
        float min = GraphHelper.getMin(intreadings);
        float max = GraphHelper.getMax(intreadings);
        Number[] readings = {46.5, 34, 29.12, 18.2, 20.1512, 40.2, 90.51, 20.1}; //Readings
        Number[] time = {1425688873, 1425689873, 1425694802, 1425695702, 1425696602, 1425697502, 1425698702, 1425699602}; //TimeStamp

        // initialize our XYPlot reference:
        tempPlot = (XYPlot) findViewById(R.id.tempPlot);

        XYSeries myPlot = new SimpleXYSeries(Arrays.asList(time), Arrays.asList(readings), "Temperature Plot");

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
        lineFill.setAlpha(180);
        lineFill.setShader(new LinearGradient(0, 0, 0, 250, Color.YELLOW, Color.RED, Shader.TileMode.MIRROR));
        plotFormat.setFillPaint(lineFill);
        tempPlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        tempPlot.getBackgroundPaint().setColor(Color.WHITE);
    }

    private void initWaterGraphLayout() {
        //Must get data and generate arrays.
        float[] intreadings = {46.5f, 34f, 29.12f, 18.2f, 20.1512f, 40.2f, 90.51f, 20.1f};
        float min = GraphHelper.getMin(intreadings);
        float max = GraphHelper.getMax(intreadings);
        Number[] readings = {46.5, 34, 29.12, 18.2, 20.1512, 40.2, 90.51, 20.1}; //Readings
        Number[] time = {1425688873, 1425689873, 1425694802, 1425695702, 1425696602, 1425697502, 1425698702, 1425699602}; //TimeStamp

        // initialize our XYPlot reference:
        waterPlot = (XYPlot) findViewById(R.id.waterPlot);

        XYSeries myPlot = new SimpleXYSeries(Arrays.asList(time), Arrays.asList(readings), "Water Usage Plot");

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
        lineFill.setAlpha(180);
        lineFill.setShader(new LinearGradient(0, 0, 0, 250, Color.BLUE, Color.WHITE, Shader.TileMode.MIRROR));
        plotFormat.setFillPaint(lineFill);
        waterPlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        waterPlot.getBackgroundPaint().setColor(Color.WHITE);
    }

    private void initGasGraphLayout() {
        //Must get data and generate arrays.
        float[] intreadings = {46.5f, 34f, 29.12f, 18.2f, 20.1512f, 40.2f, 90.51f, 20.1f};
        float min = GraphHelper.getMin(intreadings);
        float max = GraphHelper.getMax(intreadings);
        Number[] readings = {46.5, 34, 29.12, 18.2, 20.1512, 40.2, 90.51, 20.1}; //Readings
        Number[] time = {1425688873, 1425689873, 1425694802, 1425695702, 1425696602, 1425697502, 1425698702, 1425699602}; //TimeStamp

        // initialize our XYPlot reference:
        gasPlot = (XYPlot) findViewById(R.id.gasPlot);

        XYSeries myPlot = new SimpleXYSeries(Arrays.asList(time), Arrays.asList(readings), "Gas Usage Plot");

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
        lineFill.setAlpha(180);
        lineFill.setShader(new LinearGradient(0, 0, 0, 250, Color.MAGENTA, Color.BLACK, Shader.TileMode.MIRROR));
        plotFormat.setFillPaint(lineFill);
        gasPlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        gasPlot.getBackgroundPaint().setColor(Color.WHITE);
    }

    public void testDatabase(View view) {

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



    private enum viewEnum {
        INTRO,
        ELECLAYOUT,
        WATERLAYOUT,
        TEMPLAYOUT,
        GASLAYOUT,
        VIEWPAGER
    }

    private class ScreenTimerTask extends TimerTask {
        @Override
        public void run() {
            // start screen saver
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (moverTimer != null)
                        moverTimer.cancel();
                    moverTimer = new Timer();
                    moverTimerTask = new MoverTimerTask();
                    moverTimer.schedule(moverTimerTask, 5000, 5000);
                }
            });
        }
    }

    private class MoverTimerTask extends TimerTask {
        @Override
        public void run() {
            // screen saver code
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ScreenSaver.changeState();
                    int announcementToScrollTo = ScreenSaver.currentAnnouncement;


                    if (ScreenSaver.currentState.stateType == StateType.ANNOUNCEMENTS) {
                        if (announcementToScrollTo == 0) {
                            viewPager.setCurrentItem(0);
                            radioGroup.check(btnAnnouncements.getId());
                        } else {
                            // no animation needed, just change page
                            radioGroup.check(btnAnnouncements.getId());
                            viewPager.setCurrentItem(announcementToScrollTo);

                        }
                    } else if (ScreenSaver.currentState.stateType == StateType.ELECTRICITY) {
                        radioGroup.check(btnElectricity.getId());
                    } else if (ScreenSaver.currentState.stateType == StateType.INTRO) {
                        radioGroup.check(btnIntroduction.getId());
                    } else if (ScreenSaver.currentState.stateType == StateType.WATER) {
                        radioGroup.check(btnWater.getId());
                    } else if (ScreenSaver.currentState.stateType == StateType.TEMPERATURE) {
                        radioGroup.check(btnTemperature.getId());
                    } else if (ScreenSaver.currentState.stateType == StateType.GAS) {
                        radioGroup.check(btnGas.getId());
                    }


                }
            });
        }
    }

}
