package com.ble.sharan;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;

public class MusicPlayer /*extends AppCompatActivity*/
{

    String TAG = MusicPlayer.class.getSimpleName();
    AudioManager mAudioManager;

    Context context;

    boolean isPlayingInKarbon= false;

/*    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        context = this;

        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);


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


//    public void previous(View v)
    public void previous(Context context)
    {
        String deviceName = android.os.Build.MANUFACTURER;
        Log.e("Manufacture", deviceName);


        if (deviceName.contains("LENOVO") || deviceName.contains("vivo"))
        {
            refresh(context);
            long eventtime = SystemClock.uptimeMillis();
            Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
            KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
            downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
            context.sendOrderedBroadcast(downIntent, null);
        }
        else if(deviceName.contains("KARBONN"))
        {

            isPlayingInKarbon=true;

            refreshKarbon(context);

            long eventtime = SystemClock.uptimeMillis();
            Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
            KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
            downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
            context.sendOrderedBroadcast(downIntent, null);
        }
        else
        {
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
            mAudioManager.dispatchMediaKeyEvent(event);
        }


    }


//    public void playPause(View v)
    public void playPause(Context context)
    {
        String deviceName = android.os.Build.MANUFACTURER;
        Log.e("Manufacture", deviceName);


        if (deviceName.contains("LENOVO") || deviceName.contains("vivo"))
        {
            refresh(context);
            long eventtime = SystemClock.uptimeMillis();
            Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
            KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
            downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
            context.sendOrderedBroadcast(downIntent, null);
        }
        else if(deviceName.contains("KARBONN"))
        {
            refreshKarbon(context);

            isPlayingInKarbon= !isPlayingInKarbon ;

           int a= isPlayingInKarbon ?    KeyEvent.KEYCODE_MEDIA_PLAY : KeyEvent.KEYCODE_MEDIA_PAUSE ;

            long eventtime = SystemClock.uptimeMillis();
            Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
            KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN , a, 0);
            downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
            context.sendOrderedBroadcast(downIntent, null);
        }
        else
        {
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            if (mAudioManager.isMusicActive())
            {
                KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE);
                mAudioManager.dispatchMediaKeyEvent(event);
            }
            else
            {
                KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY);
                mAudioManager.dispatchMediaKeyEvent(event);
            }
        }
    }


   // public void next(View v)
    public void next(Context context)
    {
        String deviceName = android.os.Build.MANUFACTURER;
        Log.e("Manufacture", deviceName);

        if (deviceName.contains("LENOVO") || deviceName.contains("vivo"))
        {
            refresh(context);
            long eventtime = SystemClock.uptimeMillis();
            Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
            KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT, 0);
            downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
            context.sendOrderedBroadcast(downIntent, null);
        }
        else if(deviceName.contains("KARBONN"))
        {
            isPlayingInKarbon=true;

            refreshKarbon(context);
            long eventtime = SystemClock.uptimeMillis();
            Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
            KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT, 0);
            downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
            context.sendOrderedBroadcast(downIntent, null);
        }
        else
        {
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT);
            mAudioManager.dispatchMediaKeyEvent(event);
        }

    }


    public void refresh(Context context)
    {
        long eventtime = SystemClock.uptimeMillis();
        Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
        KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
        upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
        context.sendOrderedBroadcast(upIntent, null);

    }

    public void refreshKarbon(Context context)
    {
        long eventtime = SystemClock.uptimeMillis();
        Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
        KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PAUSE, 0);
        upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
        context.sendOrderedBroadcast(upIntent, null);

    }

}
