package com.internal.threadexmpl;

/**
 * Created by DerpPC on 2/7/2015.
 */

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.*;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;


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
     // check first
     //   hyperSetter();

    }

   /** private void hyperSetter()
    {
        for(int i=messengerClients.size()-1;i>0;i--)
        {

            try
            {
    todo: retrieve data logic and send back results
                Bundle bdl = new Bundle();
                bdl.putString(StringValues.bdlString, "derp" + "some value to add here");
                Message msg = Message.obtain(null, StringValues.Msg_Set_Val);
                msg.setData(bdl);

                messengerClients.get(i).send(msg);
            }

            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
    }
*/
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
                case StringValues.Msg_Register:
                    Toast.makeText(getApplicationContext(),"registered",Toast.LENGTH_SHORT).show();
                    messengerClients.add(daMessage.replyTo);
                    break;
                case StringValues.Msg_UnRT:
                    Toast.makeText(getApplicationContext(),"unregistered", Toast.LENGTH_SHORT).show();
                    messengerClients.remove(daMessage.replyTo);
                    break;
                case StringValues.Msg_Set_Val:
                   // todo: retrieve data logic
                    break;
                default:
                    super.handleMessage(daMessage);
            }
        }
    }

    //target for clients to send messages to incoming handler
    final Messenger dasMessenger = new Messenger(new InboundHandler());



    ArrayList<Messenger> messengerClients = new ArrayList<Messenger>();
}
