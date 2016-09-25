package com.prvprojects.paginationdapp.paginationview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by reena on 9/24/2016.
 */
public class PaginationView extends LinearLayout{

    private int currentPageNumber; // Page numbers are from 1 to x
    private int totalNumberOfPages;

    private int textColor; //  \Should be a color

    LinearLayout rootView;
    private ImageView ivHome;
    private ImageView ivPrevious;
    private TextView tvPageNumber;
    private ImageView ivNext;
    private ImageView ivLast;

    boolean isNextPageButtonPressed = false;
    boolean isPreviousPageButtonPressed = false;
    private final long REPEAT_INTERVAL_MS = 100l;
    Handler handler = new Handler();

    public interface PageSelectedInterface{
        void onPageChangedListener(int newCurrentPageNumber);
    }

    PageSelectedInterface pageSelectedInterface = null;

    public PaginationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PaginationView, 0, 0);
        textColor = ta.getColor(R.styleable.PaginationView_textColor, Color.BLACK);
        ta.recycle();

        rootView = (LinearLayout)inflate(context, R.layout.paginationview, this);
        rootView.setWeightSum(getResources().getInteger(R.integer.weightsum_rootview));

        ivHome = (ImageView) rootView.findViewById(R.id.paginationview_iv_home);
        ivPrevious = (ImageView) rootView.findViewById(R.id.paginationview_iv_previous);
        tvPageNumber = (TextView) rootView.findViewById(R.id.paginationview_tv_pagenumber);
        ivNext = (ImageView) rootView.findViewById(R.id.paginationview_iv_next);
        ivLast = (ImageView) rootView.findViewById(R.id.paginationview_iv_last);

        currentPageNumber = 0;
        totalNumberOfPages = 0;
        tvPageNumber.setTextColor(textColor);
        AppCompatActivity temp = null;
        try{
            temp = (AppCompatActivity) getContext();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final AppCompatActivity tempRef = temp;
        tvPageNumber.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalNumberOfPages>=3) {
                    if(tempRef!=null) {
                        PageSelectorDialog temp = PageSelectorDialog.newInstance(1, totalNumberOfPages, currentPageNumber);
                        temp.setPageNumberSelectedListener(new PageSelectorDialog.PageNumberSelectedListener() {
                            @Override
                            public void onPageNumberSelected(int pageNumberSelected) {
                                if(currentPageNumber!=pageNumberSelected){
                                    //currentPageNumber = pageNumberSelected;
                                    updateCurrentPageNumber(pageNumberSelected);
                                }
                            }
                        });
                        temp.show(tempRef.getSupportFragmentManager(), "Page Selector");
                    }

                }
            }
        });

        ivHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToFirstPage();
            }
        });

        ivPrevious.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToPreviousPage();
            }
        });

        ivPrevious.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isPreviousPageButtonPressed = true;
                handler.post(new MoveToPreviousPage());
                return false;
            }
        });

        ivPrevious.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    stopFastMovementOfPages();
                }
                return false;
            }
        });

        ivNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNextPage();
            }
        });

        ivNext.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isNextPageButtonPressed = true;
                handler.post(new MoveToNextPage());
                return false;
            }
        });
        ivNext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    stopFastMovementOfPages();
                }
                return false;
            }
        });

        ivLast.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToLastPage();
            }
        });

        //read xml attributes
