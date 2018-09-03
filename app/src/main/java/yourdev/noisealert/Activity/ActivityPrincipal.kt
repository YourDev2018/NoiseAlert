package yourdev.noisealert.Activity


import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView

import yourdev.noisealert.R

import `in`.goodiebag.carouselpicker.CarouselPicker
import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Handler
import android.os.Vibrator
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import yourdev.noisealert.Class.*


class ActivityPrincipal : AppCompatActivity() {

    lateinit var carouselPicker1: CarouselPicker

    lateinit var progressBar: ImageView
    lateinit var clipDrawable: ClipDrawable
    lateinit var butOnOff: ImageView
    lateinit var phoneOnOff: ImageView
    lateinit var fundoProgressBar: ImageView
    lateinit var volumeAmbiente: TextView
    lateinit var indicadorSonoro: ImageView
    lateinit var indicador: ImageView
    lateinit var sensibilidadeTextView: TextView
    lateinit var imageBot: ImageView
    lateinit var textAdapter: CarouselPicker.CarouselViewAdapter
    lateinit var listCarousel: List<CarouselPicker.PickerItem>
    lateinit var goSettings: Button
    lateinit var homeTv:TextView

    private var mFileName = ""
    private var contVezes = 0

    private var level: Int
        get() = clipDrawable.level
        set(level) {
            clipDrawable.level = level
        }
    lateinit var gambiarra: ImageView

    lateinit var modoToque: String
    var vibrar: Int = 0

    var tempoDeToque = 0
    internal val updater: Runnable = Runnable { updateTv() }
    internal val mHandler = Handler()
    lateinit var runner: Thread

    private val REQUEST_RECORD_AUDIO_PERMISSION = 200

    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private lateinit var managePermissions: ManagePermissions

    lateinit var vibrator: Vibrator

    var mRecorder = MediaRecorder()
    var list = java.util.ArrayList<Double>()
    var TEMPO_MEDIO = 0
    var RECORD_AUDIO = 0

    var auxPhoneState = 0

    var auxStop = false
    private var mp = MyMediaPlayer()
    var mPlayer = MediaPlayer()

    var myNotification =  MyNotification()
    lateinit var notificationManager: NotificationManager

