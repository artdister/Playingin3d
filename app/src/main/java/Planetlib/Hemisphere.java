package Planetlib;

import android.content.Context;

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

    }




    public void setTransparent(int na){

        this.getPlanetObj().setTransparency(na);
    }

}
