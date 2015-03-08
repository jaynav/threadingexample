package com.internal.threadexmpl;
import android.app.Activity;
import android.content.*;
import android.os.*;
import android.text.Editable;
import android.view.*;
import android.widget.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // request a progress bar// has to be called before adding content
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.main);

        urlTextTest = (EditText)findViewById(R.id.editText);
        statusTextTest= (TextView)findViewById(R.id.textView);
        downloadButton = (Button)findViewById(R.id.button);
        stopButton = (Button)findViewById(R.id.button2);
        progressBarTest = (ProgressBar)findViewById(R.id.progressBar);
        goSlowCBX = (CheckBox)findViewById(R.id.checkBox);
        serviceCBX = (CheckBox)findViewById(R.id.cbxService);


        // set value of starting progress bar
        progressBarTest.setVisibility(View.INVISIBLE);


    }

    //on resume goes here
    @Override
    public void onResume()
    {
        super.onResume();

        SharedPreferences dahpref = this.getSharedPreferences("dahPref", Context.MODE_PRIVATE);
        taskKilled = dahpref.getBoolean("restart",false);

        if(taskKilled)
        {
            urlTextTest.setText(dahpref.getString("urlText","blah did not load"));
            downloader = new GenDownloader();
            ClickEngine.startTask(UrlCheck.isUrlCorrect(urlTextTest.getText()), downloader);
            statusTextTest.setText("restarted download of" + urlTextTest.getText());
        }
    }

    //on pause goes here
    @Override
    public void onPause()
    {

        super.onPause();
        ClickEngine.stopTask(downloader);

        SharedPreferences dahpref = this.getSharedPreferences("dahPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor ads = dahpref.edit();
        ads.putString("urlText", urlTextTest.getText().toString());
        ads.putBoolean("restart",taskKilled);
        ads.commit();
        statusTextTest.setText("Android OS paused the task automatically");

    }

    //recreate an activity
    //on save goes here
    @Override
    public void onSaveInstanceState(Bundle outSaveState)
    {
        outSaveState.putString("urlText", String.valueOf(urlTextTest.getText()));

        super.onSaveInstanceState(outSaveState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        urlTextTest.setText(savedInstanceState.getString("urlText"));
    }
    //on destroy goes here
    public void  onDestroy()
    {
        super.onDestroy();
        statusTextTest.setText("task was killed by Android");

    }
/////////////////////////////////////////////////////////////////////action bar/context menu/action mode

    @Override
    //action bar
    public boolean onCreateOptionsMenu(Menu derMenu)
    {
        getMenuInflater().inflate(R.menu.main, derMenu);
        return true;
    }

    public  boolean onOptionsItemSelected(MenuItem itm)
    {
        switch (itm.getItemId())
        {
            case R.id.actionBar_settings:
                // startPreferenceDetail();
                return  true;
            default:
                return super.onOptionsItemSelected(itm);
        }
    }

    //////////////////////////////////////////////////////////context menu/actionMode
    public boolean onItemLongClick(AdapterView<?> view,View row, int position, long id)
    {
        return  true;
    }

    //@Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu)
    {
        mode.getMenuInflater().inflate(R.menu.contextual_action_mode, menu);
        mode.setTitle("edit");
        return true;
    }

    //@Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
    {
        return false;
    }

    //  @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.contextualAction_remove:
                return true;
            default:
                return false;
        }
    }

    // @Override
    public void onDestroyActionMode(ActionMode mode)
    {
        //
    }
    //////////////////////////////////////////////////////////////////////////////////inner class async demo/background thread
    protected class GenDownloader extends AsyncTask<CharSequence,CharSequence,CharSequence>
    {
        //for this to work correctly, it needs to implement the abstracted method+ following 4. not sure why the other 4 are not
        //abstracted methods since they have nothing on asyncTask.java

        protected void onPreExecute()
        {
            //run ui thread first on setup. grab state of chbx
            goSlow = goSlowCBX.isChecked();
        }

        //... is variable arguments/  acts like parameter arrays in C#(params keyword) similar pattern
        //The three periods after the final parameter's type indicate that the final argument
        // may be passed as an array or as a sequence of arguments // was not taught in my java classes ;\
        @Override
        protected CharSequence doInBackground(CharSequence... urlParams)
        {
            //must also call publishProgress()  or ui thread will not be able to communicate with background thread
            //run on background//no ui interface here//indirect ui call

            String stat = "";
            int tSize =0;
            //needed to read from file stream or network
            InputStream inStream = null;
            long startTime = System.currentTimeMillis();
            long lastT = startTime;


            //Thrown when a waiting thread is activated before the condition it was waiting for has been satisfied.
            try
            {

                URL derUrl = new URL(urlParams[0].toString());
                URLConnection connectInternet = derUrl.openConnection();
                connectInternet.setConnectTimeout(1000);
                connectInternet.connect();


                inStream = connectInternet.getInputStream();

                //read and discard counter
                byte[] readBuffer = new byte[1024];
                int length;


                while ((length = inStream.read(readBuffer, 0, readBuffer.length)) != -1)
                {

                    tSize += length;
                    long rightnow = System.currentTimeMillis();
                    //to slow down internet connection
                    if (goSlow)
                    {
                        Thread.sleep(100);
                    }

                    //update the ui based on time passed
                    if ((rightnow - lastT) >= 250)
                    {
                        publishProgress(ClickEngine.textStatus(tSize, rightnow - startTime));
                    }
                    stat = "done loading";
                }
            }
            catch (InterruptedException ex)
            {
                stat = "there was the following interruption: " + ex.getMessage();
                errorInRead = true;
            }
            catch (IOException ex)
            {
                stat = "there was the following network error: " + ex.getMessage();
                errorInRead =true;

            }
            catch (SecurityException ex)
            {
                stat = "there was a security execption" + ex.getMessage();
                errorInRead = true;
            }
            finally
            {
                try
                {
                    if (inStream != null)
                    {
                        inStream.close();
                    }

                }
                catch (IOException ignore)
                {
                    stat = "Network error occurred when closing : " + ignore.getMessage();
                    errorInRead = true;
                }
            }


            if (stat.equals("done loading"))
            {
                return stat + ClickEngine.textStatus(tSize, System.currentTimeMillis()-startTime);
            }
            else
            {
                return stat;
            }

        }



        protected void onProgressUpdate(CharSequence...status)
        {
            // publishProgress() calls it
            //push data to UI
            statusTextTest.setText(status[0]);

        }

        protected void onPostExecute(CharSequence status)
        {
            //called on final result/ui update

            setUIR(false);
            statusTextTest.setText(status);
            downloader =null;
            if(errorInRead)
            {
                taskKilled = true;
            }
            else
            {
                taskKilled = false;
            }

        }

        protected void onCancelled(CharSequence status)
        {
            statusTextTest.setText("was cancelled" + status);
            downloader = null;
            if(btnStop)
            {
                taskKilled = false;
            }
            else
            {
                taskKilled =true;
            }
            //called when cancelled
        }
        //to slow connection speed
        private boolean goSlow;
        private boolean errorInRead;
    }

    public void stopDownloadT(View view)
    {

        ClickEngine.stopTask(downloader);
        setUIR(false);
        statusTextTest.setText(statusTextTest.getText()+" was stopped by the user");
        taskKilled = false;
        btnStop = true;
    }

    public void eraseMe(View view)
    {
        String data = urlTextTest.getText().toString();
        if(data.equals("site URL"))
        {
            urlTextTest.setText("");
        }
    }

    public void dasServicer(View view)
    {
        if(serviceCBX.isChecked())
        {
            Setter.setValue(true);
        }
        else
        {
            Setter.setValue(false);
        }
    }
