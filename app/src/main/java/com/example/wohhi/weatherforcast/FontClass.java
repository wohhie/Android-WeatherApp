package com.example.wohhi.weatherforcast;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by wohhi on 2/23/2017.
 */

public class FontClass{

    public static Typeface getLightFont(Context context){
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/basictitlefont.ttf");
        return font;
    }
}
