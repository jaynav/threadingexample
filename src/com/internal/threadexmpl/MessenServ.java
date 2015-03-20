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
                    break;
                case MSG_Un:
                    mClient.remove(msg.replyTo);
                    break;
                case MSG_Set_V:
                    break;
                default:
                    super.handleMessage(msg);
            }
        }


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