    lateinit var telephonyManager: TelephonyManager
    lateinit var listener: PhoneStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal_nova)

        initializeUi()
        fonts()

       // stopRecorder()
        try {


        val intent = intent
        if (intent != null) {

            val params = intent.extras
            if (params != null) {

                auxPhoneState = params["phoneState"] as Int
               // Log.i("Noise_Console", "phoneState: $auxPhoneState")


                if (auxPhoneState == 1) {
                    efeitosOn()
                    setNotification(true)
                }else {
                    efeitosOff()
                }//   stopRecorder()


                modoToque = params["modo_som"].toString()
                vibrar = params["vibracao"] as Int

                tempoDeToque = params["tempoDeToque"] as Int

               /* val notification = params["notification"] as Boolean
                if (notification){
                    finish()
                }
*/
            }else{

                if(getPhoneState()==1){
                    stopRecorder()

                }

            }

        }
        }catch (i: RuntimeException){

        }

        gambiarra.setOnClickListener {}
        goSettings.setOnClickListener { startActivity(Intent(this, ActivitySettings::class.java)); finish(); }

        butOnOff.setOnClickListener {
            //  phoneOnOff.setVisibility(View.INVISIBLE);
            if (butOnOff.tag.toString() == "on") {
                // Toast.makeText(applicationContext, "True", Toast.LENGTH_SHORT).show()
                efeitosOff()
                stopRecorder()
                setPhoneState(0)
                setNotification(false)
            } else
                if (butOnOff.tag.toString() == "off") {
                    // Toast.makeText(applicationContext, "True", Toast.LENGTH_SHORT).show()
                    efeitosOn()
                    stopRecorder()
                    startRecorder()
                    setPhoneState(1)
                    setNotification(true)
                }
        }


        val list = listOf<String>(
                Manifest.permission.RECORD_AUDIO

        )

        managePermissions = ManagePermissions(this,list,REQUEST_RECORD_AUDIO_PERMISSION)
        ActivityCompat.requestPermissions(this, permissions,REQUEST_RECORD_AUDIO_PERMISSION)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        carouselPicker1.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {





            }
            override fun onPageSelected(position: Int) {

               // Log.i("Noise_Console","Position $position ")

                if (position == 0)
                    TEMPO_MEDIO = 10000
                if (position == 1)
                    TEMPO_MEDIO = 9000
                if (position == 2)
                    TEMPO_MEDIO = 8000
                if (position == 3)
                    TEMPO_MEDIO = 7000
                if (position == 4)
                    TEMPO_MEDIO = 6000
                if (position == 5)
                    TEMPO_MEDIO = 5000
                if (position == 6)
                    TEMPO_MEDIO = 4000
                if (position == 7)
                    TEMPO_MEDIO = 3000
                if (position == 8)
                    TEMPO_MEDIO = 2000
                if (position == 9)
                    TEMPO_MEDIO = 1000

                if (position == 1-1)
                    indicadorSonoro.setImageDrawable(applicationContext.getDrawable(R.mipmap.ic_carousel_100))
                if (position == 2-1)
                    indicadorSonoro.setImageDrawable(applicationContext.getDrawable(R.mipmap.ic_carousel_90))
                if (position == 3-1)
                    indicadorSonoro.setImageDrawable(applicationContext.getDrawable(R.mipmap.ic_carousel_80))
                if (position == 4-1)
                    indicadorSonoro.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_activity_principal_car))

                if (position == 5-1)
                    indicadorSonoro.setImageDrawable(applicationContext.getDrawable(R.mipmap.ic_carousel_70))

                if (position == 6-1)
                    indicadorSonoro.setImageDrawable(applicationContext.getDrawable(R.mipmap.ic_carousel_50))

                if (position == 7-1)
                    indicadorSonoro.setImageDrawable(applicationContext.getDrawable(R.mipmap.ic_carousel_40))

                if (position == 8-1)
                    indicadorSonoro.setImageDrawable(applicationContext.getDrawable(R.mipmap.ic_carousel_30))

                if (position == 9-1)
                    indicadorSonoro.setImageDrawable(applicationContext.getDrawable(R.mipmap.ic_carousel_20))

                if (position == 10 -1)
                    indicadorSonoro.setImageDrawable(applicationContext.getDrawable(R.mipmap.ic_carousel_10))

            }

        })

        tempoDeToque = getTempoToque()
        runner = object : Thread() {
            override fun run() {
                while (runner != null) {

                    if (auxStop){
                        auxStop = false
                        Thread.sleep((tempoDeToque*1000 - 1000).toLong())
                        mPlayer = mp.pauseMusic(mPlayer)
                        vibrator = mp.pauseVibrator(vibrator)
                        continue
                    }

                    mHandler.post(updater)
                    try {
                        Thread.sleep(1000)
                        //  Log.i("Noise", "Tock")
                    } catch (e: InterruptedException) {

                    }

                }
            }
        }

        runner.start()