//        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ValueBar, 0, 0);
//        barHeight = ta.getDimensionPixelSize(R.styleable.ValueBar_barHeight, 0);
//        circleRadius = ta.getDimensionPixelSize(R.styleable.ValueBar_circleRadius, 0);
//        spaceAfterBar = ta.getDimensionPixelSize(R.styleable.ValueBar_spaceAfterBar, 0);
//        circleTextSize = ta.getDimensionPixelSize(R.styleable.ValueBar_circleTextSize, 0);
//        maxValueTextSize = ta.getDimensionPixelSize(R.styleable.ValueBar_maxValueTextSize, 0);
//        labelTextSize = ta.getDimensionPixelSize(R.styleable.ValueBar_labelTextSize, 0);
//        labelTextColor = ta.getColor(R.styleable.ValueBar_labelTextColor, Color.BLACK);
//        currentValueTextColor = ta.getColor(R.styleable.ValueBar_maxValueTextColor, Color.BLACK);
//        circleTextColor = ta.getColor(R.styleable.ValueBar_circleTextColor, Color.BLACK);
//        baseColor = ta.getColor(R.styleable.ValueBar_baseColor, Color.BLACK);
//        fillColor = ta.getColor(R.styleable.ValueBar_fillColor, Color.BLACK);
//        labelText = ta.getString(R.styleable.ValueBar_labelText);
//        ta.recycle();
//
//        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        labelPaint.setTextSize(labelTextSize);
//        labelPaint.setColor(labelTextColor);
//        labelPaint.setTextAlign(Paint.Align.LEFT);
//        labelPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//
//        maxValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        maxValuePaint.setTextSize(maxValueTextSize);
//        maxValuePaint.setColor(currentValueTextColor);
//        maxValuePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        maxValuePaint.setTextAlign(Paint.Align.RIGHT);
//
//        barBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        barBasePaint.setColor(baseColor);
//
//        barFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        barFillPaint.setColor(fillColor);
//
//        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        circlePaint.setColor(fillColor);
//
//        currentValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        currentValuePaint.setTextSize(circleTextSize);
//        currentValuePaint.setColor(circleTextColor);
//        currentValuePaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setPageSelectedInterface(PageSelectedInterface pageSelectedInterface1){
        pageSelectedInterface = pageSelectedInterface1;
    }

    public void setTotalNumberOfPages(int totalNumberOfPages1){
        totalNumberOfPages = totalNumberOfPages1;
        if(totalNumberOfPages>0) {
            //currentPageNumber = 1;
            updateCurrentPageNumber(1);
        }
    }

    public int getTotalNumberOfPages(){
        return totalNumberOfPages;
    }

    public void setCurrentPageNumber(int currentPageNumber1){
        if(currentPageNumber1<totalNumberOfPages) {
            //currentPageNumber = currentPageNumber1;
            updateCurrentPageNumber(currentPageNumber1);
        }
    }

    public int getCurrentPageNumber(){
        return currentPageNumber;
    }

    private void moveToNextPage(){
        if(currentPageNumber>=totalNumberOfPages)
            ;
        else {
            //currentPageNumber++;
            updateCurrentPageNumber(currentPageNumber+1);
        }
    }

    private void moveToPreviousPage(){
        if(currentPageNumber<=1)
            ;
        else {
            //currentPageNumber--;
            updateCurrentPageNumber(currentPageNumber-1);
        }
    }

    private void moveToFirstPage(){
        if(currentPageNumber<=1)
            ;
        else
            //currentPageNumber=1;
            updateCurrentPageNumber(1);
    }

    private void moveToLastPage(){
        if(currentPageNumber>=totalNumberOfPages)
            ;
        else
            //currentPageNumber = totalNumberOfPages;
            updateCurrentPageNumber(totalNumberOfPages);
    }

    private void updateUi(){

        if(currentPageNumber<=1){
            stopFastMovementOfPages();
            tvPageNumber.setText("1 of " + String.valueOf(totalNumberOfPages));
            ivHome.setEnabled(false);
            ivPrevious.setEnabled(false);
            if(totalNumberOfPages>currentPageNumber) {
                ivNext.setEnabled(true);
                ivLast.setEnabled(true);
            } else {
                ivNext.setEnabled(false);
                ivLast.setEnabled(false);
            }
        } else if(currentPageNumber<totalNumberOfPages){
            tvPageNumber.setText(String.valueOf(currentPageNumber)+" of "+String.valueOf(totalNumberOfPages));
            ivHome.setEnabled(true);
            ivPrevious.setEnabled(true);
            ivNext.setEnabled(true);
            ivLast.setEnabled(true);
        }else {
            stopFastMovementOfPages();
            tvPageNumber.setText(String.valueOf(totalNumberOfPages)+" of "+String.valueOf(totalNumberOfPages));
            ivNext.setEnabled(false);
            ivLast.setEnabled(false);
            if(totalNumberOfPages>1) {
                ivPrevious.setEnabled(true);
                ivHome.setEnabled(true);
            } else {
                ivPrevious.setEnabled(false);
                ivHome.setEnabled(false);
            }
        }
    }

    private class MoveToNextPage implements Runnable {
        @Override
        public void run() {
            if(isNextPageButtonPressed){
                moveToNextPage();
                handler.postDelayed(new MoveToNextPage(), REPEAT_INTERVAL_MS);
            }
        }
    }

    private class MoveToPreviousPage implements Runnable {
        @Override
        public void run() {
            if(isPreviousPageButtonPressed){
                moveToPreviousPage();
                handler.postDelayed(new MoveToPreviousPage(), REPEAT_INTERVAL_MS);
            }
        }
    }

    private void stopFastMovementOfPages(){
        isPreviousPageButtonPressed = false;
        isNextPageButtonPressed = false;
    }

    private void updateCurrentPageNumber(int newPageNumber){
        currentPageNumber = newPageNumber;
        updateUi();
        if(pageSelectedInterface!=null) {
            pageSelectedInterface.onPageChangedListener(currentPageNumber);
        }
    }

}
