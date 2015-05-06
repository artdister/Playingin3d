package Planetlib;

import android.content.Context;

import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureInfo;
import com.threed.jpct.TextureManager;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.World;


import java.io.IOException;
import java.util.Map;

/**
 * Created by Artjem on 23.04.2015.
 */
public class SphereObj {

    private static Object3D planet = null;
    public static Context ctx = null;



    public SphereObj(Context ctx, float size){
        this.ctx = ctx;



        try{
            planet = Object3D.mergeAll(Loader.load3DS(ctx.getResources().getAssets().open("planet2.3ds"), size));
            planet.setScale(1);
            planet.rotateX(-(float) Math.PI / 2);
            planet.setCulling(true);
            planet.strip();
            planet.build();

        }catch (IOException e){

        }


        planet.compile();
        planet.setSpecularLighting(true);
        planet.strip();

    }


    public static void loadTexture(String pt,String en, String et){
        TextureManager.getInstance().addTexture(pt+en, new Texture(BitmapHelper.rescale(BitmapHelper.convert(ctx.getResources().getDrawable(ctx.getResources().getIdentifier(pt+et, "drawable", ctx.getPackageName()))), 512, 512)));
    }


    public Object3D getPlanetObj(){
        return planet;
    }



}

