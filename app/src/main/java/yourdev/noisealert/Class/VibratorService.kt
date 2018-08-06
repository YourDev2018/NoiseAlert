package yourdev.noisealert.Class

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator

class VibratorService : Service() {

    lateinit var vibrator: Vibrator

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()

        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)



        vibrator.vibrate(2000)

    }

    override fun onDestroy() {
        super.onDestroy()

    }





}