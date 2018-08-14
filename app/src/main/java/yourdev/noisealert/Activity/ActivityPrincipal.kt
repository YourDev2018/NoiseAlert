package yourdev.noisealert.Activity


import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView

import yourdev.noisealert.Class.CarouselItens
import yourdev.noisealert.R

import `in`.goodiebag.carouselpicker.CarouselPicker
import android.graphics.Color
import android.widget.TextView
import org.w3c.dom.Text


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
    lateinit var list:List<CarouselPicker.PickerItem>

    private var level: Int
        get() = clipDrawable.level
        set(level) {
            clipDrawable.level = level
        }
    lateinit var gambiarra: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal_nova)

        initializeUi()

        gambiarra.setOnClickListener{}


        butOnOff.setOnClickListener {
            //  phoneOnOff.setVisibility(View.INVISIBLE);
            if (butOnOff.tag.toString() == "on") {
               // Toast.makeText(applicationContext, "True", Toast.LENGTH_SHORT).show()
                efeitosOff()
            }else
                if (butOnOff.tag.toString() == "off") {
                    // Toast.makeText(applicationContext, "True", Toast.LENGTH_SHORT).show()
                    efeitosOn()
                }
        }
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
        level = 6000
        list = ArrayList()
        list = CarouselItens().createCarouselCinza(applicationContext,list)
        textAdapter = CarouselPicker.CarouselViewAdapter(applicationContext,list,0)

        carouselPicker1.adapter = textAdapter
        gambiarra = findViewById(R.id.gambiarra_off)


    }


}