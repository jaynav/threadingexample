package com.internal.threadexmpl;

/**
 * Created by DerpPC on 2/7/2015.
 */

import com.internal.threadexmpl.MyActivity.GenDownloader;
public class ClickEngine
{
    public static void startTask(CharSequence text, GenDownloader downloader)
    {

        //runs async execute which calls doinbackground
        downloader.execute(text);

    }




    public static void stopTask(MyActivity.GenDownloader downloader)
    {

        if(downloader != null)
        {
            //refers to the cancel method on the async parent class
            downloader.cancel(true);
        }


    }

    //find out how many bits were read
    public static CharSequence textStatus(int size, long timePassed)
    {
        if(timePassed ==0)
        {
            timePassed = 1;
        }
        return "read: "+size+" bytes "+ "at: "+(((size*8)/1000)/timePassed)+"Kbps";

    }



}
