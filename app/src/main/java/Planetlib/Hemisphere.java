package Planetlib;

import android.content.Context;

import com.threed.jpct.TextureInfo;
import com.threed.jpct.TextureManager;

/**
 * Created by Artjem on 04.05.2015.
 */

import com.threed.jpct.Logger;
public class Hemisphere extends SphereObj {
    private int a;
    public static String planetTexture = null;
    public Hemisphere(Context ctx,String planetTexture, float size, int alpha){

        super(ctx,size);
        Hemisphere.planetTexture = planetTexture;
        this.a = alpha;


        loadTexture(planetTexture, "H","h");
        loadTexture(planetTexture,"HN","hn");

        TextureInfo ti = new TextureInfo(TextureManager.getInstance().getTextureID(planetTexture+"H"));
        ti.add(TextureManager.getInstance().getTextureID(planetTexture+"HN"), TextureInfo.MODE_MODULATE);

        this.getPlanetObj().setTexture(ti);
        this.getPlanetObj().setTransparency(this.a);


        this.getPlanetObj().strip();
        this.getPlanetObj().build();
    }




    public void setTransparent(int na){

        this.getPlanetObj().setTransparency(na);
    }

}
