package com.example.artjem.playingin3d;

import android.content.Context;

import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureInfo;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;

import java.io.IOException;

/**
 * Created by Artjem on 23.04.2015.
 */
public class PlanetObj {

    private static Object3D planet = null;
    private static Context ctx = null;
    private static String planetTexture = null;


    public PlanetObj(Context ctx, World world, String planetTexture){

        this.ctx = ctx;
        this.planetTexture = planetTexture;

        loadTexture();
        loadTextueHights();
        loadTextureNormals();


        TextureInfo ti = new TextureInfo(TextureManager.getInstance().getTextureID(planetTexture));
        ti.add(TextureManager.getInstance().getTextureID(planetTexture+"N"), TextureInfo.MODE_MODULATE);



        try{
            planet = Object3D.mergeAll(Loader.load3DS(ctx.getResources().getAssets().open("planet2.3ds"), 10));
            planet.setTexture(ti);
            planet.setScale(1);
            planet.rotateX(-(float) Math.PI / 2);
            planet.setCulling(true);
           // planet.setAdditionalColor(107,107,255);
            planet.strip();
            planet.build();





        }catch (IOException e){

        }
        world.addObject(planet);




        planet.compile();
        planet.setSpecularLighting(true);
        planet.strip();
    }


    public static void loadTexture(){
        TextureManager.getInstance().addTexture(planetTexture,
                new Texture(BitmapHelper.rescale
                        (BitmapHelper.convert(ctx.getResources().getDrawable(ctx.getResources().
                                getIdentifier(planetTexture , "drawable", ctx.getPackageName()))),512, 512)));
    }
    public static void loadTextueHights(){
        TextureManager.getInstance().addTexture(planetTexture+"H",
                new Texture(BitmapHelper.rescale
                        (BitmapHelper.convert(ctx.getResources().getDrawable(ctx.getResources().
                                getIdentifier(planetTexture+"hights" , "drawable", ctx.getPackageName()))),512, 512)));
    }
    public static void loadTextureNormals(){
        TextureManager.getInstance().addTexture(planetTexture+"N",
                new Texture(BitmapHelper.rescale(BitmapHelper.convert(ctx.getResources().
                        getDrawable(ctx.getResources().getIdentifier(planetTexture+"normals" ,
                                "drawable", ctx.getPackageName()))), 512, 512)));}

    public static Object3D getPlanetObj(){
        return planet;
    }


}
