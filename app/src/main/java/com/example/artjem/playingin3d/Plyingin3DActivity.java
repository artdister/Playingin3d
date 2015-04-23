package com.example.artjem.playingin3d;
//Test commit Part2


import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.GLSLShader;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

public class Plyingin3DActivity  extends ActionBarActivity  {
    private static Plyingin3DActivity master = null;

    private GLSurfaceView mGLView;
    private MyRenderer renderer = null;
    private FrameBuffer fb = null;
    private InputTouchHandler intouchHandle = null;
    private World world = null;
    private RGBColor back = new RGBColor(50, 50, 100);

    PointF touchPoint = null;

    private CamerObj cam;
    private PlanetObj planet = null;

    private Object3D space = null;
    private SimpleVector rotateVec = new SimpleVector();
    private int fps = 0;

    private static float rotate = 0;


    private FrameBuffer buffer;
    private GLSLShader shader = null;
    private Light sun = null;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        Logger.log("onCreate");

        if (master != null) {
            copy(master);
        }

        super.onCreate(savedInstanceState);
        mGLView = new GLSurfaceView(getApplication());

        mGLView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                // Ensure that we get a 16bit framebuffer. Otherwise, we'll fall
                // back to Pixelflinger on some device (read: Samsung I7500)
                int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };
                EGLConfig[] configs = new EGLConfig[1];
                int[] result = new int[1];
                egl.eglChooseConfig(display, attributes, configs, 1, result);
                return configs[0];
            }
        });



        intouchHandle = new InputTouchHandler();
        renderer = new MyRenderer();
        mGLView.setRenderer(renderer);
        setContentView(mGLView);

       // super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_plyingin3d);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void copy(Object src) {
        try {
            Logger.log("Copying data from master Activity!");
            Field[] fs = src.getClass().getDeclaredFields();
            for (Field f : fs) {
                f.setAccessible(true);
                f.set(this, f.get(src));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean onTouchEvent(MotionEvent me) {
        int pointerIndex = me.getActionIndex();

        int maskedAction = me.getActionMasked();
        Logger.log("HALLO");
        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:{
                intouchHandle.actionDown(me);
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                intouchHandle.actionPointerDown(me,pointerIndex);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                touchPoint = intouchHandle.actionMove(me);
                break;
            }

            case MotionEvent.ACTION_UP:{
                intouchHandle.actionUp(me);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:{
                intouchHandle.actionPointerUp(me, pointerIndex);
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                // TODO use data
                break;
            }
        }


        return true;


    }

    protected boolean isFullscreenOpaque() {
        return true;
    }

    class MyRenderer implements GLSurfaceView.Renderer {

        private long time = System.currentTimeMillis();

        public MyRenderer() {
        }

        public void onSurfaceChanged(GL10 gl, int w, int h) {

            if (fb != null) {
                fb.dispose();
            }
            fb = new FrameBuffer(gl, w, h);

            if (master == null) {

                touchPoint = new PointF();

                world = new World();

                sun = new Light(world);
                sun.enable();

                sun.setIntensity(127, 127, 255);
                sun.setPosition(SimpleVector.create(0, 0, 200));

                world.setAmbientLight(0, 0, 0);




                TextureManager.getInstance().addTexture("skyline", new Texture(BitmapHelper.rescale(BitmapHelper.convert(getResources().getDrawable(R.drawable.skyline)),512,512)));


                try{
                    space = Object3D.mergeAll(Loader.load3DS(getResources().getAssets().open("planet.3ds"),10));
                    space.setTexture("skyline");
                    space.setScale(30);
                    space.setCulling(false);
                    space.strip();

                    space.build();


                }catch (IOException e){

                }
                world.addObject(space);


                planet = new PlanetObj(getApplicationContext(), world, "earth");


                String vertex=Loader.loadTextFile(getResources().openRawResource(R.raw.vertexshader_offset));
                String fragment=Loader.loadTextFile(getResources().openRawResource(R.raw.fragmentshader_offset));

                shader=new GLSLShader(vertex, fragment);
                shader.setStaticUniform("colorMap", 0);
                shader.setStaticUniform("normalMap", 0);
                shader.setStaticUniform("invRadius", 10.0005f);

                //planet.setRenderHook(shader);



                cam = new CamerObj(world);
                SimpleVector sv = new SimpleVector();
               // sv.set(planet.getTransformedCenter());
                sv.y = 10;
                sv.z -= 10;
               // sun.setPosition(sv);
                MemoryHelper.compact();

                if (master == null) {
                    Logger.log("Saving master Activity!");
                    master = Plyingin3DActivity.this;
                }
            }
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        public void onDrawFrame(GL10 gl) {

            cam.onRendering(touchPoint.x, touchPoint.y);
            cam.focusonPlanet(planet.getPlanetObj());

            //sun.setPosition(cam.getCamPos());
            rotate = rotate -0.000005f;
            planet.getPlanetObj().rotateX(rotate);


            fb.clear(back);
            world.renderScene(fb);
            world.draw(fb);
            fb.display();

            if (System.currentTimeMillis() - time >= 1000) {
               // Logger.log(fps + "fps");
                fps = 0;

                time = System.currentTimeMillis();
            }
            fps++;
        }
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plyingin3_d, menu);
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
    }*/


}
