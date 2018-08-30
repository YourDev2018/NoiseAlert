package yourdev.noisealert.Activity

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.Typeface
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.opengl.Visibility
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
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
    lateinit var constraintTermosDeUso: ConstraintLayout

    lateinit var textViewModoSom: TextView
    lateinit var textViewBuscar: TextView
    lateinit var textViewGravar: TextView
    lateinit var textViewVibrar: TextView
    lateinit var textViewSobreNois: TextView
    lateinit var textViewTempoToque: TextView
    lateinit var textViewHome: TextView


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

    lateinit var one:TextView
    lateinit var three: TextView
    lateinit var five:TextView
    lateinit var seven:TextView
    lateinit var ten:TextView
    lateinit var two:TextView
    lateinit var four:TextView
    lateinit var six:TextView
    lateinit var eight:TextView
    lateinit var nine:TextView

    lateinit var seekBarDiscrete: SeekBar

    var tempoToque = 0





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initializeUi()
        fonts()

        contraintTempoToque.setOnClickListener { /*dialogTempoToque()*/ dialogTempoToque() }
        constraintVibrar.setOnClickListener { contraintSetSwitch() }
        switchVibrar.setOnClickListener { contraintSetSwitch() }
        constraintLayoutGravar.setOnClickListener { dialogGravarToque() }
        constraintLayoutBuscar.setOnClickListener { /* buscarMusica() */ emBreve() }
        constraintLayoutModoSom.setOnClickListener { dialogModoSom() }
        contraintSobreNos.setOnClickListener { sobreNos() }
