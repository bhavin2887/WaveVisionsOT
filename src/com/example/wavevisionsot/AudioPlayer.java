package com.example.wavevisionsot;

import java.io.IOException;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

public class AudioPlayer {
   
    String fileName;
    Context contex;
    MediaPlayer mp;

    //Constructor
    public AudioPlayer(String name, Context context) {
        fileName = name;
        contex = context;
        playAudio();
    }

    //Play Audio
    public void playAudio() {
        mp = new MediaPlayer();
        try {
            Log.i("PlayAudio","Playing Audio");
            AssetFileDescriptor descriptor = contex.getAssets().openFd(fileName);
            mp.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mp.reset();
            mp.release();
            mp.prepare();
            mp.setLooping(true);
            mp.start();
            mp.setVolume(5, 5);
            Log.i("PlayAudio","Played Audio");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Stop Audio
    public void stop() {
        mp.stop();
    }
}
