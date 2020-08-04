package com.example.hangmangame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MyCanvas extends View {


    private Paint paint,paintBody;
    private String partOfBody;
    private float arcLeft,arcRight;
    private Path mPath;
    Canvas canvas;


    public MyCanvas(Context context, String partOfBody) {
        super(context);
        drawTheLines(context);
        settingsForBase();
        settingsForPartsOfBody();
        this.partOfBody = partOfBody;
    }

    public void setPartOfBody(String partOfBody) {
        this.partOfBody = partOfBody;
    }

    public void settingsForBase() {
        paintBody= new Paint();
        paintBody.setAntiAlias(true);
        paintBody.setStyle(Paint.Style.STROKE);
        paintBody.setColor(Color.RED);
        paintBody.setStrokeWidth(15);
    }

    public void settingsForPartsOfBody() {
        paint= new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(15);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        // create the base
        canvas.drawLine(50,50,50,700,paint);
        canvas.drawLine(50,50,450,50,paint);
        canvas.drawLine(450,50,450,100,paint);
        canvas.drawPath(mPath,paint);
        // create the base

        switch (partOfBody) {
            case "rightFoot":
                drawTheRightFoot();
            case "leftFoot":
                drawTheLeftFoot();
            case "rightHand":
                drawTheRightHand();
            case "leftHand":
                drawTheLeftHand();
            case "body":
                drawTheBody();
            case "head":
                drawTheHead();
        }
    }

    protected void drawTheLines(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        arcLeft = pxFromDp(context,20);
        arcRight = pxFromDp(context,1);
        Point p1 = new Point((int) pxFromDp(context,80)+ (screenWidth/2),(int) pxFromDp(context,40));
        mPath = new Path();
        mPath.moveTo(p1.x,p1.y);
        mPath.close();
    }

    protected void drawTheHead(){

        canvas.drawCircle(450, 150, arcLeft, paintBody);
        canvas.drawCircle(430, 145, arcRight, paintBody);
        canvas.drawCircle(470, 145, arcRight, paintBody);
        canvas.drawLine(430,175,470,175,paintBody);
        canvas.drawLine(450,130,450,160,paintBody);
    }

    protected void drawTheBody(){
        canvas.drawLine(450,210,450,400,paintBody);
    }

    protected void drawTheLeftHand(){
        canvas.drawLine(450,250,400,330,paintBody);
    }

    protected void drawTheRightHand(){
        canvas.drawLine(450,250,500,330,paintBody);
    }

    protected void drawTheLeftFoot(){
        canvas.drawLine(450,390,410,450,paintBody);
    }

    protected void drawTheRightFoot(){
        canvas.drawLine(450,390,490,450,paintBody);
    }

    protected static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}
