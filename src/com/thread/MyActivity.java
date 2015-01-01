package com.thread;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

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
        /*

    private ProgressBar progressBarTest;

    private CheckBox goSlow;*/

    }

    //on resume goes here
    @Override
    public void onResume()
    {
        super.onResume();
    }

    //on pause goes here
    @Override
    public void onPause()
    {
        super.onPause();
       ClickEngine.stopTask(downloader);
    }

    //on save goes here
    @Override
    public void onSaveInstanceState(Bundle outSaveState)
    {
        super.onSaveInstanceState(outSaveState);
    }

    //on destroy goes here
    public void  onDestroy()
    {
        super.onDestroy();
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
    }
//////////////////////////////////////////////////////////////////////////////////inner class async demo/background thread
    protected class GenDownloader extends AsyncTask<CharSequence,CharSequence,CharSequence>
    {
        //for this to work correctly, it needs to implement the abstracted method+ following 4. not sure why the other 4 are not
        //abstracted methods since they have nothing on asyncTask.java

        protected void onPreExecute()
        {

        }

        //... is variable arguments/  acts like parameter arrays in C#(params keyword) similar pattern
        //The three periods after the final parameter's type indicate that the final argument
        // may be passed as an array or as a sequence of arguments // was not taught in my java classes ;\
        @Override
        protected CharSequence doInBackground(CharSequence... params)
        {
           //must also call publishProgress()  or ui thread will not be able to communicate with background thread
            return null;
        }



        protected void onProgressUpdate(CharSequence...status)
        {
            // publishProgress() calls it

        }

        protected void onPostExecute(CharSequence status)
        {
            //called on final res
        }

        protected void onCancelled()
        {
            //called when cancelled
        }


    }

    ///////////////////////////////////////////////////////////////////////////////////clickable buttons
    public void startDownloadT(View derView)
    {
        //ui update call
        setUIR(true);
        downloader = new GenDownloader();
        ClickEngine.startTask(urlTextTest.getText(),downloader);
        statusTextTest.setText("started download of" +urlTextTest.getText());
    }

    public void stopDownloadT(View view)
    {

        ClickEngine.stopTask(downloader);
        setUIR(false);
        statusTextTest.setText(statusTextTest.getText()+" was stopped by the user");
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


    ///////////////////////////////////////////////////////////////variables

    private EditText urlTextTest;
    private TextView statusTextTest;
    private Button downloadButton, stopButton;
    private ProgressBar progressBarTest;
    private GenDownloader downloader;

    private CheckBox goSlowCBX;

}
