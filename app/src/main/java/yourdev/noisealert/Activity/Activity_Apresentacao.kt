package yourdev.noisealert.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import yourdev.noisealert.R

class Activity_Apresentacao : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apresentacao)

        // declarar variáveis
        val button = findViewById<Button>(R.id.apresentacao_button_iniciar)

        button.setOnClickListener{ startActivity(Intent(this, Activity_Principal::class.java))
        }



    }


}
