package com.internal.threadexmpl;

import android.os.Bundle;

/**
 * Created by DerpPC on 2/11/2015.
 */
public class StringValues
{


    public  static String bdlString = "str1";

    /**
     *
     * @param urlText the url sending to service via a bundle
     * @return formatted bundle
     */
    public static Bundle bundleMaker(CharSequence urlText)
    {
        Bundle bn = new Bundle();
        bn.putCharSequence(bdlString,urlText);
        return bn;
    }


}
