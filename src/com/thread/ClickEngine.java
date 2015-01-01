package com.thread;

import android.text.Editable;
import com.thread.MyActivity.GenDownloader;
/**
 * Created by DerpPC on 12/31/2014.
 */
public class ClickEngine
{
    public static void startTask(Editable text, GenDownloader downloader)
    {


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
        return "read:"+size+" bytes"+ "at"+(size*1000)/(1*1024)+"Kbps";
    }


}
