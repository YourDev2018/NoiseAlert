package yourdev.noisealert.Activity

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import org.w3c.dom.Text
import yourdev.noisealert.Class.FuncSQLiteDB
import yourdev.noisealert.Class.ManagePermissions
import yourdev.noisealert.Class.MyMediaPlayer
import yourdev.noisealert.R
import java.io.File
import java.io.FileInputStream

class ActivitySettings : AppCompatActivity(){


    lateinit var constraintLayoutModoSom: ConstraintLayout
    lateinit var constraintLayoutBuscar: ConstraintLayout
    lateinit var constraintLayoutGravar: ConstraintLayout
    lateinit var constraintVibrar: ConstraintLayout
    lateinit var contraintTempoToque: ConstraintLayout
    lateinit var contraintSobreNos: ConstraintLayout

    lateinit var butReturn: ImageView

 //   private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private lateinit var managePermissions: ManagePermissions
    private val REQUEST_Storage_PERMISSION:Int = 0x4

    lateinit var chronometer: Chronometer

    var mRecorder = MediaRecorder()
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200
    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)

    lateinit var switchVibrar: Switch

    private var mp = MyMediaPlayer()
    var mPlayer = MediaPlayer()
    private var mFileName = ""

    lateinit var subTitulo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initializeUi()

        contraintTempoToque.setOnClickListener { /*dialogTempoToque()*/ emBreve() }
        constraintVibrar.setOnClickListener { contraintSetSwitch() }
        constraintLayoutGravar.setOnClickListener { dialogGravarToque() }
        constraintLayoutBuscar.setOnClickListener { /* buscarMusica() */ emBreve() }
        constraintLayoutModoSom.setOnClickListener { dialogModoSom() }
        contraintSobreNos.setOnClickListener { sobreNos() }

        val modoSom = getModoSom()

        if (modoSom == "padrao"){

            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_modo_som_subtitulo)

        }

        if (modoSom == "alternativa"){

            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_modo_som_subtitulo_alternativa)
        }

        if (modoSom == "crazy"){

            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_modo_som_subtitulo_crazy)
        }
        if (modoSom == "buscar"){

            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_buscar_toque_pop_up)
        }

        if (modoSom == "gravar") {

            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_gravar_toque_pop_up)

        }




        butReturn.setOnClickListener { startActivity(Intent(this, ActivityPrincipal::class.java)); finish() }

        switchVibrar.setOnClickListener{setSwitch()}
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_Storage_PERMISSION -> {
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private fun initializeUi(){

        constraintLayoutBuscar = findViewById(R.id.act_settings_contraint_buscar_toque)
        constraintLayoutGravar = findViewById(R.id.act_settings_contraint_gravatar_toque)
        constraintLayoutModoSom = findViewById(R.id.act_settings_contraint_modo__som)
        constraintVibrar = findViewById(R.id.act_settings_constraint_vibrar)
        contraintTempoToque = findViewById(R.id.act_settings_contraint_tempo_toque)
        butReturn = findViewById(R.id.act_settings_return)
        switchVibrar = findViewById(R.id.activity_settings_switch)
        contraintSobreNos = findViewById(R.id.act_settings_contraint_sobre_nos)
        subTitulo = findViewById(R.id.act_settings_modo_som_subtitulo)

    }

    private fun dialogModoSom(){



        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this@ActivitySettings)
        val view = inflater.inflate(R.layout.dialog_modo_som, null)


        val radioPadrao = view.findViewById<RadioButton>(R.id.dialog_modo_som_radio_padrao)
        val radioAlternativa = view.findViewById<RadioButton>(R.id.dialog_modo_som_radio_alternativa)
        val radioCrazy = view.findViewById<RadioButton>(R.id.dialog_modo_som_radio_crazy)
        val radioToqueBuscar = view.findViewById<RadioButton>(R.id.dialog_modo_som_radio_buscar)
        val radioToqueGravar = view.findViewById<RadioButton>(R.id.dialog_modo_som_radio_gravou)


        val tvPadrao = view.findViewById<TextView>(R.id.dialog_modo_som_tv_padrao)
        val tvAlternativa = view.findViewById<TextView>(R.id.dialog_modo_som_tv_alternativa)
        val tvdioCrazy = view.findViewById<TextView>(R.id.dialog_modo_som_tv_crazy)
        val tvToqueBuscar = view.findViewById<TextView>(R.id.dialog_modo_som_tv_buscar)
        val tvToqueGravar = view.findViewById<TextView>(R.id.dialog_modo_som_tv_gravou)


        val tvOk = view.findViewById<TextView>(R.id.dialog_modo_som_tv_ok)
        val tvCancelar = view.findViewById<TextView>(R.id.dialog_modo_som_tv_cancelar)


        val alertDialog = builder.create()
        alertDialog.setView(view)
        alertDialog.show()

        val colorOn = "#f86c69"
        val colorOff = "#414852"

        val modoSom = getModoSom()

        if (modoSom == "padrao"){
            radioPadrao.isChecked = true
            tvPadrao.setTextColor(Color.parseColor(colorOn))


        }

        if (modoSom == "alternativa"){
            radioAlternativa.isChecked = true
            tvAlternativa.setTextColor(Color.parseColor(colorOn))
        }

        if (modoSom == "crazy"){
            radioCrazy.isChecked = true
            tvdioCrazy.setTextColor(Color.parseColor(colorOn))
        }
        if (modoSom == "buscar"){
            radioToqueBuscar.isChecked = true
            tvToqueBuscar.setTextColor(Color.parseColor(colorOn))
        }

        if (modoSom == "gravar"){
            radioToqueGravar.isChecked = true
            tvToqueGravar.setTextColor(Color.parseColor(colorOn))

        }




        if(getUrlBuscar() != ""){
            radioToqueBuscar.visibility = View.VISIBLE
            tvToqueBuscar.visibility = View.VISIBLE
        }

        if(getUrlGravar() != ""){
            radioToqueGravar.visibility = View.VISIBLE
            tvToqueGravar.visibility = View.VISIBLE
        }



        radioPadrao.setOnClickListener {
            radioPadrao.isChecked = true
            radioAlternativa.isChecked = false
            radioCrazy.isChecked = false
            radioToqueBuscar.isChecked = false
            radioToqueGravar.isChecked = false

            tvPadrao.setTextColor(Color.parseColor(colorOn))
            tvAlternativa.setTextColor(Color.parseColor(colorOff))
            tvdioCrazy.setTextColor(Color.parseColor(colorOff))
            tvToqueBuscar.setTextColor(Color.parseColor(colorOff))
            tvToqueGravar.setTextColor(Color.parseColor(colorOff))

            setModoSom("padrao")
            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_modo_som_subtitulo)


        }
        tvPadrao.setOnClickListener {
            radioPadrao.isChecked = true
            radioAlternativa.isChecked = false
            radioCrazy.isChecked = false
            radioToqueBuscar.isChecked = false
            radioToqueGravar.isChecked = false

            tvPadrao.setTextColor(Color.parseColor(colorOn))
            tvAlternativa.setTextColor(Color.parseColor(colorOff))
            tvdioCrazy.setTextColor(Color.parseColor(colorOff))
            tvToqueBuscar.setTextColor(Color.parseColor(colorOff))
            tvToqueGravar.setTextColor(Color.parseColor(colorOff))

            setModoSom("padrao")
            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_modo_som_subtitulo)

        }

        radioAlternativa.setOnClickListener {
            radioAlternativa.isChecked = true
            radioPadrao.isChecked = false
            radioCrazy.isChecked = false
            radioToqueBuscar.isChecked = false
            radioToqueGravar.isChecked = false
            setModoSom("alternativa")
            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_modo_som_subtitulo_alternativa)
        }
        tvAlternativa.setOnClickListener {

            radioPadrao.isChecked = false
            radioAlternativa.isChecked = true
            radioCrazy.isChecked = false
            radioToqueBuscar.isChecked = false
            radioToqueGravar.isChecked = false

            tvPadrao.setTextColor(Color.parseColor(colorOff))
            tvAlternativa.setTextColor(Color.parseColor(colorOn))
            tvdioCrazy.setTextColor(Color.parseColor(colorOff))
            tvToqueBuscar.setTextColor(Color.parseColor(colorOff))
            tvToqueGravar.setTextColor(Color.parseColor(colorOff))
            setModoSom("alternativa")
            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_modo_som_subtitulo_alternativa)

        }

        radioCrazy.setOnClickListener{
            radioAlternativa.isChecked = false
            radioPadrao.isChecked = false
            radioCrazy.isChecked = true
            radioToqueBuscar.isChecked = false
            radioToqueGravar.isChecked = false
            tvPadrao.setTextColor(Color.parseColor(colorOff))
            tvAlternativa.setTextColor(Color.parseColor(colorOff))
            tvdioCrazy.setTextColor(Color.parseColor(colorOn))
            tvToqueBuscar.setTextColor(Color.parseColor(colorOff))
            tvToqueGravar.setTextColor(Color.parseColor(colorOff))
            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_modo_som_subtitulo_crazy)
            setModoSom("crazy")
        }
        tvdioCrazy.setOnClickListener {

            radioAlternativa.isChecked = false
            radioPadrao.isChecked = false
            radioCrazy.isChecked = true
            radioToqueBuscar.isChecked = false
            radioToqueGravar.isChecked = false
            tvPadrao.setTextColor(Color.parseColor(colorOff))
            tvAlternativa.setTextColor(Color.parseColor(colorOff))
            tvdioCrazy.setTextColor(Color.parseColor(colorOn))
            tvToqueBuscar.setTextColor(Color.parseColor(colorOff))
            tvToqueGravar.setTextColor(Color.parseColor(colorOff))
            setModoSom("crazy")
            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_modo_som_subtitulo_crazy)
        }

        radioToqueBuscar.setOnClickListener {
            radioAlternativa.isChecked = false
            radioPadrao.isChecked = false
            radioCrazy.isChecked = false
            radioToqueBuscar.isChecked = true
            radioToqueGravar.isChecked = false
            tvPadrao.setTextColor(Color.parseColor(colorOff))
            tvAlternativa.setTextColor(Color.parseColor(colorOff))
            tvdioCrazy.setTextColor(Color.parseColor(colorOff))
            tvToqueBuscar.setTextColor(Color.parseColor(colorOn))
            tvToqueGravar.setTextColor(Color.parseColor(colorOff))
            setModoSom("buscar")
            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_buscar_toque_pop_up)

        }
        tvToqueBuscar.setOnClickListener {
            radioAlternativa.isChecked = false
            radioPadrao.isChecked = false
            radioCrazy.isChecked = false
            radioToqueBuscar.isChecked = true
            radioToqueGravar.isChecked = false
            tvPadrao.setTextColor(Color.parseColor(colorOff))
            tvAlternativa.setTextColor(Color.parseColor(colorOff))
            tvdioCrazy.setTextColor(Color.parseColor(colorOff))
            tvToqueBuscar.setTextColor(Color.parseColor(colorOn))
            tvToqueGravar.setTextColor(Color.parseColor(colorOff))
            setModoSom("buscar")
            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_buscar_toque_pop_up)

        }

        radioToqueGravar.setOnClickListener {
            radioAlternativa.isChecked = false
            radioPadrao.isChecked = false
            radioCrazy.isChecked = false
            radioToqueBuscar.isChecked = false
            radioToqueGravar.isChecked = true
            tvPadrao.setTextColor(Color.parseColor(colorOff))
            tvAlternativa.setTextColor(Color.parseColor(colorOff))
            tvdioCrazy.setTextColor(Color.parseColor(colorOff))
            tvToqueBuscar.setTextColor(Color.parseColor(colorOff))
            tvToqueGravar.setTextColor(Color.parseColor(colorOn))
            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_gravar_toque_pop_up)
            setModoSom("gravar")}

        tvToqueGravar.setOnClickListener {
            radioAlternativa.isChecked = false
            radioPadrao.isChecked = false
            radioCrazy.isChecked = false
            radioToqueBuscar.isChecked = false
            radioToqueGravar.isChecked = true
            tvPadrao.setTextColor(Color.parseColor(colorOff))
            tvAlternativa.setTextColor(Color.parseColor(colorOff))
            tvdioCrazy.setTextColor(Color.parseColor(colorOff))
            tvToqueBuscar.setTextColor(Color.parseColor(colorOff))
            tvToqueGravar.setTextColor(Color.parseColor(colorOn))
            setModoSom("gravar")
            subTitulo.text = applicationContext.getText(R.string.activity_configuracoes_gravar_toque_pop_up)
        }


        tvCancelar.setOnClickListener { alertDialog.dismiss() }
        tvOk.setOnClickListener { alertDialog.dismiss() }


    }

    private fun buscarMusica(){
/*
        val list = listOf<String>(
                Manifest.permission.RECORD_AUDIO

        )

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {



            } else {

                // No explanation needed, we can request the permission.


                 ActivityCompat.requestPermissions(this, permissions,Manifest.permission.READ_EXTERNAL_STORAGE.toInt())

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            val musicResolver = contentResolver
            val musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val musicCursor = musicResolver.query(musicUri, null, null, null, null)

        }


        val root = Environment.getExternalStorageDirectory();
        val dir = File(root.absolutePath+"/NoiseAlert")

        try{
            val fileImputStream = FileInputStream()

        }catch (){

        }

    //    managePermissions = ManagePermissions(this,list,REQUEST_Storage_PERMISSION )

*/
    }

    private fun emBreve(){
        Toast.makeText(applicationContext,"Estamos desenvolvendo...",Toast.LENGTH_SHORT).show()
    }

    private fun dialogGravarToque(){
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this@ActivitySettings)
        val view = inflater.inflate(R.layout.dialog_gravar_toque, null)


        val tvSalvar = view.findViewById<TextView>(R.id.dialog_gravar_toque_salvar)
        val tvCancelar = view.findViewById<TextView>(R.id.dialog_gravar_toque_cancelar)
        val tvCronometro = view.findViewById<Chronometer>(R.id.dialog_gravar_toque_cronometro)


        val butPlay = view.findViewById<ImageView>(R.id.dialog_gravar_toque_play)
        val play = view.findViewById<ImageView>(R.id.dialog_but_circle_play)
        val stop = view.findViewById<ImageView>(R.id.dialog_but_circle_stop)
        val alertDialog = builder.create()
        alertDialog.setView(view)
        alertDialog.show()

        tvCancelar.setOnClickListener { alertDialog.dismiss() }
        tvSalvar.setOnClickListener { alertDialog.dismiss(); dialogToqueSalvo() }

        butPlay.setOnClickListener {
            if (butPlay.tag == "on"){
                butPlay.tag = "off"
                play.visibility = View.GONE
                stop.visibility = View.VISIBLE

                tvCronometro.base = SystemClock.elapsedRealtime()
                tvCronometro.start()

                startRecorder()



            }else{
                stopRecorder()
                butPlay.tag = "on"
                play.visibility = View.VISIBLE
                stop.visibility = View.GONE
                mFileName = externalCacheDir.absolutePath + "/noise_alert_music.3gp"
                setUrlGravar(mFileName)

                tvCronometro.stop()
            }
        }

    }

    private fun dialogTempoToque(){

        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this@ActivitySettings)
        val view = inflater.inflate(R.layout.dialog_tempo_toque, null)

        val alertDialog = builder.create()
        alertDialog.setView(view)
        alertDialog.show()
    }

    private fun setSwitch(){
        val sql = FuncSQLiteDB(applicationContext)
        val contentValues = ContentValues()
        if (switchVibrar.isChecked) {

            contentValues.put("vibracao", 1)
            sql.upadateDados(contentValues, "UserConfig", "id=1")
        }else{

            contentValues.put("vibracao", 0)
            sql.upadateDados(contentValues, "UserConfig", "id=1")
        }

        //   getSwith()

    }

    private fun contraintSetSwitch(){
        switchVibrar.isChecked = !switchVibrar.isChecked
        setSwitch()
    }

    private fun sobreNos(){
        startActivity(Intent(this, Activity_Sobre_Nos::class.java))
    }

    private fun setModoSom(modoSom:String){
        val sql = FuncSQLiteDB(applicationContext)
        val contentValues = ContentValues()
        contentValues.put("modo_som", modoSom)
        sql.upadateDados(contentValues, "UserConfig", "id=1")
    }

    private fun setUrlGravar(url:String){
        val sql = FuncSQLiteDB(applicationContext)
        val contentValues = ContentValues()
        contentValues.put("urlMusicGravar", url)
        if(sql.upadateDados(contentValues, "UserConfig", "id=1")){
            Log.i("Noise_Console","UpdateGravar com sucesso")
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
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
            val aux = cursor.getString(cursor.getColumnIndex("urlMusicGravar")).trim().toString()
            Log.i("Noise_Console","Url Gravar: $aux")
            return aux
        }catch (i: RuntimeException){
            Toast.makeText(applicationContext,"Erro na leitura de dados",Toast.LENGTH_SHORT).show()
            return ""

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

    private fun startRecorder() {

        mFileName = externalCacheDir.absolutePath + "/noise_alert_music.3gp"

        /*
        if (getModoMusica().equals("Personalizado"))
            mFileName = getUrl()
        */

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
            Toast.makeText(applicationContext,"Erro recorder Settings",Toast.LENGTH_SHORT).show()

        } catch (e: java.lang.SecurityException) {
            android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e))
            Toast.makeText(applicationContext,"Erro recorder Settings",Toast.LENGTH_SHORT).show()
        }

        try {

            mRecorder.start()
            Log.i("Console_Noise_Alert","Start Ok")

        } catch (e: java.lang.SecurityException) {
            android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e))
            Toast.makeText(applicationContext,"Erro recorder Settings",Toast.LENGTH_SHORT).show()

        } catch (e: IllegalStateException) {
            android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e))
            Toast.makeText(applicationContext,"Erro recorder Settings",Toast.LENGTH_SHORT).show()

            //   startRecorder()
        }

    }

    private fun stopRecorder() {


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

    private fun dialogToqueSalvo(){

        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this@ActivitySettings)
        val view = inflater.inflate(R.layout.dialog_toque_salvo, null)

        val butOk = view.findViewById<TextView>(R.id.dialog_toque_salvo_ok)



        val alertDialog = builder.create()
        alertDialog.setView(view)
        alertDialog.show()

        butOk.setOnClickListener { alertDialog.dismiss() }

    }

}