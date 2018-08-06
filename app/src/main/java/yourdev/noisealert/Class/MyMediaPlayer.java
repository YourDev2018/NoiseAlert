package yourdev.noisealert.Class;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

import yourdev.noisealert.R;


public class MyMediaPlayer {

    MediaPlayer mp;
    int music = R.raw.toque_hotline_bling;

    public MediaPlayer playMusic(Context context, android.media.MediaPlayer mp){
        try {
            if (mp.isPlaying()) {
             //   mp.release();
             //   mp = null;
                return mp;
            } else {

                mp = MediaPlayer.create(context, R.raw.toque_hotline_bling);
                mp.start();
                return mp;
            }
        }catch (NullPointerException n){
            mp = MediaPlayer.create(context, R.raw.toque_hotline_bling);
            mp.start();
            return mp;
        }

    }

    public  MediaPlayer playMusic(Context context, android.media.MediaPlayer mp,String fileName){
        try {
            if (mp.isPlaying()) {
                mp.release();
                mp = null;
                return mp;
            } else {

                mp.setDataSource(fileName);
                mp.start();
                return mp;
            }

        }catch (NullPointerException n){
            mp = android.media.MediaPlayer.create(context, R.raw.toque_hotline_bling);
            mp.start();
            return mp;
        } catch (IOException e) {
            e.printStackTrace();
            mp = android.media.MediaPlayer.create(context, R.raw.toque_hotline_bling);
            mp.start();
            return mp;

        }

    //    return mp;
    }

    public android.media.MediaPlayer pauseMusic(android.media.MediaPlayer mp){
        try {
            mp.reset();
            if (!mp.isPlaying()) {
                mp.stop();
            }
            mp.reset();
            return mp;
        }catch (NullPointerException n){
            return mp;
        }
    }

}
