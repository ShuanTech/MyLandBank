package com.shuan.myland.property;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.support.annotation.NonNull;


import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.shuan.myland.R;
import com.shuan.myland.activities.BookingActivity;
import com.shuan.myland.activities.FacilitiesActivity;
import com.shuan.myland.activities.GalleryActivity;
import com.shuan.myland.adapter.PropertyImageAdapter;
import com.shuan.myland.list.PropertyItem;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.scrollView.ObservableScrollView;
import com.shuan.myland.scrollView.ObservableScrollViewCallbacks;
import com.shuan.myland.scrollView.ScrollState;
import com.shuan.myland.scrollView.ScrollUtils;
import com.shuan.myland.scrollView.TouchInterceptionFrameLayout;
import com.shuan.myland.utils.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import mehdi.sakout.fancybuttons.FancyButton;

public class PropertyView extends AppCompatActivity implements ObservableScrollViewCallbacks,View.OnClickListener{

    private Context mContext;
    private Common mApp;
    public String id,img;
    private static final String STATE_TRANSLATION_Y = "translationY";

    protected View mHeader;
    protected View mHeaderBar;
    private View mImageView;
    private View mHeaderBackground;
    private TextView mTitle;

    private TouchInterceptionFrameLayout mInterceptionLayout;

    // Fields that needs to saved
    private float mInitialTranslationY;

    // Fields that just keep constants like resource values
    protected int mActionBarSize;
    protected int mFlexibleSpaceImageHeight;
    protected int mIntersectionHeight;
    private int mHeaderBarHeight;

    // Temporary states
    private float mScrollYOnDownMotion;

