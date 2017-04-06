package com.ble.sharan;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;

public class MusicPlayer /*extends AppCompatActivity*/
{

    String TAG=MusicPlayer.class.getSimpleName();
    //AudioManager mAudioManager;

   /* @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

       // mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

    }*/

    public static MusicPlayer instance = null;

    public static MusicPlayer getInstance()
    {
        if (instance == null)
        {
            instance = new MusicPlayer();
        }
        return instance;
    }


    //public void previous(View v)
    public void previous(Context context)
    {

        refresh(context);
        long eventtime = SystemClock.uptimeMillis();
        Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
        KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
        context.sendOrderedBroadcast(downIntent, null);


      /*  KeyEvent downEvent = new KeyEvent(eventtime,  eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
        mAudioManager.dispatchMediaKeyEvent(downEvent);*/
    }


    //public void playPause(View v)
    public void playPause(Context context)
    {

        refresh(context);
        long eventtime = SystemClock.uptimeMillis();
        Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
        KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
        context.sendOrderedBroadcast(downIntent, null);

        Log.e(TAG, "Hello");

    }


    //public void next(View v)
    public void next(Context context)
    {

        refresh(context);
        long eventtime = SystemClock.uptimeMillis();
        Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
        KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT, 0);
        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
        context.sendOrderedBroadcast(downIntent, null);

        /*KeyEvent downEvent = new KeyEvent(eventtime,  eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT, 0);
        mAudioManager.dispatchMediaKeyEvent(downEvent);*/

    }


    public void refresh(Context context)
    {
        long eventtime = SystemClock.uptimeMillis();
        Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
        KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
        upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
        context.sendOrderedBroadcast(upIntent, null);

    }
}
