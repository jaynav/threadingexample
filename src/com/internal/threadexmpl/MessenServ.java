package com.internal.threadexmpl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.*;
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
    protected int mVal= 1;
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
                   // this is needed because the messenger service sends data prior to being registered
                    setHandlerData();
                    break;

                case MSG_Un:
                    mClient.remove(msg.replyTo);
                    // android debugger does not come here to debug. this is the only way or than logging on debugger
                    Toast.makeText(getApplicationContext(),"I unregistered",Toast.LENGTH_LONG).show();
                    break;

                case MSG_Set_V:
                    String deString = msg.getData().getString(Setter.URLValue);
                    manipulateURL(deString, msg);
                     break;
                default:
                    super.handleMessage(msg);
            }
        }


    }

    /**
     * gives it the ok to send the data from messenger test
     * this sets a arg valu of 1 in the incoming handler of messengerTest, thus allowing it to send the data back
     * after it registered
     *
     */
    private  void setHandlerData()
    {
        for (int i=mClient.size()-1; i>=0; i--) {
            try {
                mClient.get(i).send(Message.obtain(null,
                        MSG_Reg, mVal, 0));
            } catch (RemoteException e) {
                // The client is dead.  Remove it from the list;
                // we are going through the list from back to front
                // so this is safe to do inside the loop.
                mClient.remove(i);
            }
        }
    }

    /**
     *
     * @param deString site URL
     * @param msg future messenger data manipulaton
     */
    private void manipulateURL(String deString, Message msg)
    {
        for(int i= mClient.size()-1; i >=0; i--)
       {

           try
           {
               Bundle mani = new Bundle();
               Message ms = Message.obtain(null, MSG_Set_V,mani);
               mani.putString(Setter.URLValue,deString +"nah nah nah nah");
               ms.setData(mani);
               mClient.get(i).send(ms);

           }
           catch (RemoteException ex)
           {
               mClient.remove(i);
               ex.printStackTrace();
           }
       }
    }

    /**
     * messenger needed to handle messenger service from messengertest.java
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * required binder for messenger
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent)
    {
        return mMessenger.getBinder();
    }

    /**
     * default when created
     * add future notification.builder();
     */
    @Override
    public  void onCreate()
    {
      //  Toast.makeText(getApplicationContext(),"I started messenserv", Toast.LENGTH_SHORT).show();
    }

    /**
     * default when service stops
     */
    @Override
    public void onDestroy()
    {
        Toast.makeText(this,"service STopped",Toast.LENGTH_LONG).show();
    }

}