    private float mPrevTranslationY;
    private boolean mGapIsChanging;
    private boolean mGapHidden;
    private boolean mReady;
    private View mToolbarView;
    private Toolbar toolbar;
    private ObservableScrollView scrollView;
    public ProgressBar progressBar;
    private HttpUrlConnectionParser parser=new HttpUrlConnectionParser();
    private php call=new php();
    public HashMap<String,String> proImgData=new HashMap<String,String>();
    private PropertyImageAdapter adapter;
    private ViewPager pro_img;
    public ArrayList<PropertyItem> proImg;
    public HashMap<String, String> proData;
    public ImageButton prev,next;
    private Button more;
    private FancyButton buy;
    private ImageButton img1, img2, img3,img4,img5;
    public String pro_id,Project_id,pro_type,plot_id,pro_name,pro_deatils,getPrice,area,a_type,booking_ammount,address,landmark,cofig,flooring,furnishing,car_parkng,overlooking,facing,water_avl,elecricty_avl,posted_date,construction_age,location,ownership,pro_description;
    public TextView proId,proDate, proDet, proPrice, proArea, plotId, proBookAmnt, proMaintanCharge, proAddr, proLand, proConfig, proFlooring, proStatus, proAge, proFurnish, proCarParkng, proOverlook, proFacing, proFloor, proWaterAvl, proEleAvl, proOwnership, proUnit, proLoanOffered, proDesc, proValue;
    public RelativeLayout row9,row10,row12,row13,row14,row16,row17,row20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mContext=getApplicationContext();
        mApp= (Common) mContext.getApplicationContext();
        id=getIntent().getStringExtra("id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_view);

        toolbar= (Toolbar) findViewById(R.id.app_bar);
        progressBar= (ProgressBar) findViewById(R.id.progress_bar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
        mActionBarSize = getActionBarSize();
        mHeaderBarHeight = getResources().getDimensionPixelSize(R.dimen.header_bar_height);

        // Even when the top gap has began to change, header bar still can move
        // within mIntersectionHeight.
        mIntersectionHeight = getResources().getDimensionPixelSize(R.dimen.intersection_height);
        mToolbarView = findViewById(R.id.app_bar);
        pro_img = (ViewPager) findViewById(R.id.img_pager);
        mImageView = findViewById(R.id.image);
        mHeader = findViewById(R.id.header);
        mHeaderBar = findViewById(R.id.header_bar);
        mHeaderBackground = findViewById(R.id.header_background);
        scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        prev= (ImageButton) findViewById(R.id.prev);
        next= (ImageButton) findViewById(R.id.next);
        plotId= (TextView) findViewById(R.id.pro_plot_id);
        scrollView.setScrollViewCallbacks(this);
        // mScrollable = createScrollable();

        mInterceptionLayout = (TouchInterceptionFrameLayout) findViewById(R.id.scroll_wrapper);
        mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText("Overview");
        ViewHelper.setTranslationY(mToolbarView, (mHeaderBarHeight - mActionBarSize) / 2);
        setTitle(null);


        if (savedInstanceState == null) {
            mInitialTranslationY = mFlexibleSpaceImageHeight - mHeaderBarHeight;
        }

        ScrollUtils.addOnGlobalLayoutListener(mInterceptionLayout, new Runnable() {
            @Override
            public void run() {
                mReady = true;
                updateViews(mInitialTranslationY, false);
            }
        });


        more = (Button) findViewById(R.id.more);

        proId= (TextView) findViewById(R.id.pro_id);
        proDate = (TextView) findViewById(R.id.pos);
        proDet = (TextView) findViewById(R.id.pro_det);
        proPrice = (TextView) findViewById(R.id.pro_val);
        proArea = (TextView) findViewById(R.id.pro_sqft);
        proValue = (TextView) findViewById(R.id.pro_price);
        proBookAmnt = (TextView) findViewById(R.id.pro_book_amnt);
        proAddr = (TextView) findViewById(R.id.pro_address);
        proLand = (TextView) findViewById(R.id.pro_landmark);
        proConfig = (TextView) findViewById(R.id.pro_config);
        proFlooring = (TextView) findViewById(R.id.pro_flooring);
        proStatus = (TextView) findViewById(R.id.pro_status);
        proAge = (TextView) findViewById(R.id.pro_age);
        proFurnish = (TextView) findViewById(R.id.pro_furnish);
        proCarParkng = (TextView) findViewById(R.id.pro_parking);
        proOverlook = (TextView) findViewById(R.id.pro_looking);
        proFacing = (TextView) findViewById(R.id.pro_facing);
        proOwnership = (TextView) findViewById(R.id.pro_owner);
        proWaterAvl = (TextView) findViewById(R.id.pro_water);
        proEleAvl = (TextView) findViewById(R.id.pro_electricity);
        proDesc = (TextView) findViewById(R.id.pro_desc);


        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
        img3 = (ImageButton) findViewById(R.id.img3);
        img4= (ImageButton) findViewById(R.id.img4);
        img5= (ImageButton) findViewById(R.id.img5);

        row9= (RelativeLayout) findViewById(R.id.row9);
        row10= (RelativeLayout) findViewById(R.id.row10);
        row12= (RelativeLayout) findViewById(R.id.row12);
        row13= (RelativeLayout) findViewById(R.id.row13);
        row14= (RelativeLayout) findViewById(R.id.row14);
        row16= (RelativeLayout) findViewById(R.id.row16);
        row17= (RelativeLayout) findViewById(R.id.row17);


        buy= (FancyButton) findViewById(R.id.buy);




        proImg = new ArrayList<PropertyItem>();
        new LoadProperty().execute();
        new LoadPropertyImg().execute();
        progressBar.setVisibility(View.VISIBLE);


        more.setOnClickListener(this);
        buy.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        prev.setOnClickListener(this);
        next.setOnClickListener(this);



    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mInitialTranslationY = savedInstanceState.getFloat(STATE_TRANSLATION_Y);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloat(STATE_TRANSLATION_Y, ViewHelper.getTranslationY(mInterceptionLayout));
        super.onSaveInstanceState(outState);
    }


    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
        @Override
        public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
            return getMinInterceptionLayoutY() < (int) ViewHelper.getY(mInterceptionLayout)
                    || (moving && scrollView.getCurrentScrollY() - diffY < 0);
        }

