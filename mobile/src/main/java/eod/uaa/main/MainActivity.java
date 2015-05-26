package eod.uaa.main;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import eod.uaa.animation.SlideAnimation;
import eod.uaa.announcement.ImageAdapter;

public class MainActivity extends Activity {
    //public ANNOUNCEMENTS ANNOUNCEMENTS;
    public ImageView Introduction;
    public LinearLayout oneLayout;
    public LinearLayout webLayout;
    public XYPlot onePlot;
    viewEnum CurrentView = viewEnum.INTRO;
    private RadioGroup radioGroup;
    private RadioButton btnIntroduction;
    private RadioButton btnElectricity;
    private RadioButton btnWater;
    private RadioButton btnGas;
    private RadioButton btnTemperature;
    private RadioButton btnAnnouncements;
    private ImageAdapter imageAdapter;
    private Timer screenTimer;
    private Timer moverTimer;
    private TimerTask screenTimerTask;
    private TimerTask moverTimerTask;
    private WebView webView;
    private ImageView forwardArrow;
    private ImageView backArrow;
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
        btnAnnouncements = (RadioButton) findViewById(R.id.btn_announcements);

        Introduction = (ImageView) findViewById(R.id.introduction);
        oneLayout = (LinearLayout) findViewById(R.id.oneLayout);
        onePlot = (XYPlot) findViewById(R.id.tempPlot);

        forwardArrow = (ImageView) findViewById(R.id.forwardarrow);
        backArrow = (ImageView) findViewById(R.id.backarrow);

        forwardArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView != null) {
                    webView.goForward();
                    updateArrows();
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goBack();
                updateArrows();
            }
        });

        webLayout = (LinearLayout) findViewById(R.id.weblayout);
        webView = null;
        //createWebView();

        initFadeAnimations();

        Introduction.setVisibility(View.VISIBLE);
        webLayout.setVisibility(View.GONE);


//
//        JSONObject Edemand;
//        Edemand = JSONfunctions.getJSONfromURL("http://cannxs.org/eod/queries/ElectricityDemand.php");
//        JSONObject Eusage;
//        Eusage = JSONfunctions.getJSONfromURL("http://cannxs.org/eod/queries/ElectricityUsage.php");
//        JSONObject Gconsumption;
//        Gconsumption = JSONfunctions.getJSONfromURL("http://cannxs.org/eod/queries/GasConsumption.php");
//        JSONObject Wusage;
//        Wusage = JSONfunctions.getJSONfromURL("http://cannxs.org/eod/queries/WaterUsage.php");
//        JSONObject Otemperature;
//        Otemperature = JSONfunctions.getJSONfromURL("http://cannxs.org/eod/queries/OutsideTemperature.php");

    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        super.dispatchTouchEvent(ev);
