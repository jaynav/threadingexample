package com.thread;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
    private class GenDownloader extends AsyncTask<CharSequence,CharSequence,CharSequence>
    {
        //for this to workc orrectly, it needs to implement the abstracted method+ following 4. not sure why the other 4 are not
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
}
