package yourdev.noisealert.Activity


import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.webkit.PermissionRequest
import android.webkit.WebView
import android.widget.*
import yourdev.noisealert.Class.FuncSQLiteDB
import yourdev.noisealert.R
import java.util.ArrayList
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat
import yourdev.noisealert.Class.ManagePermissions
import yourdev.noisealert.Class.MyMediaPlayer
import yourdev.noisealert.Class.VibratorService


class Activity_Principal : AppCompatActivity() {


    private lateinit var microfone: ImageView
    private lateinit var onOff: TextView
    private lateinit var add: ImageView
    private lateinit var remove: ImageView
    private  lateinit var porcentagem: TextView
    private lateinit var porcentagemBot: TextView
    private lateinit var fundo: ConstraintLayout
    private lateinit var config: ImageButton
    private lateinit var sobreNos: ImageButton
    private lateinit var progressBarPorcentagemTop: ProgressBar
    private lateinit var progressBarPorcentagemBot: ProgressBar

    private var vibracao = 0
    private var volume  = 0
    private var name = ""
    private var sensibilidade = 90
    private var url = ""
    private var modoSom = ""
    private var onOffDB = 1
    private var mFileName = ""
    private var contVezes = 0
    private var play_on = false


    lateinit var strURL: String

    internal val updater: Runnable = Runnable { updateTv() }
    internal val mHandler = Handler()
    lateinit var runner: Thread

    internal val cronometro: Runnable = Runnable { startCronometro() }
    internal val mHandlerCronometro = Handler()
    lateinit var runnerCronometro: Thread

    private val REQUEST_RECORD_AUDIO_PERMISSION = 200
    var isPermissionsGranted = false
    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private lateinit var managePermissions: ManagePermissions

    var mRecorder = MediaRecorder()
    var list =  ArrayList<Double>()
    var TEMPO_MEDIO = 0
    var RECORD_AUDIO = 0

    var auxStop = false
    private var mp = MyMediaPlayer()
    var mPlayer = MediaPlayer()

    lateinit var vibrator: Vibrator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        initializeUi()

        //recuperarDados()
        val list = listOf<String>(
                Manifest.permission.RECORD_AUDIO

        )

        managePermissions = ManagePermissions(this,list,REQUEST_RECORD_AUDIO_PERMISSION)
        ActivityCompat.requestPermissions(this, permissions,REQUEST_RECORD_AUDIO_PERMISSION)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

      //  mPlayer = MediaPlayer()
        // temos que rever onde esse recebimento de configuração de toque é mais apropriado


        // efeito botão ligado/desligado
        onOff.setOnClickListener {


            if (onOff.text.toString().toUpperCase() == applicationContext.getString(R.string.principal_text_view_ligado).toUpperCase()){

                efeitosOff()
                setSensibilidade()
                setPhoneState(0)
                stopRecorder()
            }

            else
                if (onOff.text.toString().toUpperCase() ==  applicationContext.getString(R.string.principal_text_view_desligado).toUpperCase()) {
                    recuperarSensibilidade()
                    efeitosOn()
                    setPhoneState(1)
                    startRecorder()

                }
        }

        // efeito microfone ligado/desligado
        microfone.setOnClickListener {

            if (onOff.text.toString().toUpperCase() == applicationContext.getString(R.string.principal_text_view_ligado).toUpperCase()){

                efeitosOff()
                setSensibilidade()
                setPhoneState(0)
                stopRecorder()
            }

            else
                if (onOff.text.toString().toUpperCase() ==  applicationContext.getString(R.string.principal_text_view_desligado).toUpperCase()) {
                    recuperarSensibilidade()
                    efeitosOn()
                    setPhoneState(1)
                    startRecorder()

                }
        }

        //add sensibilidade
        add.setOnClickListener {

            if (porcentagem.text.toString().replace("%","").trim().toInt() == 0 && onOff.text.toString().toUpperCase() ==  applicationContext.getString(R.string.principal_text_view_desligado).toUpperCase())
                return@setOnClickListener

            sensibilidade += 10
            porcentagem.text = (porcentagem.text.toString().replace("%","").trim().toInt() + 10).toString() + "%"
            if (porcentagem.text.toString().replace("%","").trim().toInt()>100) {
                porcentagem.text = "100%"
                sensibilidade = 100
            }
            progressBarPorcentagemTop.progress = sensibilidade
            TEMPO_MEDIO = sensibilidade

        }

