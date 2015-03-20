package com.internal.threadexmpl;

/**
 * Created by DerpPC on 2/7/2015.
 */
public class Setter
{

    protected static boolean serviceValue = false;

    protected static final String Extra_Row = "rowID";
    protected static final String URLValue = "SiteName";


    protected static void setValue(boolean value)
    {
        serviceValue = value;
    }


    public static boolean getValue()
    {
        return serviceValue;
    }
}
