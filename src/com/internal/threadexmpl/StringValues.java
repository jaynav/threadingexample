package com.internal.threadexmpl;

import android.os.Bundle;

/**
 * Created by DerpPC on 2/11/2015.
 */
public class StringValues
{
    //Commands to the service to display
    static final int Msg_Register = 1;
    static final int Msg_UnRT= 2;
    static final int Msg_Set_Val = 3;

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

    /*public static <typeofBundle> Bundle bMaker(typeofBundle txt)
    {
        return null;
    }
    */
}
