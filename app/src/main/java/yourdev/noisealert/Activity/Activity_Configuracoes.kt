package yourdev.noisealert.Activity

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.media.AudioManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.KeyEvent
import android.widget.*
import kotlinx.android.synthetic.main.activity__configuracoes.*
import yourdev.noisealert.Class.FuncSQLiteDB
import yourdev.noisealert.R
import android.content.ContentResolver
import android.media.MediaRecorder
import android.support.v4.app.ActivityCompat
import android.view.View
import yourdev.noisealert.Class.ManagePermissions


@Suppress("UNREACHABLE_CODE")
class Activity_Configuracoes : AppCompatActivity(){


    lateinit var butReturn: ImageView
    lateinit var sobreNos: ImageView
    lateinit var constraintVibrar: ConstraintLayout
    lateinit var textViewVibrar : TextView
    lateinit var textViewModoSomTitulo: TextView
    lateinit var textViewModosubTitulo: TextView
    lateinit var imageViewModoSomImageView: ImageView
    lateinit var constraintLayoutModoSom: ConstraintLayout
    lateinit var switchVibrar: Switch
    lateinit var constraintLayoutVolume: ConstraintLayout
    lateinit var textViewVolume: TextView
    lateinit var constraintLayoutBuscar: ConstraintLayout
    lateinit var textViewBuscar: TextView
    lateinit var constraintLayoutGravar: ConstraintLayout
    lateinit var butGravar: ImageView
    lateinit var textViewGravar: ImageView

    private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private lateinit var managePermissions: ManagePermissions

    private var mFileName = ""
    var mRecorder = MediaRecorder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__configuracoes)

        initializeUi()
        getSwith()
        getModoSom()

        butReturn.setOnClickListener{ finish()/*startActivity(Intent(this, Activity_Principal::class.java)) */}
        sobreNos.setOnClickListener{
            startActivity(Intent(this, Activity_Sobre_Nos::class.java))
            finish()
        }

        constraintLayoutModoSom.setOnClickListener {  escolherModoSom() }
        imageViewModoSomImageView.setOnClickListener { escolherModoSom() }
        textViewModoSomTitulo.setOnClickListener { escolherModoSom() }
        textViewModosubTitulo.setOnClickListener { escolherModoSom() }

        constraintVibrar.setOnClickListener { setVibrar(); }
        textViewVibrar.setOnClickListener {setVibrar() }
        switchVibrar.setOnClickListener{setSwitch()}

        textViewVolume.setOnClickListener { volumeControlStream = 10}
        constraintLayoutVolume.setOnClickListener { volumeControlStream = AudioManager.STREAM_MUSIC; }

        constraintLayoutBuscar.setOnClickListener { /*buscarMusica()*/ }
        textViewBuscar.setOnClickListener{ /*buscarMusica()*/ }

        butGravar.setOnLongClickListener{ return@setOnLongClickListener false}





    }