        // remover sensibilidade
        remove.setOnClickListener{

            if (porcentagem.text.toString().replace("%","").trim().toInt() == 0)
                return@setOnClickListener

            sensibilidade -= 10
            porcentagem.text = (porcentagem.text.toString().replace("%","").trim().toInt() - 10).toString() + "%"
            if (porcentagem.text.toString().replace("%","").trim().toInt()<0) {
                porcentagem.text = "0%"
                sensibilidade = 0
                //porcentagem.setText((aux.toInt() - 10).toString() +  "%")
            }
            progressBarPorcentagemTop.progress = sensibilidade
            TEMPO_MEDIO = sensibilidade


        }

        //ir para sobre
        sobreNos.setOnClickListener { startActivity(Intent(this, Activity_Sobre_Nos::class.java)) }

        // ir para config
        config.setOnClickListener { startActivity(Intent(this, Activity_Configuracoes::class.java)) }


            runner = object : Thread() {
                override fun run() {
                    while (runner != null) {

                        if (auxStop){
                            auxStop = false
                            Thread.sleep(4000)
                            mp.pauseMusic(mPlayer)
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
            Log.d("Noise", "start runner()")
    }

    override fun onResume() {
        super.onResume()
       // Log.i("Console_Noise_Alert_toq","onResume")
        getToque()


    }

    override fun onPause() {
        super.onPause()

        //salvarDados()
    }

    override fun onDestroy() {
        super.onDestroy()

       // salvarDados()
        stopRecorder()

    }

    private fun initializeUi(){
        microfone = findViewById(R.id.activity_principal_image_view_microfone)
        onOff = findViewById(R.id.activity_principal_text_view_on_off)
        add = findViewById(R.id.activity_principal_image_view_add)
        remove = findViewById(R.id.activity_principal_image_view_remove)
        porcentagem = findViewById(R.id.activity_principal_text_view_porcentagem)
        fundo = findViewById(R.id.activity_principal_layout_bot)
        config = findViewById(R.id.activity_principal_config)
        sobreNos = findViewById(R.id.activity_principal_menu_sobre_nos)
        progressBarPorcentagemTop = findViewById(R.id.activity_principal_progress_bar_top)
        progressBarPorcentagemBot = findViewById(R.id.activity_principal_progress_bar_bot)
        porcentagemBot = findViewById(R.id.activity_principal_text_view_porcentagem_bot)
       // setGrafico(20)

    }

    // Receive the permissions request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION ->{
                 isPermissionsGranted = managePermissions
                        .processPermissionsResult(requestCode,permissions,grantResults)

                if(isPermissionsGranted){
                    Log.i("NoiseAlert_Console","Permissão ok")
                    if(getPhoneState()){
                        Log.i("NoiseAlert_Console","State ok")
                        getModoMusica()
                        recuperarSensibilidade()
                        efeitosOn()
                        startRecorder()
                    }else{
                        getModoMusica()
                        efeitosOff()

                    }
                }
            }
        }
    }



    private fun efeitosOn(){

        onOff.setBackgroundResource(R.drawable.color_degrade_activity_principal_text_view_ligado)
        onOff.setText(R.string.principal_text_view_ligado)
        microfone.setImageResource(R.drawable.activity_principal_phone)
        fundo.setBackgroundResource(R.color.activity_principal_background_color)
        onOffDB = 1

    }

    private fun efeitosOff(){
        onOff.setBackgroundResource(R.drawable.color_degrade_activity_principal_text_view_desligado)
        onOff.setText(R.string.principal_text_view_desligado)
        microfone.setImageResource(R.drawable.activity_principal_phone_desligado)
        fundo.setBackgroundResource(R.color.activity_principal_background_color_off)
        porcentagem.text = "0%"
        onOffDB = 0

        progressBarPorcentagemTop.progress = 0
        progressBarPorcentagemBot.progress = 0
        porcentagemBot.text = "0%"




    }



