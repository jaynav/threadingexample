package com.internal.threadexmpl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by DerpPC on 3/17/2015.
 */
public class MessenServ extends Service
{
    ArrayList<Messenger>mClient = new ArrayList<Messenger>();
    protected static final int MSG_Set_V =1;
    protected static final int MSG_Reg = 2;
    protected static final int MSG_Un = 3;
    protected int mVal= 0;
    //use wakelock or wifi lock to avoid android to go to sleep and use alarm manager  to schedule events at interval
    //or when something you want happens

    //the handler
    class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_Reg:
                    // Messenger where replies to this message can be sent. The semantics of
                    // exactly how this is used are up to the sender and receiver.
                    mClient.add(msg.replyTo);
                    // this registers last for some reason
                    Toast.makeText(getApplicationContext(),"I registered",Toast.LENGTH_LONG).show();
                    break;
                case MSG_Un:
                    mClient.remove(msg.replyTo);
                    // android debugger does not come here to debug. this is the only way or than logging on debugger
                    Toast.makeText(getApplicationContext(),"I unregistered",Toast.LENGTH_LONG).show();
                    break;
                case MSG_Set_V:
                    String deString = msg.getData().getString(Setter.URLValue);
                    manipulateURL(deString);
                    Toast.makeText(getApplicationContext(),"I set theValue to: "+ deString,Toast.LENGTH_LONG).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }


    }

    private void manipulateURL(String deString)
    {
        Toast.makeText(getApplicationContext(),"I manipulated this value in service "+ deString,Toast.LENGTH_LONG).show();

    }


    final Messenger mMessenger = new Messenger(new IncomingHandler());
    @Override
    public IBinder onBind(Intent intent)
    {
        return mMessenger.getBinder();
    }

    @Override
    public  void onCreate()
    {
        Toast.makeText(getApplicationContext(),"I started messenserv", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDestroy()
    {
        Toast.makeText(this,"service STopped",Toast.LENGTH_LONG).show();
    }

}