        @Override
        public void onDownMotionEvent(MotionEvent ev) {

            mScrollYOnDownMotion = scrollView.getCurrentScrollY();

        }

        @Override
        public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
            float translationY = ViewHelper.getTranslationY(mInterceptionLayout) - mScrollYOnDownMotion + diffY;
            float minTranslationY = getMinInterceptionLayoutY();
            if (translationY < minTranslationY) {
                translationY = minTranslationY;
            } else if (mFlexibleSpaceImageHeight - mHeaderBarHeight < translationY) {
                translationY = mFlexibleSpaceImageHeight - mHeaderBarHeight;
            }

            updateViews(translationY, true);
        }

        @Override
        public void onUpOrCancelMotionEvent(MotionEvent ev) {
        }
    };

    protected void updateViews(float translationY, boolean animated) {
        // If it's ListView, onScrollChanged is called before ListView is laid out (onGlobalLayout).
        // This causes weird animation when onRestoreInstanceState occurred,
        // so we check if it's laid out already.
        if (!mReady) {
            return;
        }
        ViewHelper.setTranslationY(mInterceptionLayout, translationY);

        // Translate image
        ViewHelper.setTranslationY(mImageView, (translationY - (mFlexibleSpaceImageHeight - mHeaderBarHeight)) / 2);

        // Translate title
        ViewHelper.setTranslationY(mToolbarView, Math.min(mIntersectionHeight, (mHeaderBarHeight - mActionBarSize) / 2));

        // Show/hide gap
        boolean scrollUp = translationY < mPrevTranslationY;
        if (scrollUp) {
            if (translationY <= mActionBarSize) {
                toolbar.setTitle(pro_deatils + " " + pro_type);
                changeHeaderBackgroundHeightAnimated(false, animated);

            }
        } else {
            if (mActionBarSize <= translationY) {
                changeHeaderBackgroundHeightAnimated(true, animated);
                toolbar.setTitle("");
            }
        }



        mPrevTranslationY = translationY;


    }

    private void changeHeaderBackgroundHeightAnimated(boolean shouldShowGap, boolean animated) {
        if (mGapIsChanging) {
            return;
        }
        final int heightOnGapShown = mHeaderBar.getHeight();
        final int heightOnGapHidden = mHeaderBar.getHeight() + mActionBarSize;
        final float from = mHeaderBackground.getLayoutParams().height;
        final float to;
        if (shouldShowGap) {
            if (!mGapHidden) {
                // Already shown

                return;
            }
            to = heightOnGapShown;
        } else {
            if (mGapHidden) {
                // Already hidden

                return;
            }
            to = heightOnGapHidden;
        }
        if (animated) {
            ViewPropertyAnimator.animate(mHeaderBackground).cancel();
            ValueAnimator a = ValueAnimator.ofFloat(from, to);
            a.setDuration(100);

            //Toast.makeText(getApplicationContext(),"WRK",Toast.LENGTH_SHORT).show();
            a.setInterpolator(new AccelerateDecelerateInterpolator());
            a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float height = (float) animation.getAnimatedValue();

                    changeHeaderBackgroundHeight(height, to, heightOnGapHidden);
                }
            });

            a.start();
        } else {

            changeHeaderBackgroundHeight(to, to, heightOnGapHidden);
        }
    }

    private void changeHeaderBackgroundHeight(float height, float to, float heightOnGapHidden) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mHeaderBackground.getLayoutParams();
        lp.height = (int) height;
        lp.topMargin = (int) (mHeaderBar.getHeight() - height);
        mHeaderBackground.requestLayout();
        mGapIsChanging = (height != to);
        if (!mGapIsChanging) {
            mGapHidden = (height == heightOnGapHidden);
        }
    }

    private float getMinInterceptionLayoutY() {
        return mActionBarSize - mIntersectionHeight;
        // If you want to move header bar to the top, return 0 instead.
        //return 0;
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public void onClick(View v) {
        Intent in;
        switch (v.getId()) {
            case R.id.more:
                showAlertDialog();
                break;
            case R.id.img1:
                in=new Intent(getApplicationContext(), FacilitiesActivity.class);
                in.putExtra("loc",location);
                startActivity(in);
                break;
            case R.id.img2:
                in=new Intent(getApplicationContext(), FacilitiesActivity.class);
                in.putExtra("loc",location);
                startActivity(in);
                break;
            case R.id.img3:
                in=new Intent(getApplicationContext(), FacilitiesActivity.class);
                in.putExtra("loc",location);
                startActivity(in);
                break;
            case R.id.img4:
                in=new Intent(getApplicationContext(), FacilitiesActivity.class);
                in.putExtra("loc",location);
                startActivity(in);
                break;
            case R.id.img5:
                in=new Intent(getApplicationContext(), FacilitiesActivity.class);
                in.putExtra("loc",location);
                startActivity(in);
                break;
            case R.id.buy:
                in=new Intent(getApplicationContext(),BookingActivity.class);
                in.putExtra("id",id);
                in.putExtra("pro_id",pro_id);
                startActivity(in);
                //startActivity(new Intent(getActivity(), BookingActivity.class));
                break;
            case R.id.prev:
                goPrev();
                break;
            case R.id.next:
                goNext();
                break;
        }
    }

    private void goNext() {


        int currentPage = pro_img.getCurrentItem();
        int totalPages = pro_img.getAdapter().getCount();

        int nextPage = currentPage+1;
        if (nextPage >= totalPages) {
            // We can't go forward anymore.
            // Loop to the first page. If you don't want looping just
            // return here.
            //nextPage = 0;
            prev.setVisibility(View.VISIBLE);
            next.setVisibility(View.GONE);
        }else {
            next.setVisibility(View.VISIBLE);

        }

        pro_img.setCurrentItem(nextPage, true);




    }

    private void goPrev() {
        int currentPage = pro_img.getCurrentItem();
        int totalPages = pro_img.getAdapter().getCount();

        int previousPage = currentPage-1;
        if (previousPage < 0) {
            // We can't go back anymore.
            // Loop to the last page. If you don't want looping just
            // return here.
            prev.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
        }else{
            prev.setVisibility(View.VISIBLE);

        }

        pro_img.setCurrentItem(previousPage, true);
    }


    private void showAlertDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myScrollView = inflater.inflate(R.layout.read_more, null, false);
        TextView tv = (TextView) myScrollView
                .findViewById(R.id.read_more_txt);
        tv.setText(pro_description);
        new AlertDialog.Builder(this).setView(myScrollView)
                .setTitle("Property Description")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }

                }).show();
    }




    public class LoadPropertyImg extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            proImgData = new HashMap<String, String>();
            proImgData.put("id", id);

            try {
                JSONObject json = parser.makeHttpUrlConnection(php.pro_sub_img, proImgData);
                int succ = json.getInt("success");

                if (succ == 0) {

                } else {
                    JSONArray jsonArray = json.getJSONArray("proImg");
                    JSONObject child = jsonArray.getJSONObject(0);
                    String man_img=child.optString("man_img");
                    String img = child.optString("sub_img");

                    proImg.add(new PropertyItem(man_img));

                    for (String ans : img.split(",")) {
                        proImg.add(new PropertyItem(ans));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new PropertyImageAdapter(getApplicationContext(), proImg);
                            pro_img.setAdapter(adapter);
                            adapter.setClickListener(new PropertyImageAdapter.ClickListener() {
                                @Override
                                public void itemClicked(View v, int pos) {
                                    Intent in=new Intent(getApplicationContext(), GalleryActivity.class);
                                    in.putExtra("id",id);
                                    in.putExtra("pos",pos);
                                    in.putExtra("from","property");
                                    startActivity(in);
                                }
                            });
                        }
                    });
                }

            } catch (Exception e) {
            }


            return null;
        }
    }





    public class LoadProperty extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            proData = new HashMap<String, String>();
            proData.put("id", id);

            try {
                JSONObject json = parser.makeHttpUrlConnection(php.pro_view, proData);

                int succ = json.getInt("success");

                if (succ == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "No Property Available", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    JSONArray jsonArray = json.getJSONArray("property");

                    JSONObject child = jsonArray.getJSONObject(0);
                    pro_id=child.optString("pro_id");
                    Project_id=child.optString("Project_id");
                    pro_type = child.optString("pro_type");
                    plot_id=child.optString("plot_id");
                    pro_name=child.optString("pro_name");
                    pro_deatils = child.optString("pro_deatils");
                    getPrice = child.optString("price");
                    area = child.optString("area");
                    a_type=child.optString("a_type");
                    booking_ammount = child.optString("booking_ammount");
                    address = child.optString("address");
                    landmark = child.optString("landmark");
                    cofig = child.optString("cofig");
                    flooring = child.optString("flooring");
                    furnishing = child.optString("furnishing");
                    car_parkng = child.optString("car_parkng");
                    overlooking = child.optString("overlooking");
                    facing = child.optString("facing");
                    water_avl = child.optString("water_avl");
                    elecricty_avl = child.optString("elecricty_avl");
                    posted_date = child.optString("posted_date");
                    construction_age = child.optString("construction_age");
                    location=child.optString("location");
                    ownership=child.optString("ownership");
                    pro_description = child.optString("pro_description");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (pro_type.equalsIgnoreCase("house/villa")) {
                            row14.setVisibility(View.GONE);
                        } else if (pro_type.equalsIgnoreCase("plot") || pro_type.equalsIgnoreCase("farms")) {
                            row9.setVisibility(View.GONE);
                            row10.setVisibility(View.GONE);
                            row12.setVisibility(View.GONE);
                            row13.setVisibility(View.GONE);
                            row14.setVisibility(View.GONE);
                            row16.setVisibility(View.GONE);


                        }

                        String outDate = null;
                        SimpleDateFormat getDate = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat ansDate = new SimpleDateFormat("MMM dd, yy");
                        try {
                            Date date = getDate.parse(posted_date);
                            outDate = ansDate.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        int len = getPrice.length();
                        String price = "";
                        int get = 0;

                        if (len <= 5) {
                            price = getPrice + " " + "K";

                        } else if (len >= 6 && len < 8) {
                            get = Integer.parseInt(getPrice);
                            price = Integer.toString((get / 100000)) + " " + "Lac";
                        } else {
                            get = Integer.parseInt(getPrice);
                            price = Integer.toString((get / 10000000)) + " " + "Cr";
                        }

                        proId.setText(pro_id);
                        proDate.setText(outDate);
                        proDet.setText(pro_deatils + " " + pro_type);
                        plotId.setText("Plot No: "+" "+plot_id);
                        proPrice.setText(getResources().getString(R.string.Rs) + " " + price);
                        proArea.setText(area + " " + a_type);
                        proValue.setText(getResources().getString(R.string.Rs) + " " + price);
                        proBookAmnt.setText(booking_ammount);
                        proAddr.setText(address);
                        proLand.setText(landmark);
                        proConfig.setText(cofig);
                        proFlooring.setText(flooring);
                        proStatus.setText("Ready to Move");
                        proAge.setText(construction_age);
                        proFurnish.setText(furnishing);
                        proCarParkng.setText(car_parkng);
                        proOverlook.setText(overlooking);
                        proFacing.setText(facing);
                        proWaterAvl.setText(water_avl);
                        proEleAvl.setText(elecricty_avl);
                        proOwnership.setText(ownership);
                        proDesc.setText(pro_description);
                    }
                });

            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


}
