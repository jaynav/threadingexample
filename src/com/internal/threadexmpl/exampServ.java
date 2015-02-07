package com.internal.threadexmpl;

/**
 * Created by DerpPC on 2/7/2015.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.os.Message;
import android.os.Messenger;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by DerpPC on 1/26/2015.
 */

//uses notification manager to send back message from service
public class exampServ extends Service
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i("msgr", "started service der");

    }
    @Override
    public int onStartCommand(Intent theStartedIntent, int theFlags, int startingId)
    {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        Toast.makeText(getApplicationContext(),"isBinding",Toast.LENGTH_SHORT).show();
        return dasMessenger.getBinder();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("srvc","serviceDestroyed");
    }


    //handler of incoming messages from client
    private  class InboundHandler extends Handler
    {
        @Override
        public  void handleMessage(Message daMessage)
        {
            switch (daMessage.what)
            {
                case Msg_Register:
                    //todo: add stuff
                    Toast.makeText(getApplicationContext(),"registered",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(daMessage);
            }
        }
    }

    //target for clients to send messages to incoming handler
    final Messenger dasMessenger = new Messenger(new InboundHandler());

    //Commands to the service to display
    static final int Msg_Register = 1;
}
