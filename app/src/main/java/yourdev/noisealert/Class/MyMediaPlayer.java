package yourdev.noisealert.Class;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import java.io.IOException;

import yourdev.noisealert.R;


public class MyMediaPlayer {

    MediaPlayer mp;
    Vibrator vibrator;

    //int music = R.raw.toque_hotline_bling;



    public  MediaPlayer playMusic(Context context, android.media.MediaPlayer mp,Integer uri){
        try {


            if (mp.isPlaying()) {
                mp.pause();
             //   mp.stop();
                mp.release();
                mp = new MediaPlayer();
                return mp;
            } else {

                mp = MediaPlayer.create(context, uri);

                mp.start();
                return mp;
            }

        }catch (NullPointerException | IllegalStateException n){

            mp = new MediaPlayer();

          //  mp = android.media.MediaPlayer.create(context, uri);
            Toast.makeText(context,"Entrou no Catch",Toast.LENGTH_SHORT).show();
    //        mp.start();
          //  mp.release();
            return mp;
        }

        //    return mp;
    }

    public  MediaPlayer playMusicWithInternalURI(Context context, android.media.MediaPlayer mp,String uri){
        try {


            if (mp.isPlaying()) {
                mp.pause();
                mp.stop();
                //  mp.release();
                //   mp = new MediaPlayer();
                return mp;
            } else {
//context.getExternalCacheDir().getAbsolutePath() + "noise_alert_music.3gp"
                Uri myUri = Uri.parse(uri); // initialize Uri here
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(context, myUri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                return mp;
            }

        }catch (NullPointerException | IllegalStateException i){

            mp = new MediaPlayer();

            //  mp = android.media.MediaPlayer.create(context, uri);
            i.printStackTrace();Toast.makeText(context,"Entrou catch" ,Toast.LENGTH_SHORT).show();
            //        mp.start();
            //  mp.release();
            return mp;
        } catch (IOException e) {
            e.printStackTrace();
            return mp;
        }

        //    return mp;
    }

    public android.media.MediaPlayer pauseMusic(android.media.MediaPlayer mp){
        try {
            mp.pause();
            mp.stop();
          //  mp.release();


            return mp;
        }catch (NullPointerException | IllegalStateException n){
            mp = new MediaPlayer();

            return mp;
        }
    }

    public void releaseMusic(MediaPlayer mp){
        mp.release();
        mp =null;
    }

    public boolean isPlaying(MediaPlayer mp){

        try {
            return mp.isPlaying();
        }catch (NullPointerException i){
            return false;
        }



    }


    public Vibrator startVibrate(Context context, Vibrator vibrator, Long tempoToque){


        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator.vibrate(VibrationEffect.createOneShot(tempoToque,VibrationEffect.DEFAULT_AMPLITUDE));
        else
            vibrator.vibrate(tempoToque);

        return vibrator;
    }

    public Vibrator pauseVibrator(Vibrator vibrator) {

        vibrator.cancel();

        return vibrator;
    }
}
