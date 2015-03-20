package com.internal.threadexmpl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

;
import android.widget.Toast;

/**
 * Created by DerpPC on 3/17/2015.
 */
public class MessengerTest extends Activity
{
    Messenger mServ= null;
    boolean mIsB;
    //for getting value from intent
    String siteName;
    private static final String Save_Row = "saverow";


    class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        // this is part of handler can't rename. thats why it did not work was never implemented. doh
        {
            switch (msg.what)
            {
                case MessenServ.MSG_Set_V:
                    Toast.makeText(getApplicationContext(), "I got this from service" + msg.arg1, Toast.LENGTH_LONG).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    //service interation
    private ServiceConnection mConnect = new ServiceConnection()
    {
        @Override
        //called when established
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            mServ = new Messenger(service);
            Toast.makeText(getApplicationContext(),"i Attached",Toast.LENGTH_SHORT).show();

            //monitor Service while connected

            try
            {
                Message msg = Message.obtain(null,MessenServ.MSG_Reg);
                msg.replyTo = mMessenger;
                mServ.send(msg);
            }
            catch (RemoteException exceptional)
            {
                exceptional.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name)
                //called when service crashes
        {
            mServ = null;
            Toast.makeText(getApplicationContext(), "i Crashed/disconnected",Toast.LENGTH_SHORT).show();

        }

    };

    protected void bindServ()
    {



        bindService(new Intent(this,MessenServ.class),mConnect,Context.BIND_AUTO_CREATE);
        mIsB = true;
        Toast.makeText(getApplicationContext(),"binding now",Toast.LENGTH_SHORT).show();

        sendServiceData(siteName);

    }

    protected void sendServiceData(String siteName)
    {
        if(mIsB)
        {
            if (mServ!=null)
            {
                try
                {

                    Message ms = Message.obtain(null,MessenServ.MSG_Set_V);
                    Bundle dahBun = new Bundle();
                    dahBun.putString(Setter.URLValue,siteName);
                    ms.setData(dahBun);
                    mServ.send(ms);

                }
                catch (RemoteException fail)
                {
                    fail.printStackTrace();
                }
            }
        }
    }


    public void unBindServ()
    {
        if(mIsB)
        {
            if(mServ != null)
            {
                try
                {
                    Message ms = Message.obtain(null,MessenServ.MSG_Un);
                    ms.replyTo = mMessenger;
                    mServ.send(ms);
                }
                catch (RemoteException excp)
                {
                    excp.printStackTrace();
                }
            }
            //detach service
            unbindService(mConnect);
            mIsB = false;
            Toast.makeText(getApplicationContext(),"disconnected from servic",Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onCreate(Bundle savedInState)
    {
        super.onCreate(savedInState);
        setContentView(R.layout.messenger_test);

        if(savedInState== null)
        {
            Bundle xtra = getIntent().getExtras();
            if(xtra != null && xtra.containsKey(Setter.Extra_Row))
            {
                siteName = (String)xtra.getCharSequence(Setter.Extra_Row);
            }
            else
            {
                siteName = savedInState.getString(Save_Row);
            }
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outSave)
    {
        super.onSaveInstanceState(outSave);
        outSave.putString(Save_Row,siteName);
    }
    public void startService(View view)
    {
        if(!binderAgo)
        {
            bindServ();
            binderAgo= true;
        }
        else
        {
            unBindServ();
            binderAgo = false;
        }

    }
    boolean binderAgo= false;
}
