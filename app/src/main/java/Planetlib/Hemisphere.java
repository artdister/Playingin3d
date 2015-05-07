package Planetlib;

import android.content.Context;

import com.threed.jpct.TextureInfo;
import com.threed.jpct.TextureManager;

/**
 * Created by Artjem on 04.05.2015.
 */


public class Hemisphere extends SphereObj {
    private int a;
    public static String planetTexture = null;
    public Hemisphere(Context ctx,String planetTexture, float size, int alpha){

        super(ctx,size);
        this.planetTexture = planetTexture;
        this.a = alpha;


        this.getPlanetObj().setTransparency(this.a);
        loadTexture(planetTexture,"H","hights");

        TextureInfo ti = new TextureInfo(TextureManager.getInstance().getTextureID(planetTexture));
        ti.add(TextureManager.getInstance().getTextureID(planetTexture+"H"), TextureInfo.MODE_MODULATE);
       // this.getPlanetObj().setTexture(ti);
//
    }




    public void setTransparent(int na){

        this.getPlanetObj().setTransparency(na);
    }

}