/*
        telephonyManager =  getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        listener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                var stateString = "N/A"
                when (state) {
                    TelephonyManager.CALL_STATE_IDLE -> Log.i("PhoneStateNoiseAlert","Call_state_idle")
                    TelephonyManager.CALL_STATE_OFFHOOK -> Log.i("PhoneStateNoiseAlert","CALL_STATE_OFFHOOK")
                    TelephonyManager.CALL_STATE_RINGING -> Log.i("PhoneStateNoiseAlert","CALL_STATE_RINGING")
                }

            }
        }

  */
    }

    // Receive the permissions request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(applicationContext,"Permission True", Toast.LENGTH_SHORT).show()
                    stopRecorder()
                    startRecorder()

                } else {
                    Toast.makeText(applicationContext,"Permission false", Toast.LENGTH_SHORT).show()
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    override fun onResume() {
        super.onResume()
        // Log.i("Console_Noise_Alert_toq","onResume")
        getToque()
        tempoDeToque = getTempoToque()


    }

    override fun onPause() {
        super.onPause()

        //salvarDados()
    }
/*
    override fun onStop() {
        super.onStop()
        stopRecorder()
        //Log.i("Console_Noise_Alert_toq","onStop")
    }
  */

    override fun onDestroy() {
        super.onDestroy()
      //  Log.i("Console_Noise_Alert_toq","onDestroy")
        // salvarDados()
        stopRecorder()
        try {
            notificationManager.cancel(1)
        }catch (I: RuntimeException){
            //
        }catch (i: UninitializedPropertyAccessException){
            //
        }

    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun updateTv() {
/*
        if (!getPhoneState()) {
            return
        }
*/
        contVezes += 1

        val auxAmplitude = getAmplitude()
        if (auxAmplitude == 0.0) {
            list = java.util.ArrayList()
            return
        }


        list.add(auxAmplitude)

        if (list.size < 4) {
            //mPlayer = mp.pauseMusic(mPlayer);
            return
        } else {

            var result = 0.0
            var i = 0
            if (TEMPO_MEDIO == 0)
                TEMPO_MEDIO = 10000
            while(i < 4) {


                val aux = list.get(i) as Double
               // Log.i("Alarme","teste for " + aux)
                if (aux < TEMPO_MEDIO) {

                    mPlayer = mp.pauseMusic(mPlayer)
                   // Log.i("Alarme","Pausado " )
                    list = java.util.ArrayList()
                    break
                }
                result += aux
                i++

            }
            if (result / 4 >= TEMPO_MEDIO) {
                try {
                    //Log.i("Alarme","Tocando")
                    if (mPlayer.isPlaying)
                        return

                    if (getVibrate()) {
                        vibrator = mp.startVibrate(applicationContext, vibrator, (tempoDeToque * 1000).toLong())

                        val modoSom = getModoSom()
                        if(modoSom== "gravar") {
                            mPlayer = mp.playMusicWithInternalURI(applicationContext, mPlayer, externalCacheDir.absolutePath + "/noise_alert_music.3gp")
                        }

                        if (modoSom == "padrao"){
                            mPlayer = mp.playMusic(applicationContext,mPlayer,R.raw.toque_padrao)
                        }

                        if (modoSom == "alternativa"){
                            mPlayer = mp.playMusic(applicationContext,mPlayer,R.raw.toque_alternativo)
                        }


                    } else{

                        val modoSom = getModoSom()
                        if(modoSom== "gravar") {
                            mPlayer = mp.playMusicWithInternalURI(applicationContext, mPlayer, externalCacheDir.absolutePath + "/noise_alert_music.3gp")
                        }

                        if (modoSom == "padrao"){
                            mPlayer = mp.playMusic(applicationContext,mPlayer,R.raw.toque_padrao)
                        }

                        if (modoSom == "alternativa"){
                            mPlayer = mp.playMusic(applicationContext,mPlayer,R.raw.toque_alternativo)
                        }
                    }

                    //startCronometro()
                    list = java.util.ArrayList()

                    auxStop = true

                    return

                }catch (i: IllegalStateException){
                    mPlayer = mp.pauseMusic(mPlayer)
                }

            }else{
                mPlayer = mp.pauseMusic(mPlayer)
                list = java.util.ArrayList()
            }
        }

    }

    private fun getAmplitude(): Double {


        var amplitude =  mRecorder.maxAmplitude.toDouble()
        amplitude = 10 * Math.log10(amplitude)
        var porcentagem = (amplitude * 100)
        porcentagem /= 45.15436681141699

        if (porcentagem < 0){

            level = 0


        }else{
            if (phoneOnOff.tag == "on") {
                level = (porcentagem * 100).toInt()
            }

        }

        if (porcentagem.toString() == "-Infinity")
            return 0.0


        return porcentagem * 100

    }
    private fun efeitosOn() {
        butOnOff.tag = "on"
        phoneOnOff.tag = "on"
        butOnOff.setBackgroundResource(R.drawable.act_principal_ripple_but_on)
        phoneOnOff.setBackgroundResource(R.drawable.ic_activity_principal_phon)
        fundoProgressBar.setBackgroundResource(R.drawable.activity_principal_progress_bar_on)
        volumeAmbiente.setTextColor(Color.parseColor("#c4c4c4"))
        indicadorSonoro.setColorFilter(Color.parseColor("#ffffff"))
        indicador.setColorFilter(Color.parseColor("#ffffff"))
        sensibilidadeTextView.setTextColor(Color.parseColor("#ffffff"))
      //  textAdapter.
      //  list = CarouselItens().createCarouselCinza(applicationContext,list)
      //  textAdapter = CarouselPicker.CarouselViewAdapter(applicationContext,list,0)

        //carouselPicker1.adapter = textAdapter

        imageBot.visibility = View.VISIBLE
        gambiarra.visibility = View.GONE
        auxPhoneState = 1

    }

    private fun efeitosOff() {
        butOnOff.tag = "off"
        phoneOnOff.tag = "off"
        butOnOff.setBackgroundResource(R.drawable.act_principal_ripple_but_off)
        phoneOnOff.setBackgroundResource(R.drawable.ic_mic_off)
        fundoProgressBar.setBackgroundResource(R.drawable.activity_principal_progress_bar_off)
        level = 0
        volumeAmbiente.setTextColor(Color.parseColor("#4Dc4c4c4"))
        indicadorSonoro.setColorFilter(Color.parseColor("#eaeaea"))
        indicador.setColorFilter(Color.parseColor("#eaeaea"))
        sensibilidadeTextView.setTextColor(Color.parseColor("#4Dc4c4c4"))

     //   list = CarouselItens().createCarouselCinza(applicationContext,list)
     //   textAdapter = CarouselPicker.CarouselViewAdapter(applicationContext,list,0)

       // carouselPicker1.adapter = textAdapter
        imageBot.visibility = View.INVISIBLE
        gambiarra.visibility = View.VISIBLE

        auxPhoneState = 0

    }

    private fun initializeUi() {

        progressBar = findViewById(R.id.act_principal_seek_bar_on)
        clipDrawable = progressBar.drawable as ClipDrawable
        carouselPicker1 = findViewById<View>(R.id.carouselPicker1) as CarouselPicker
        butOnOff = findViewById(R.id.act_principal_but_on_off)
        phoneOnOff = findViewById(R.id.act_principal_phone_on_off)
        fundoProgressBar = findViewById(R.id.act_principal_progress_bar_fundo)
        volumeAmbiente = findViewById(R.id.act_principal_volume_ambiente)
        indicadorSonoro = findViewById(R.id.act_principal_indicador_sonoro)
        indicador = findViewById(R.id.act_principal_indicador)
        sensibilidadeTextView = findViewById(R.id.act_principal_text_view_sensibilidade)
        imageBot = findViewById(R.id.act_principal_image_bot)
        listCarousel = ArrayList()
        listCarousel = CarouselItens().createCarouselCinza(applicationContext,listCarousel)
        textAdapter = CarouselPicker.CarouselViewAdapter(applicationContext,listCarousel,0)

        carouselPicker1.adapter = textAdapter
        gambiarra = findViewById(R.id.gambiarra_off)
        goSettings = findViewById(R.id.act_principal_nova_configuracoes)
        homeTv = findViewById(R.id.act_principal_text_view_home)


    }

    private fun fonts(){
        val regular = Typeface.createFromAsset(assets, "roboto_regular.ttf")
        val medium = Typeface.createFromAsset(assets, "roboto_medium.ttf")
        homeTv.typeface = medium
        volumeAmbiente.typeface = regular
        sensibilidadeTextView.typeface = regular
    }

    private fun getVibrate(): Boolean{
        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("vibracao")
        val cursor  = sql.getDados("UserConfig",colunas)
        try {
            cursor.moveToFirst()
            //  Log.i("Console_Noise_Alert_s",cursor.getString(cursor.getColumnIndex("vibracao")).toInt().toString())
            return cursor.getString(cursor.getColumnIndex("vibracao")).toInt() == 1
        }catch (i: RuntimeException){
            //   Toast.makeText(applicationContext,"Erro na leitura de dados", Toast.LENGTH_SHORT).show()
            return false

        }
    }
/*
    private fun getSensibilidade(): Int{
        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("sensibilidade")
        val cursor  = sql.getDados("UserConfig",colunas)

        try {
            cursor.moveToFirst()
            Log.i("Console_Noise_Alert_sen",cursor.getString(cursor.getColumnIndex("sensibilidade")).trim())
            return cursor.getString(cursor.getColumnIndex("sensibilidade")).trim().toInt()

            //  progressBarPorcentagemBot.progress = sensibilidade

        }catch (i: RuntimeException){
            Toast.makeText(applicationContext,"Erro na leitura de dados",Toast.LENGTH_SHORT).show()
            return 90
        }
    }
  */

    private fun getModoSom(): String{
        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("modo_som")
        val cursor  = sql.getDados("UserConfig",colunas)

        try {
            cursor.moveToFirst()
       //     Log.i("Console_Noise_Alert_ms","Noise:"+ cursor.getString(cursor.getColumnIndex("modo_som")).trim())
            return cursor.getString(cursor.getColumnIndex("modo_som")).trim()
        }catch (i: RuntimeException){
         //   Log.i("Console_Noise_Alert_toq", "Entrou no Runtime")
            return ""
        }
    }

    private fun getToque(): Int{

        val aux = getModoSom().toUpperCase()

        if (aux == "padrao") {
            //    mPlayer = android.media.MediaPlayer.create(applicationContext, R.raw.toque_hotline_bling)
        //    Log.i("Console_Noise_Alert","Entrou Padrão getToque")
            return R.raw.toque_padrao
        }else
            if (aux == "alternativa") {
                //  mPlayer = android.media.MediaPlayer.create(applicationContext, R.raw.toque_alternativo)
          //      Log.i("Console_Noise_Alert", "Entrou Alternativo")
                return  R.raw.toque_alternativo
            }else
                if (aux == "crazy") {
                    return  R.raw.toque_alternativo
                }else
                    if (aux == "buscar") {
                        return 0
                    }else{
                        if (aux == "gravar") {
                            return 0
                        }
                    }


        return 0
    }

    private fun getTempoToque(): Int{
        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("tempoDeToque")
        val cursor  = sql.getDados("UserConfig",colunas)

        try {
            cursor.moveToFirst()
            val aux = cursor.getString(cursor.getColumnIndex("tempoDeToque")).toInt()
          //  Log.i("Console_Noise_Alert_ms","Tempo de toque $aux")
            return aux
        }catch (i: RuntimeException){
          //  Log.i("Console_Noise_Alert_tmt", "Entrou no Runtime")
            return 4
        }
    }

    private fun setPhoneState(int: Int){

        val sql = FuncSQLiteDB(applicationContext)
        val contentValues = ContentValues()
        contentValues.put("phoneState",int)
        if(sql.upadateDados(contentValues,"UserConfig","id=1")){
          // Log.i("Noise_Console", "Phone State : $int")
        }

    }

    private fun getUrl(): String{

        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("urlMusic")
        val cursor  = sql.getDados("UserConfig",colunas)
        try {
            cursor.moveToFirst()
            return cursor.getString(cursor.getColumnIndex("urlMusic")).trim().toString()
        }catch (i: RuntimeException){
            Toast.makeText(applicationContext,"Erro na leitura de dados",Toast.LENGTH_SHORT).show()
            return ""

        }
    }

    private fun startRecorder() {


        try {
            val intent = intent
            if (intent != null) {

                val params = intent.extras
                if (params != null) {
                    val aux = params["notificaion"] as Int
                    if (aux == 1) {
                        return
                    }
                }
            }
        }catch (i: RuntimeException){

        }

                mFileName = externalCacheDir.absolutePath + "/record.3gp"

        /*
        if (getModoMusica().equals("Personalizado"))
            mFileName = getUrl()
        */

        mRecorder = MediaRecorder()
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName)
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
       // Log.i("mFileName", mFileName)

        try {
            mRecorder.prepare()
        } catch (ioe: java.io.IOException) {
       //     android.util.Log.e("Console_Noise_Alert", "IOException: " + android.util.Log.getStackTraceString(ioe))

        } catch (e: java.lang.SecurityException) {
         //   android.util.Log.e("Console_Noise_Alert", "SecurityException: " + android.util.Log.getStackTraceString(e))
        }

        try {

            mRecorder.start()
           // Log.i("Console_Noise_Alert","Start Ok")

        } catch (e: java.lang.SecurityException) {
           // android.util.Log.e("Console_Noise_Alert", "SecurityException: " + android.util.Log.getStackTraceString(e))


        } catch (e: IllegalStateException) {
           // android.util.Log.e("Console_Noise_Alert", "SecurityException: " + android.util.Log.getStackTraceString(e))
     //       Toast.makeText(applicationContext,"Erro audio Recorder",Toast.LENGTH_SHORT).show()
            stopRecorder()
            //startRecorder()
        }

    }

    private fun stopRecorder() {
        try {
            vibrator = mp.pauseVibrator(vibrator)
            auxStop = false

        }catch (i: RuntimeException) {
        }catch (i: IllegalStateException ){

        }

        try{

//            mRecorder.stop()
            mRecorder.release()
            mRecorder = MediaRecorder()

            mPlayer = mp.pauseMusic(mPlayer)
        }catch (i: IllegalStateException ){
           // Log.i("Console_Noise_Alert","Illegar" + android.util.Log.getStackTraceString(i))
            mPlayer = mp.pauseMusic(mPlayer)
        }catch (i: RuntimeException){
           // Log.i("Console_Noise_Alert","Pause RunTime ")
            mPlayer = mp.pauseMusic(mPlayer)
        }

    }

    private fun getModoMusica(): String{
        //recuperando modo_som
        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("modo_som")
        val cursor  = sql.getDados("UserConfig",colunas)
        cursor.moveToFirst()
        modoToque = cursor.getString(cursor.getColumnIndex("modo_som")).trim()
        //Log.i("NoiseAlert_Console",modoToque)

        return modoToque


    }

    private fun getUrlBuscar(): String{

        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("urlMusicBuscar")
        val cursor  = sql.getDados("UserConfig",colunas)
        try {
            cursor.moveToFirst()
            return cursor.getString(cursor.getColumnIndex("urlMusicBuscar")).trim().toString()
        }catch (i: RuntimeException){
            Toast.makeText(applicationContext,"Erro na leitura de dados",Toast.LENGTH_SHORT).show()
            return ""

        }
    }

    private fun getUrlGravar(): String{

        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("urlMusicGravar")
        val cursor  = sql.getDados("UserConfig",colunas)
        try {
            cursor.moveToFirst()
            return cursor.getString(cursor.getColumnIndex("urlMusicGravar")).trim().toString()
        }catch (i: RuntimeException){
            Toast.makeText(applicationContext,"Erro na leitura de dados",Toast.LENGTH_SHORT).show()
            return ""

        }
    }

    private fun setNotification(status: Boolean) {

        try {

            if (status)
                notificationManager = myNotification.simples(applicationContext, R.mipmap.logo_noise_branca,application.getString(R.string.titulo_notificao_on),application.getString(R.string.texto_notificacao), 1, "1")
            else
                notificationManager.cancel(1)

        }catch (i:UninitializedPropertyAccessException){
            //
        }

    }

    private fun getPhoneState():Int{
        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("phoneState")
        val cursor  = sql.getDados("UserConfig",colunas)
        cursor.moveToFirst()
        return cursor.getInt(cursor.getColumnIndex("phoneState"))

    }


    

}