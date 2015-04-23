package com.example.artjem.playingin3d;

import android.graphics.PointF;
import android.view.MotionEvent;

import com.threed.jpct.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artjem on 16.04.2015.
 */
public class InputTouchHandler {

    private static List<PointF> touchPointList;
    private static PointF touch;


    public InputTouchHandler(){
        touchPointList = new ArrayList<PointF>();
        touch = new PointF();
    }


    public static void actionDown(MotionEvent me){
        touch.x = me.getX();
        touch.y = me.getY();
        touchPointList.add(0,touch);
    }

    public static void actionUp(MotionEvent me){
        touchPointList.remove(0);
    }

    public static void actionPointerDown(MotionEvent me, int pointerIndex){
        touch.x = me.getX(pointerIndex)- touchPointList.get(0).x;
        touch.y = me.getY(pointerIndex)- touchPointList.get(0).y;

        touchPointList.add(pointerIndex,touch);

    }

    public static void actionPointerUp(MotionEvent me, int pointerIndex){
        touchPointList.remove(pointerIndex);
    }

    public static PointF actionMove(MotionEvent me){
        float xd = me.getX() - touchPointList.get(0).x;
        float yd = me.getY() - touchPointList.get(0).y;

        touchPointList.get(0).x = me.getX();
        touchPointList.get(0).y = me.getY();

        if(touchPointList.size() == 2) {
            Logger.log("   pointerId  " +touchPointList.get(touchPointList.size()-1).length());
        }
        xd = xd /10f;
        yd = yd / 100f;

        return new PointF(xd,yd);

    }
}