////////////////////////////////////////////////////// other methods

    /**
     *
     * @param isRunning if it is running disable buttons or enable buttons
     */
    private void setUIR(boolean isRunning)
    {
        downloadButton.setEnabled(!isRunning);
        stopButton.setEnabled(isRunning);

        //progress bar set its state to visible if running, invisible if not running
        setProgressBarIndeterminateVisibility(isRunning);
        //if running is true, then set visible else invisible
        progressBarTest.setVisibility(isRunning?View.VISIBLE:View.INVISIBLE);

    }

    ///////////////////////////////////////////////////////////////////////////////////clickable buttons
    public void startDownloadT(View derView)
    {
        //ui update call
        setUIR(true);
        downloader = new GenDownloader();
        //if not checked start async task if checked start as service
        //may also implement wakelock/wifilock forkeeping devices running
        if(!Setter.getValue())
        {
            //some logic hidden in different class to start async task. too much stuff here
            ClickEngine.startTask(UrlCheck.isUrlCorrect(urlTextTest.getText()), downloader);
            statusTextTest.setText("started download of" + urlTextTest.getText());
            taskKilled = true; //to handle onPause
        }
        else


















        {


        }
    }



//////////////////////////////////////////////////////////////////////////////the service





    private class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case exampServ.Msg_Set_Val:
                String derString = msg.getData().getString(StringValues.bdlString);
                    statusTextTest.setText(derString +"was loaded");
                    break;
                default:
                    super.handleMessage(msg);

            }
        }
    }



    ///////////////////////////////////////////////////////////////variables

    private EditText urlTextTest;
    private TextView statusTextTest;
    private Button downloadButton, stopButton;
    private ProgressBar progressBarTest;
    private GenDownloader downloader;

    protected boolean taskKilled,btnStop = false;

    private CheckBox goSlowCBX;
    private CheckBox  serviceCBX;

    //messenger for communicating with the service
    Messenger msgServicer= null;
    // Flag indicating whether we have called bind on the service.
    boolean mBound;
    //handling incoming messages
    final Messenger mMessenger = new Messenger(new IncomingHandler());



}