    private fun getPhoneState(): Boolean {

        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("phoneState")
        val cursor  = sql.getDados("UserConfig",colunas)
        try {
            cursor.moveToFirst()
            Log.i("NoiseAlert_Console",cursor.getString(cursor.getColumnIndex("phoneState")).trim().toInt().toString())
            if((cursor.getString(cursor.getColumnIndex("phoneState"))).trim().toInt() == 1) {
                onOffDB = 1
                return true
            }
            else{
                onOffDB = 0
                return false
            }
        }catch (i: RuntimeException){
            Toast.makeText(applicationContext,"Erro na leitura de dados",Toast.LENGTH_SHORT).show()
            return false

        }


    }

    private fun setPhoneState(int: Int){

        val sql = FuncSQLiteDB(applicationContext)
        val contentValues = ContentValues()
        contentValues.put("phoneState",int)
        if(sql.upadateDados(contentValues,"UserConfig","id=1")){
            Log.i("NoiseAlert_Console","Phone State : true")
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

    private fun getModoMusica(): String{
        //recuperando modo_som
        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("modo_som")
        val cursor  = sql.getDados("UserConfig",colunas)
        cursor.moveToFirst()
        modoSom = cursor.getString(cursor.getColumnIndex("modo_som")).trim()
        Log.i("NoiseAlert_Console",modoSom)

        return modoSom


    }


    private fun recuperarDados(){

        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("id","onOff","volume", "sensibilidade","modo_som","vibracao","urlMusic","nameMusic")
        val cursor  = sql.getDados("UserConfig",colunas)

        // recuperando on/off

        try {
            cursor.moveToFirst()
            if((cursor.getString(cursor.getColumnIndex("onOff"))).trim().toInt() == 1)
                efeitosOn()
            else
                if((cursor.getString(cursor.getColumnIndex("onOff"))).trim().toInt() == 0)
                    efeitosOff()


        // recuperando volume
        cursor.moveToFirst()
        volume = cursor.getString(cursor.getColumnIndex("volume")).trim().toInt()



        // recupera a confoguração de vibrar e trocar
        cursor.moveToFirst()
        vibracao = cursor.getString(cursor.getColumnIndex("vibracao")).trim().toInt()

        cursor.moveToFirst()


        }catch (i: RuntimeException){
            Toast.makeText(applicationContext,"Erro na leitura de dados",Toast.LENGTH_SHORT).show()

        }

    }

    private fun salvarDados(){

    }

    private fun recuperarSensibilidade(){

        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("sensibilidade")
        val cursor  = sql.getDados("UserConfig",colunas)

        try {
            cursor.moveToFirst()
            cursor.moveToFirst()
            sensibilidade = cursor.getString(cursor.getColumnIndex("sensibilidade")).trim().toInt()
            porcentagem.text = sensibilidade.toString() + "%"
            progressBarPorcentagemTop.progress = sensibilidade
          //  progressBarPorcentagemBot.progress = sensibilidade

        }catch (i: RuntimeException){
            Toast.makeText(applicationContext,"Erro na leitura de dados",Toast.LENGTH_SHORT).show()

        }
    }

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

    private fun setSensibilidade(){
        val sql = FuncSQLiteDB(applicationContext)

        val contentValues = ContentValues()
      //  contentValues.put("onOff",onOffDB)
        contentValues.put("sensibilidade",sensibilidade)

        if(sql.upadateDados(contentValues,"UserConfig","id=1")){
            //   Toast.makeText(applicationContext,"Atualizado ao fechar",Toast.LENGTH_SHORT).show()
        }
    }


    override fun onBackPressed() {

        moveTaskToBack(true)

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
            list = ArrayList()
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
                TEMPO_MEDIO = getSensibilidade()
            while(i < 4) {


                val aux = list.get(i) as Double
                Log.i("Alarme","teste for " + aux)
                if (aux < TEMPO_MEDIO) {

                    mPlayer = mp.pauseMusic(mPlayer)
                    Log.i("Alarme","Pausado " )
                    list = ArrayList()
                    break
                }
                result += aux
                i++

            }
            if (result / 4 >= TEMPO_MEDIO) {
                try{
                    //Log.i("Alarme","Tocando")
                    if(mPlayer.isPlaying)
                        return

                    mPlayer = mp.playMusic(applicationContext, mPlayer, getToque())
                    vibrator = mp.startVibrate(applicationContext,vibrator)
                    //startCronometro()
                    list = ArrayList()

                    auxStop = true

                    return

                }catch (i: IllegalStateException){
                    mPlayer = mp.pauseMusic(mPlayer)
                }

            }else{
                mPlayer = mp.pauseMusic(mPlayer)
                list = ArrayList()
            }
        }

    }

    private fun getAmplitude(): Double {


        var amplitude =  mRecorder.maxAmplitude.toDouble()
        amplitude = 10 * Math.log10(amplitude)
        var porcentagem = (amplitude * 100)
        porcentagem /= 45.15436681141699

        if (porcentagem < 0){

            progressBarPorcentagemBot.progress = 0
            porcentagemBot.text =  "0%"

        }else{
            progressBarPorcentagemBot.progress = porcentagem.toInt()
            porcentagemBot.text = porcentagem.toInt().toString() + "%"

        }

        if (porcentagem.toString() == "-Infinity")
            return 0.0

      //  Log.i("Porcentagem",porcentagem.toString())

        return porcentagem

    }


    private fun startRecorder() {

        mFileName = externalCacheDir.absolutePath + "record.3gp"

        if (getModoMusica().equals("Personalizado"))
            mFileName = getUrl()

        mRecorder = MediaRecorder()
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName)
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        Log.i("mFileName", mFileName)

        try {
            mRecorder.prepare()
        } catch (ioe: java.io.IOException) {
            android.util.Log.e("[Monkey]", "IOException: " + android.util.Log.getStackTraceString(ioe))

        } catch (e: java.lang.SecurityException) {
            android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e))
        }

        try {

            mRecorder.start()
            Log.i("Console_Noise_Alert","Start Ok")

        } catch (e: java.lang.SecurityException) {
            android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e))


        } catch (e: IllegalStateException) {
            android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e))

            //   startRecorder()
        }



    }

   private fun stopRecorder() {

       vibrator = mp.pauseVibrator(vibrator)

        try {
            mRecorder.stop()
            mRecorder.release()
            mRecorder = MediaRecorder()

            mPlayer = mp.pauseMusic(mPlayer)
        }catch (i: IllegalStateException ){
            mPlayer = mp.pauseMusic(mPlayer)
        }catch (i: RuntimeException){
            mPlayer = mp.pauseMusic(mPlayer)
        }

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

    private fun getModoSom(): String{
        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("modo_som")
        val cursor  = sql.getDados("UserConfig",colunas)

        try {
            cursor.moveToFirst()
            Log.i("Console_Noise_Alert_ms","Noise:"+ cursor.getString(cursor.getColumnIndex("modo_som")).trim())
            return cursor.getString(cursor.getColumnIndex("modo_som")).trim()
        }catch (i: RuntimeException){
            Log.i("Console_Noise_Alert_toq", "Entrou no Runtime")
            return ""
        }
    }


    private fun getToque(): Int{

        val aux = getModoSom().toUpperCase()

        if (aux == applicationContext.getString(R.string.activity_configuracoes_modo_som_subtitulo).toString().toUpperCase()) {
        //    mPlayer = android.media.MediaPlayer.create(applicationContext, R.raw.toque_hotline_bling)
            Log.i("Console_Noise_Alert","Entrou Padrão")
            return R.raw.toque_hotline_bling
        }else
            if (aux == applicationContext.getString(R.string.activity_configuracoes_modo_som_subtitulo_alternativa).toString().toUpperCase()) {
              //  mPlayer = android.media.MediaPlayer.create(applicationContext, R.raw.toque_alternativo)
                Log.i("Console_Noise_Alert", "Entrou Alternativo")
                return  R.raw.toque_alternativo
            }else
                if (aux == applicationContext.getString(R.string.activity_configuracoes_modo_som_subtitulo_crazy).toString().toUpperCase()) {
                    return  R.raw.toque_alternativo
                }else
                    if (aux == "PERSONALIZADA") {
                        if (getUrl() != "") {
                            return 0
                        }
                    }


        return 0
    }
    private fun startCronometro() {
        object : Runnable {
            override fun run() {

                val initialTime = System.currentTimeMillis()
                while ((initialTime - System.currentTimeMillis()).toInt() != 4000){
                    Log.i("Cronometro_test","Contando...")
                    Thread.sleep(1000)

                }
              //  Log.i("Cronometro_test",System.currentTimeMillis().toString())

            }
        }
    }
}