//
//        if (screenTimer != null)
//            screenTimer.cancel();
//        screenTimer = new Timer();
//        if (screenTimerTask != null)
//            screenTimerTask.cancel();
//
//        screenTimerTask = new ScreenTimerTask();
//        screenTimer.schedule(screenTimerTask, 30000);
//
//        if (moverTimer != null)
//            moverTimer.cancel();
//
//        return true;
//    }
//
//    private void initTimers() {
//        screenTimerTask = new ScreenTimerTask();
//        moverTimerTask = new MoverTimerTask();
//        screenTimer = new Timer();
//        screenTimer.schedule(screenTimerTask, 30000);
//    }


    private void initFadeAnimations() {
        SlideAnimation slideAnimation = new SlideAnimation();
        final SlideAnimation finalFadeAnimation = slideAnimation;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup rGroup, int checkedId) {

                if (checkedId == R.id.btn_introduction) {
                    finalFadeAnimation.slideFromTop(Introduction);
                    if (CurrentView == viewEnum.ELECLAYOUT) {
                        finalFadeAnimation.slideToBottom(oneLayout);
                    } else if (CurrentView == viewEnum.WATERLAYOUT) {
                        finalFadeAnimation.slideToBottom(oneLayout);
                    } else if (CurrentView == viewEnum.GASLAYOUT) {
                        finalFadeAnimation.slideToBottom(oneLayout);
                    } else if (CurrentView == viewEnum.TEMPLAYOUT) {
                        finalFadeAnimation.slideToBottom(oneLayout);
                    } else {//announcement
                        finalFadeAnimation.slideToBottom(webLayout);
                    }
                    CurrentView = viewEnum.INTRO;

                } else if (checkedId == R.id.btn_electricity) {

                    if (CurrentView == viewEnum.INTRO) {
                        finalFadeAnimation.slideFromTop(oneLayout);
                        finalFadeAnimation.slideToBottom(Introduction);
                    } else if (CurrentView == viewEnum.WATERLAYOUT) {
                    } else if (CurrentView == viewEnum.GASLAYOUT) {
                    } else if (CurrentView == viewEnum.TEMPLAYOUT) {
                    } else {//announcement
                        finalFadeAnimation.slideFromTop(oneLayout);
                        finalFadeAnimation.slideToBottom(webLayout);
                        //  finalFadeAnimation.slideToBottom(ANNOUNCEMENTS);

                    }
                    onePlot.setVisibility(View.GONE);
                    initGraphLayout(viewEnum.ELECLAYOUT);
                    onePlot.setVisibility(View.VISIBLE);
                    CurrentView = viewEnum.ELECLAYOUT;

                } else if (checkedId == R.id.btn_water) {

                    //   finalFadeAnimation.slideFromTop(waterLayout);
                    if (CurrentView == viewEnum.INTRO) {
                        finalFadeAnimation.slideFromTop(oneLayout);
                        finalFadeAnimation.slideToBottom(Introduction);
                        //      finalFadeAnimation.slideToBottom(Introduction);
                    } else if (CurrentView == viewEnum.ELECLAYOUT) {
                    } else if (CurrentView == viewEnum.GASLAYOUT) {
                    } else if (CurrentView == viewEnum.TEMPLAYOUT) {
                    } else {//announcement
                        finalFadeAnimation.slideFromTop(oneLayout);
                        finalFadeAnimation.slideToBottom(webLayout);
                        //         finalFadeAnimation.slideToBottom(ANNOUNCEMENTS);
                    }
                    onePlot.setVisibility(View.GONE);
                    initGraphLayout(viewEnum.WATERLAYOUT);
                    onePlot.setVisibility(View.VISIBLE);
                    CurrentView = viewEnum.WATERLAYOUT;

                } else if (checkedId == R.id.btn_temperature) {

                    //   finalFadeAnimation.slideFromTop(tempLayout);
                    if (CurrentView == viewEnum.INTRO) {
                        finalFadeAnimation.slideFromTop(oneLayout);
                        finalFadeAnimation.slideToBottom(Introduction);
                        //             finalFadeAnimation.slideToBottom(Introduction);
                    } else if (CurrentView == viewEnum.ELECLAYOUT) {
                    } else if (CurrentView == viewEnum.WATERLAYOUT) {
                    } else if (CurrentView == viewEnum.GASLAYOUT) {
                    } else {//announcement
                        finalFadeAnimation.slideFromTop(oneLayout);
                        finalFadeAnimation.slideToBottom(webLayout);
                    }
                    onePlot.setVisibility(View.GONE);
                    initGraphLayout(viewEnum.TEMPLAYOUT);
                    onePlot.setVisibility(View.VISIBLE);
                    CurrentView = viewEnum.TEMPLAYOUT;

                } else if (checkedId == R.id.btn_gas) {

                    if (CurrentView == viewEnum.INTRO) {
                        finalFadeAnimation.slideFromTop(oneLayout);
                        finalFadeAnimation.slideToBottom(Introduction);
                        //       finalFadeAnimation.slideToBottom(Introduction);
                    } else if (CurrentView == viewEnum.ELECLAYOUT) {
                        onePlot.clear();
                    } else if (CurrentView == viewEnum.WATERLAYOUT) {
                        onePlot.clear();
                    } else if (CurrentView == viewEnum.TEMPLAYOUT) {
                        onePlot.clear();
                    } else {//announcement
                        finalFadeAnimation.slideFromTop(oneLayout);
                        finalFadeAnimation.slideToBottom(webLayout);
                    }
                    onePlot.setVisibility(View.GONE);
                    initGraphLayout(viewEnum.GASLAYOUT);
                    onePlot.setVisibility(View.VISIBLE);
                    CurrentView = viewEnum.GASLAYOUT;

                } else if (checkedId == R.id.btn_announcements) {
                    createWebView();
                    finalFadeAnimation.slideFromTop(webLayout);
                    if (CurrentView == viewEnum.INTRO) {
                        finalFadeAnimation.slideToBottom(Introduction);
                    } else if (CurrentView == viewEnum.ELECLAYOUT) {
                        finalFadeAnimation.slideToBottom(oneLayout);
                        onePlot.clear();
                    } else if (CurrentView == viewEnum.WATERLAYOUT) {
                        finalFadeAnimation.slideToBottom(oneLayout);
                        onePlot.clear();
                    } else if (CurrentView == viewEnum.GASLAYOUT) {
                        finalFadeAnimation.slideToBottom(oneLayout);
                        onePlot.clear();
                    } else { //tempPlot
                        finalFadeAnimation.slideToBottom(oneLayout);
                        onePlot.clear();
                        //         finalFadeAnimation.slideToBottom(tempLayout);
                    }
                    CurrentView = viewEnum.ANNOUNCEMENTS;
                    onePlot.setVisibility(View.GONE);
                }
            }
        });
    }

    public void initGraphLayout(viewEnum CurrentView) {
        if (CurrentView == viewEnum.ELECLAYOUT) {

            //Must get data and generate arrays.
            Number[] readings = {7596, 7887.8, 7744.7, 7318.8, 7263.3, 7779.1, 7921.4, 7747.8, 7650.9, 7684.2, 7439.7, 7257.9, 7713.6, 5948.1, 0, 7393.0, 7082.6, 7178.9, 7581.4, 7599.8, 7501.1, 7476.0, 7536.5, 7159.4, 7141.3, 7141.3}; //Readings
            Number[] time = {1427921100, 1428007500, 1428093900, 1428180300, 1428266700, 1428353100, 1428439500, 1428525900, 1428612300, 1428698700, 1428785100, 1428871500, 1428957900, 1429044300, 1429130700, 1429217100, 1429303500, 1429389900, 1429476300, 1429562700, 1429649100, 1429735500, 1429821900, 1429908300, 1429994700, 1430081100};
            // initialize our XYPlot reference:
            onePlot = (XYPlot) findViewById(R.id.elecUsagePlot);

            XYSeries myPlot = new SimpleXYSeries(Arrays.asList(time), Arrays.asList(readings), "Electricity Usage Plot");

            LineAndPointFormatter plotFormat = new LineAndPointFormatter();
            plotFormat.setPointLabelFormatter(new PointLabelFormatter());
            plotFormat.configure(getApplicationContext(), R.xml.line_point_formatter_with_elec);

            onePlot.getLegendWidget().setWidth(0); //Disable Legend
            //------- Range Domain Format -----
            onePlot.setRangeBoundaries(0, 15000, BoundaryMode.FIXED); //RANGE Boundaries*********

            onePlot.addSeries(myPlot, plotFormat);
            onePlot.setRangeValueFormat(new DecimalFormat("0"));
            onePlot.setDomainValueFormat(new Format() {
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
            onePlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
            onePlot.getBackgroundPaint().setColor(Color.WHITE);

        } else if (CurrentView == viewEnum.WATERLAYOUT) {
            Number[] readings = {4773, 4695, 5647, 2865, 2729, 4858, 4208, 4048, 4311, 4098, 2611, 2251, 4165, 1262, 0, 0, 896, 2263, 2749, 3991, 4064, 3834, 3875, 4006, 2624, 2423}; //Readings
            Number[] time = {1427921100, 1428007500, 1428093900, 1428180300, 1428266700, 1428353100, 1428439500, 1428525900, 1428612300, 1428698700, 1428785100, 1428871500, 1428957900, 1429044300, 1429130700, 1429217100, 1429303500, 1429389900, 1429476300, 1429562700, 1429649100, 1429735500, 1429821900, 1429908300, 1429994700, 1430081100};
            // initialize our XYPlot reference:
            onePlot = (XYPlot) findViewById(R.id.waterPlot);

            XYSeries myPlot = new SimpleXYSeries(Arrays.asList(time), Arrays.asList(readings), "Water Usage Plot");

            LineAndPointFormatter plotFormat = new LineAndPointFormatter();
            plotFormat.setPointLabelFormatter(new PointLabelFormatter());
            plotFormat.configure(getApplicationContext(), R.xml.line_point_formatter_with_water);

            onePlot.getLegendWidget().setWidth(0); //Disable Legend
            //------- Range Domain Format -----
            onePlot.setRangeBoundaries(0, 8000, BoundaryMode.FIXED); //RANGE Boundaries*********

            onePlot.addSeries(myPlot, plotFormat);
            onePlot.setRangeValueFormat(new DecimalFormat("0"));
            onePlot.setDomainValueFormat(new Format() {
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
            onePlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
            onePlot.getBackgroundPaint().setColor(Color.WHITE);
        } else if (CurrentView == viewEnum.TEMPLAYOUT) {

            Number[] readings = {38, 40, 40.5, 34.5, 36.5, 41.0, 42.5, 37.0, 38.0, 42.0, 37.5, 37.5, 34.5, 32.0, 41.0, 40.5, 42.0, 44.5, 45.5, 37.0, 40.0, 39.5, 38.5, 40.5, 43.0, 47.0}; //Readings
            Number[] time = {1427921100, 1428007500, 1428093900, 1428180300, 1428266700, 1428353100, 1428439500, 1428525900, 1428612300, 1428698700, 1428785100, 1428871500, 1428957900, 1429044300, 1429130700, 1429217100, 1429303500, 1429389900, 1429476300, 1429562700, 1429649100, 1429735500, 1429821900, 1429908300, 1429994700, 1430081100};
            // initialize our XYPlot reference:
            onePlot = (XYPlot) findViewById(R.id.tempPlot);

            XYSeries myPlot = new SimpleXYSeries(Arrays.asList(time), Arrays.asList(readings), "Temperature Plot");

            LineAndPointFormatter plotFormat = new LineAndPointFormatter();
            plotFormat.setPointLabelFormatter(new PointLabelFormatter());
            plotFormat.configure(getApplicationContext(), R.xml.line_point_formatter_with_temp);

            onePlot.getLegendWidget().setWidth(0); //Disable Legend
            //------- Range Domain Format -----
            onePlot.setRangeBoundaries(0, 100, BoundaryMode.FIXED); //RANGE Boundaries*********

            onePlot.addSeries(myPlot, plotFormat);
            onePlot.setRangeValueFormat(new DecimalFormat("0"));
            onePlot.setDomainValueFormat(new Format() {
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
            onePlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
            onePlot.getBackgroundPaint().setColor(Color.WHITE);
        } else if (CurrentView == viewEnum.GASLAYOUT) {
            Number[] readings = {63786, 72134, 73667, 71013, 81663, 65626, 38419, 41251, 34825, 34911, 47882, 47259, 43357, 22972, 0, 0, 9097, 31011, 22479, 32676, 43711, 41873, 37342, 28893, 30372, 28488}; //Readings
            Number[] time = {1427921100, 1428007500, 1428093900, 1428180300, 1428266700, 1428353100, 1428439500, 1428525900, 1428612300, 1428698700, 1428785100, 1428871500, 1428957900, 1429044300, 1429130700, 1429217100, 1429303500, 1429389900, 1429476300, 1429562700, 1429649100, 1429735500, 1429821900, 1429908300, 1429994700, 1430081100};

            // initialize our XYPlot reference:
            onePlot = (XYPlot) findViewById(R.id.gasPlot);

            XYSeries myPlot = new SimpleXYSeries(Arrays.asList(time), Arrays.asList(readings), "Gas Usage Plot");

            LineAndPointFormatter plotFormat = new LineAndPointFormatter();
            plotFormat.setPointLabelFormatter(new PointLabelFormatter());
            plotFormat.configure(getApplicationContext(), R.xml.line_point_formatter_with_gas);

            onePlot.getLegendWidget().setWidth(0); //Disable Legend
            //------- Range Domain Format -----
            onePlot.setRangeBoundaries(0, 100000, BoundaryMode.FIXED); //RANGE Boundaries*********

            onePlot.addSeries(myPlot, plotFormat);
            onePlot.setRangeValueFormat(new DecimalFormat("0"));
            onePlot.setDomainValueFormat(new Format() {
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
            onePlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
            onePlot.getBackgroundPaint().setColor(Color.WHITE);
        }
        onePlot.redraw();


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
        ANNOUNCEMENTS
    }

    private void createWebView() {
        if (webView != null) {
            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank");
            webView.freeMemory();
            webView.pauseTimers();
            webView = null;
        }

        webView = (WebView) findViewById(R.id.webview);
        webView.clearHistory();
        webView.loadUrl("http://www.uaa.alaska.edu/");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                updateArrows();
            }
        });
        updateArrows();
    }

    private void updateArrows()
    {
        if(webView != null)
        {
            if(webView.canGoBack())
            {
                Log.d("", "t");
                backArrow.setImageResource(R.drawable.backarrow);
            }
            else
            {
                backArrow.setImageResource(R.drawable.backarrowgrey);
            }

            if(webView.canGoForward())
            {
                forwardArrow.setImageResource(R.drawable.forwardarrow);
            }
            else
            {
                forwardArrow.setImageResource(R.drawable.forwardarrowgrey);
            }
        }
    }
}





//    private class ScreenTimerTask extends TimerTask {
//        @Override
//        public void run() {
//            // start screen saver
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (moverTimer != null)
//                        moverTimer.cancel();
//                    moverTimer = new Timer();
//                    moverTimerTask = new MoverTimerTask();
//                    moverTimer.schedule(moverTimerTask, 5000, 5000);
//                }
//            });
//        }
//    }
//
//    private class MoverTimerTask extends TimerTask {
//        @Override
//        public void run() {
//            // screen saver code
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ScreenSaver.changeState();
//                    int announcementToScrollTo = ScreenSaver.currentAnnouncement;
//
//
//                    if (ScreenSaver.currentState.stateType == StateType.ANNOUNCEMENTS) {
//                        if (announcementToScrollTo == 0) {
//                            ANNOUNCEMENTS.setCurrentItem(0);
//                            radioGroup.check(btnAnnouncements.getId());
//                        } else {
//                            // no animation needed, just change page
//                            radioGroup.check(btnAnnouncements.getId());
//                            ANNOUNCEMENTS.setCurrentItem(announcementToScrollTo);
//
//                        }
//                    } else if (ScreenSaver.currentState.stateType == StateType.ELECTRICITY) {
//                        radioGroup.check(btnElectricity.getId());
//                    } else if (ScreenSaver.currentState.stateType == StateType.INTRO) {
//                        radioGroup.check(btnIntroduction.getId());
//                    } else if (ScreenSaver.currentState.stateType == StateType.WATER) {
//                        radioGroup.check(btnWater.getId());
//                    } else if (ScreenSaver.currentState.stateType == StateType.TEMPERATURE) {
//                        radioGroup.check(btnTemperature.getId());
//                    } else if (ScreenSaver.currentState.stateType == StateType.GAS) {
//                        radioGroup.check(btnGas.getId());
//                    }
//
//
//                }
//            });
//        }
//    }



