package yourdev.noisealert.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import yourdev.noisealert.R

class Activity_Sobre_Nos : AppCompatActivity() {

    lateinit var butReturn:ImageView


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
    //    butConfig = findViewById(R.id.activity_principal_sobre_nos_config)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }




}
