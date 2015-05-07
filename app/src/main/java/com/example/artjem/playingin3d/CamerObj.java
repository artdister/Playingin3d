package com.example.artjem.playingin3d;

import android.util.FloatMath;
import android.view.MotionEvent;

import com.threed.jpct.Camera;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.Matrix;

/**
 * Created by Artjem on 09.04.2015.
 */
public class CamerObj {

    private static Camera cam;
    private static SimpleVector rotateVec;
    private static SimpleVector rotateCenter;
    private static Matrix m = new Matrix();
    static float xAxis;
    static float yAxis;
    static float distance;

    public CamerObj(World world){

        rotateVec = new SimpleVector();
        rotateCenter = new SimpleVector();
        cam = world.getCamera();

        xAxis = 0;
        yAxis = 0;
        distance = 30;
        cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);


    }

    public static void onRendering(float x,float y){


        if(x != xAxis){
            xAxis += x;
        }
        if(y != yAxis && yAxis >= -30 && yAxis <= 30){
            yAxis += y;
        }
        if(yAxis > 30) {
            yAxis = 30;
        }
        else if(yAxis < -30){
            yAxis = -30;
        }


            rotateVec.x =(distance * -FloatMath.sin(xAxis * ((float) Math.PI / 180)) * FloatMath.cos((yAxis) * ((float) Math.PI / 180)))+rotateCenter.x;
            rotateVec.y =(distance * -FloatMath.sin((yAxis) * ((float) Math.PI / 180)))+rotateCenter.y;
            rotateVec.z =(-distance * FloatMath.cos((xAxis) * ((float) Math.PI / 180)) * FloatMath.cos((yAxis) * ((float) Math.PI / 180)))+rotateCenter.z;
            cam.setPosition(rotateVec.x, rotateVec.y, rotateVec.z);


   /*     rotateVec.set(-x, 0, 0);
        m = rotateVec.normalize(rotateVec).getRotationMatrix(m);
        m.rotateAxis(m.getXAxis(), (float) -Math.PI / 2f);
        cam.moveCamera(Camera.CAMERA_MOVEIN, 50); // 50 is the distance from the cube's center that the camera has
        cam.rotateCameraAxis(m.invert3x3().getXAxis(), -rotateVec.length() / 15f); // 5f actually depends on render speed
        cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
    */
    }


    public static void setRotateCenter(SimpleVector newRotateCenter){
        rotateCenter = newRotateCenter;
    }
    public static void focusonPlanet(Object3D planet){
        cam.lookAt(planet.getTransformedCenter());
    }

    public static SimpleVector getCamPos(){return rotateVec;}

}
