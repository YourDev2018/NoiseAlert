package yourdev.noisealert.Activity

import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import yourdev.noisealert.R

class Activity_Sobre_Nos : AppCompatActivity() {

    lateinit var butReturn:ImageView
    lateinit var returnSettingsTv:TextView
    lateinit var tituloTv:TextView
    lateinit var textSobreNos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__sobre__nos)

        initializeUi()

        butReturn.setOnClickListener {
          //  startActivity(Intent(this, Activity_Principal::class.java))
            finish()
        }


    }


    fun initializeUi(){

        butReturn = findViewById(R.id.act_settings_return)
        tituloTv = findViewById(R.id.titulo_sobre_nos)
        returnSettingsTv = findViewById(R.id.act_principal_text_view_home)
        textSobreNos = findViewById(R.id.text_sobre_nos)
    //    butConfig = findViewById(R.id.activity_principal_sobre_nos_config)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun fonts() {
        val regular = Typeface.createFromAsset(assets, "roboto_regular.ttf")
        val medium = Typeface.createFromAsset(assets, "roboto_medium.ttf")

        tituloTv.typeface = medium
        textSobreNos.typeface = medium
        returnSettingsTv.typeface = medium
    }




    }