/*
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

    */


    fun initializeUi(){

        butReturn = findViewById(R.id.activity_config_return)
        sobreNos = findViewById(R.id.activity_config_menu_sobre_nos)
        constraintVibrar = findViewById(R.id.activity_configuracoes_vibracao)
        textViewVibrar = findViewById(R.id.activity_configuracoes_text_view_vibracao)
        switchVibrar = findViewById(R.id.activity_configuracoes_switch_vibracao)

        textViewModoSomTitulo = findViewById(R.id.activity_configuracoes_modo_som_text_view)
        textViewModosubTitulo = findViewById(R.id.activity_configuracoes_modo_som_subtitulo)
        imageViewModoSomImageView = findViewById(R.id.activity_configuracoes_modo_som_image_view)
        constraintLayoutModoSom = findViewById(R.id.activity_configuracoes_modo_som_contrastraint)

        constraintLayoutVolume = findViewById(R.id.activity_volume_constraint)
        textViewVolume  = findViewById(R.id.activity_configuracoes_text_view_volume)

        constraintLayoutBuscar = findViewById(R.id.activity_buscar_toque_constraint)
        textViewBuscar = findViewById(R.id.activity_configuracoes_text_view_buscar_toque)

        constraintLayoutGravar = findViewById(R.id.activity_configuracoes_contraint_layout_gravar_toque)
        textViewBuscar = findViewById(R.id.activity_configuracoes_text_view_buscar_toque)
        butGravar = findViewById(R.id.activity_configuracoes_but_gravar_toque)


    }

    private fun setVibrar(){
        val sql = FuncSQLiteDB(applicationContext)
        val contentValues = ContentValues()
        if (!switchVibrar.isChecked) {
            switchVibrar.isChecked =true
            contentValues.put("vibracao", 1)
            sql.upadateDados(contentValues, "UserConfig", "id=1")
        }else{
            switchVibrar.isChecked = false
            contentValues.put("vibracao", 0)
            sql.upadateDados(contentValues, "UserConfig", "id=1")
        }

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

    private fun setModoSom(){
        val sql = FuncSQLiteDB(applicationContext)
        val contentValues = ContentValues()
        contentValues.put("modo_som", textViewModosubTitulo.text.toString())
        sql.upadateDados(contentValues, "UserConfig", "id=1")
    }

    private fun getModoSom(){
        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("modo_som")
        val cursor  = sql.getDados("UserConfig",colunas)

        try {
            cursor.moveToFirst()
            Log.i("Console_Noise_Alert_ms",cursor.getString(cursor.getColumnIndex("modo_som")).trim())
            textViewModosubTitulo.text = cursor.getString(cursor.getColumnIndex("modo_som")).trim()
        }catch (i: RuntimeException){
            Toast.makeText(applicationContext,"Erro na leitura de dados",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getSwith(){

        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("vibracao")
        val cursor  = sql.getDados("UserConfig",colunas)
        try {
            cursor.moveToFirst()
            Log.i("Console_Noise_Alert_s",cursor.getString(cursor.getColumnIndex("vibracao")).toInt().toString())
            switchVibrar.isChecked = (cursor.getString(cursor.getColumnIndex("vibracao"))).trim().toInt() == 1
        }catch (i: RuntimeException){
         //   Toast.makeText(applicationContext,"Erro na leitura de dados", Toast.LENGTH_SHORT).show()


        }


    }

    private fun getURL(): Boolean{

        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("urlMusic")
        val cursor  = sql.getDados("UserConfig",colunas)
        try {
            cursor.moveToFirst()
            Log.i("Console_Noise_Alert_url",cursor.getString(cursor.getColumnIndex("urlMusic")).toInt().toString())
            val aux = cursor.getString(cursor.getColumnIndex("urlMusic")).toString()

            return aux == ""
            //  switchVibrar.isChecked = (cursor.getString(cursor.getColumnIndex("urlMusic))).trim().toInt() == 1
        }catch (i: RuntimeException){
            //   Toast.makeText(applicationContext,"Erro na leitura de dados", Toast.LENGTH_SHORT).show()
            return false

        }

    }

    private fun escolherModoSom(){
        if(textViewModosubTitulo.text.trim().toString() == applicationContext.getString(R.string.activity_configuracoes_modo_som_subtitulo).trim().toString()){
            Log.i("Noise_Alert_Console",applicationContext.getString(R.string.activity_configuracoes_modo_som_subtitulo_alternativa).toString())
            textViewModosubTitulo.text = applicationContext.getString(R.string.activity_configuracoes_modo_som_subtitulo_alternativa).toString()
        }else
            if(textViewModosubTitulo.text.toString() == applicationContext.getString(R.string.activity_configuracoes_modo_som_subtitulo_alternativa).toString()){
                textViewModosubTitulo.text = applicationContext.getString(R.string.activity_configuracoes_modo_som_subtitulo_crazy).toString()
            }else
                if(textViewModosubTitulo.text.toString() == applicationContext.getString(R.string.activity_configuracoes_modo_som_subtitulo_crazy).toString()){
                    if (getURL())
                        textViewModosubTitulo.text = applicationContext.getString(R.string.activity_configuracoes_modo_som_subtitulo_personalizado).toString()
                    else {
                        Toast.makeText(applicationContext, "Para usar o modo Personalizado, você precisa gravar ou buscar um toque", Toast.LENGTH_SHORT).show()
                        textViewModosubTitulo.text = applicationContext.getString(R.string.activity_configuracoes_modo_som_subtitulo).toString()
                    }
                }else
                    if(textViewModosubTitulo.text.toString() == applicationContext.getString( R.string.activity_configuracoes_modo_som_subtitulo_personalizado).toString()){
                        textViewModosubTitulo.text = applicationContext.getString(R.string.activity_configuracoes_modo_som_subtitulo).toString()
                    }
    }
 //   stre

    override fun onPause() {
        super.onPause()

        setModoSom()
    }

    private fun gravarAudio(){
        mFileName = externalCacheDir.absolutePath + "personalizada.3gp"

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

/*
    private fun buscarMusica(){




        val list = listOf<String>(
                Manifest.permission.RECORD_AUDIO

        )

        managePermissions = ManagePermissions(this,list,REQUEST_RECORD_AUDIO_PERMISSION)
        ActivityCompat.requestPermissions(this, permissions,REQUEST_RECORD_AUDIO_PERMISSION)

        val musicResolver = contentResolver
        val musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val musicCursor = musicResolver.query(musicUri, null, null, null, null)
    }
*/

}