//        constraintTermosDeUso.setOnClickListener { termsOfUse() }

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

        switchVibrar.isChecked = getSwitch() == 1


        butReturn.setOnClickListener { onBackPressed() }

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

        textViewBuscar = findViewById(R.id.act_settings_tv_buscar_toque)
        textViewGravar = findViewById(R.id.act_settings_tv_gravar_toque)
        textViewModoSom = findViewById(R.id.act_settings_tv_modo_som)
        textViewVibrar = findViewById(R.id.tv_vibrar)
        textViewSobreNois = findViewById(R.id.act_settings_tv_sobre_nos)
        textViewHome = findViewById(R.id.act_principal_text_view_home)
        textViewTempoToque = findViewById(R.id.act_settings_tv_tempo_toque)
      //  constraintTermosDeUso = findViewById(R.id.act_settings_contraint_termos_de_uso)


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

        val medium = Typeface.createFromAsset(assets, "roboto_medium.ttf")
        val regular = Typeface.createFromAsset(assets, "roboto_regular.ttf")

        tvPadrao.typeface = regular
        tvAlternativa.typeface = regular
        tvdioCrazy.typeface = regular
        tvToqueBuscar.typeface=regular
        tvToqueGravar.typeface=regular
        tvOk.typeface=medium
        tvCancelar.typeface = medium




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
        Toast.makeText(applicationContext,application.getText(R.string.em_desenvolvimento),Toast.LENGTH_SHORT).show()
    }

    private fun dialogGravarToque(){
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this@ActivitySettings)
        val view = inflater.inflate(R.layout.dialog_gravar_toque, null)


        val tvSalvar = view.findViewById<TextView>(R.id.dialog_gravar_toque_salvar)
        //val tvCancelar = view.findViewById<TextView>(R.id.dialog_gravar_toque_cancelar)
        val tvCronometro = view.findViewById<Chronometer>(R.id.dialog_gravar_toque_cronometro)

        var auxGravando = false
        val butPlay = view.findViewById<ImageView>(R.id.dialog_gravar_toque_play)
        val play = view.findViewById<ImageView>(R.id.dialog_but_circle_play)
        val stop = view.findViewById<ImageView>(R.id.dialog_but_circle_stop)
        val alertDialog = builder.create()
        alertDialog.setView(view)
        alertDialog.show()

        val medium = Typeface.createFromAsset(assets, "roboto_medium.ttf")
        val regular = Typeface.createFromAsset(assets, "roboto_regular.ttf")

        tvSalvar.typeface = medium


  //      tvCancelar.setOnClickListener { alertDialog.dismiss() }
        tvSalvar.setOnClickListener { alertDialog.dismiss(); if (auxGravando){dialogToqueSalvo();setUrlGravar(mFileName)}; stopRecorder(); if (tvCronometro.isActivated) { tvCronometro.stop()} }


        butPlay.setOnClickListener {
            if (butPlay.tag == "on"){
                butPlay.tag = "off"
                play.visibility = View.GONE
                stop.visibility = View.VISIBLE

                tvCronometro.base = SystemClock.elapsedRealtime()
                tvCronometro.start()

                startRecorder()
                auxGravando = true

            }else{
                stopRecorder()
                butPlay.tag = "on"
                play.visibility = View.VISIBLE
                stop.visibility = View.GONE
                mFileName = externalCacheDir.absolutePath + "/noise_alert_music.3gp"


                tvCronometro.stop()
            }
        }

    }

    private fun dialogTempoToque(){

        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this@ActivitySettings)
        val view = inflater.inflate(R.layout.dialog_tempo_toque, null)

        one = view.findViewById<TextView>(R.id.tempo_toque_one)
        three = view.findViewById<TextView>(R.id.tempo_toque_3)
        five = view.findViewById<TextView>(R.id.tempo_toque_5)
        seven = view.findViewById<TextView>(R.id.tempo_toque_7)
        ten = view.findViewById<TextView>(R.id.tempo_toque_10 )
        two = view.findViewById(R.id.tempo_toque_2)
        four = view.findViewById(R.id.tempo_toque_4)
        six = view.findViewById(R.id.tempo_toque_6)
        eight = view.findViewById(R.id.tempo_toque_8)
        nine = view.findViewById(R.id.tempo_toque_9)

        val tvSalvar = view.findViewById<TextView>(R.id.dialog_tempo_toque_ok)
        val tvCancelar = view.findViewById<TextView>(R.id.dialog_tempo_toque_cancelar)

        val medium = Typeface.createFromAsset(assets, "roboto_medium.ttf")
        val regular = Typeface.createFromAsset(assets, "roboto_regular.ttf")

        tvCancelar.typeface = medium
        tvSalvar.typeface = medium

        one.typeface = medium
        two.typeface = medium
        three.typeface = medium
        four.typeface = medium
        five.typeface = medium
        six.typeface = medium
        seven.typeface = medium
        eight.typeface = medium
        nine.typeface = medium
        ten.typeface = medium

        seekBarDiscrete = view.findViewById(R.id.dialog_tempo_toque_seek_bar)


        val auxTempo = getSalvarTempoToque()
        seekBarDiscrete.progress = auxTempo

        if (auxTempo == 1)
            one.visibility = View.VISIBLE
        if (auxTempo == 2)
            two.visibility = View.VISIBLE

        if (auxTempo ==3)
            three.visibility = View.VISIBLE

        if (auxTempo ==4)
            four.visibility = View.VISIBLE
        if (auxTempo ==5)
            five.visibility = View.VISIBLE
        if (auxTempo ==6)
            six.visibility = View.VISIBLE
        if (auxTempo ==7)
            seven.visibility = View.VISIBLE
        if (auxTempo ==8)
            eight.visibility = View.VISIBLE
        if (auxTempo ==9)
            nine.visibility = View.VISIBLE
        if (auxTempo ==10)
            ten.visibility = View.VISIBLE

        val alertDialog = builder.create()
        alertDialog.setView(view)
        alertDialog.show()



        seekBarDiscrete.setOnSeekBarChangeListener(object : ViewPager.OnPageChangeListener, SeekBar.OnSeekBarChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {



            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress == 1) {
                    one.visibility = View.VISIBLE
                    three.visibility = View.GONE
                    seven.visibility = View.GONE
                    five.visibility = View.GONE
                    ten.visibility = View.GONE

                    two.visibility = View.GONE
                    four.visibility = View.GONE
                    six.visibility = View.GONE
                    eight.visibility = View.GONE
                    nine.visibility = View.GONE
                }
                if (progress == 3) {
                    one.visibility = View.GONE
                    three.visibility = View.VISIBLE
                    seven.visibility = View.GONE
                    five.visibility = View.GONE
                    ten.visibility = View.GONE

                    two.visibility = View.GONE
                    four.visibility = View.GONE
                    six.visibility = View.GONE
                    eight.visibility = View.GONE
                    nine.visibility = View.GONE
                }

                if (progress == 5) {
                    one.visibility = View.GONE
                    three.visibility = View.GONE
                    seven.visibility = View.GONE
                    five.visibility = View.VISIBLE
                    ten.visibility = View.GONE

                    two.visibility = View.GONE
                    four.visibility = View.GONE
                    six.visibility = View.GONE
                    eight.visibility = View.GONE
                    nine.visibility = View.GONE
                }

                if (progress == 7) {
                    one.visibility = View.GONE
                    three.visibility = View.GONE
                    five.visibility = View.GONE
                    seven.visibility = View.VISIBLE
                    ten.visibility = View.GONE

                    two.visibility = View.GONE
                    four.visibility = View.GONE
                    six.visibility = View.GONE
                    eight.visibility = View.GONE
                    nine.visibility = View.GONE
                }

                if (progress == 10) {
                    one.visibility = View.GONE
                    three.visibility = View.GONE
                    seven.visibility = View.GONE
                    five.visibility = View.GONE
                    ten.visibility = View.VISIBLE

                    two.visibility = View.GONE
                    four.visibility = View.GONE
                    six.visibility = View.GONE
                    eight.visibility = View.GONE
                    nine.visibility = View.GONE
                }

                if (progress == 2) {
                    one.visibility = View.GONE
                    three.visibility = View.GONE
                    seven.visibility = View.GONE
                    five.visibility = View.GONE
                    ten.visibility = View.GONE

                    two.visibility = View.VISIBLE
                    four.visibility = View.GONE
                    six.visibility = View.GONE
                    eight.visibility = View.GONE
                    nine.visibility = View.GONE
                }

                if (progress == 4) {
                    one.visibility = View.GONE
                    three.visibility = View.GONE
                    seven.visibility = View.GONE
                    five.visibility = View.GONE
                    ten.visibility = View.GONE

                    two.visibility = View.GONE
                    four.visibility = View.VISIBLE
                    six.visibility = View.GONE
                    eight.visibility = View.GONE
                    nine.visibility = View.GONE
                }

                if (progress == 6) {
                    one.visibility = View.GONE
                    three.visibility = View.GONE
                    seven.visibility = View.GONE
                    five.visibility = View.GONE
                    ten.visibility = View.GONE

                    two.visibility = View.GONE
                    four.visibility = View.GONE
                    six.visibility = View.VISIBLE
                    eight.visibility = View.GONE
                    nine.visibility = View.GONE
                }

                if (progress == 8) {
                    one.visibility = View.GONE
                    three.visibility = View.GONE
                    seven.visibility = View.GONE
                    five.visibility = View.GONE
                    ten.visibility = View.GONE

                    two.visibility = View.GONE
                    four.visibility = View.GONE
                    six.visibility = View.GONE
                    eight.visibility = View.VISIBLE
                    nine.visibility = View.GONE
                }

                if (progress == 9) {
                    one.visibility = View.GONE
                    three.visibility = View.GONE
                    seven.visibility = View.GONE
                    five.visibility = View.GONE
                    ten.visibility = View.GONE

                    two.visibility = View.GONE
                    four.visibility = View.GONE
                    six.visibility = View.GONE
                    eight.visibility = View.GONE
                    nine.visibility = View.VISIBLE
                }






            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //Log.i("Noise_Alert_Console",seekBar?.progress.toString())
                tempoToque = seekBar!!.progress
                if (tempoToque ==0){
                    tempoToque = 1
                }
            }

        })


        tvSalvar.setOnClickListener { setGravarTempoToque(tempoToque); alertDialog.dismiss() }
        tvCancelar.setOnClickListener { alertDialog.dismiss() }


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

    private fun getSwitch():Int{
        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("vibracao")
        val cursor  = sql.getDados("UserConfig",colunas)

        try {
            cursor.moveToFirst()
            return cursor.getInt(cursor.getColumnIndex("vibracao"))
        }catch (i: RuntimeException){
            Log.i("Console_Noise_Alert_toq", "Entrou no Runtime")
            return 0
        }
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

    private fun setGravarTempoToque(aux:Int){

        val sql = FuncSQLiteDB(applicationContext)
        val contentValues = ContentValues()
        contentValues.put("tempoDeToque", aux)
        if(sql.upadateDados(contentValues, "UserConfig", "id=1")){
            Log.i("Noise_Console","UpdateGravar com sucesso")
        }

    }

    private fun getSalvarTempoToque():Int{

        val sql = FuncSQLiteDB(applicationContext)
        val colunas = arrayOf("tempoDeToque")
        val cursor  = sql.getDados("UserConfig",colunas)
        try {
            cursor.moveToFirst()
            return cursor.getInt(cursor.getColumnIndex("tempoDeToque")).toInt()
        }catch (i: RuntimeException){
            Toast.makeText(applicationContext,"Erro na leitura de dados",Toast.LENGTH_SHORT).show()
            return 4

        }

    }


    override fun onBackPressed() {
        super.onBackPressed()

        val colunas = arrayOf("id","sensibilidade","modo_som","vibracao","urlMusicBuscar","urlMusicGravar","nameMusic","phoneState","tempoDeToque")


        val sql = FuncSQLiteDB(applicationContext)
        // chama a função get, retornando id
        val cursor  = sql.getDados("UserConfig",colunas)

        // entra no TRY/CATCH, se ele ficar no try, a base de dados ja existe e a pessoa já usa o app
        // se for para o Catch, é a primeira vez do usuário, então, crio a tabela de dados e envio para apresentação
        try {

            cursor.moveToFirst()
            val intent = Intent(this,ActivityPrincipal::class.java)


            intent.putExtra("modo_som",cursor.getString(cursor.getColumnIndex("modo_som")))
            intent.putExtra("vibracao",cursor.getInt(cursor.getColumnIndex("vibracao")))

            intent.putExtra("phoneState",cursor.getInt(cursor.getColumnIndex("phoneState")))
            intent.putExtra("tempoDeToque",cursor.getInt(cursor.getColumnIndex("tempoDeToque")))

            startActivity(intent)
        //startActivity(Intent(this, ActivityPrincipal::class.java))
            finish()
        }catch (i: RuntimeException){


        }
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

        //    mPlayer = mp.pauseMusic(mPlayer)
        }catch (i: IllegalStateException ){
          //  mPlayer = mp.pauseMusic(mPlayer)
        }catch (i: RuntimeException){
          //  mPlayer = mp.pauseMusic(mPlayer)
        }

    }

    private fun dialogToqueSalvo(){

        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this@ActivitySettings)
        val view = inflater.inflate(R.layout.dialog_toque_salvo, null)

        val butOk = view.findViewById<TextView>(R.id.dialog_toque_salvo_ok)
        val textView = view.findViewById<TextView>(R.id.dialog_toque_salvo_modo_som)



        val medium = Typeface.createFromAsset(assets, "roboto_medium.ttf")
        val regular = Typeface.createFromAsset(assets, "roboto_regular.ttf")

        butOk.typeface = medium
        textView.typeface = regular

        val alertDialog = builder.create()
        alertDialog.setView(view)
        alertDialog.show()

        butOk.setOnClickListener { alertDialog.dismiss() }

    }



    private fun fonts() {
        val regular = Typeface.createFromAsset(assets, "roboto_regular.ttf")
        val medium = Typeface.createFromAsset(assets, "roboto_medium.ttf")

        textViewSobreNois.typeface = medium
        textViewVibrar.typeface = medium
        textViewModoSom.typeface = medium
        textViewGravar.typeface = medium
        textViewBuscar.typeface = medium
        textViewTempoToque.typeface = medium
        subTitulo.typeface = regular
        textViewHome.typeface = medium
    }


    private fun termsOfUse(){
     //   startActivity(Intent(this, ActivityTermsOfUse::class.java))
    }


}