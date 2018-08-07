package yourdev.noisealert.Activity

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import yourdev.noisealert.Class.FuncSQLiteDB
import yourdev.noisealert.R

class Activity_Inicial_Carregamento : AppCompatActivity() {

    val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__inicial_carregamento)

        // inicializa classe de funções sql
        val sql = FuncSQLiteDB(applicationContext)

        // cria array para retornar o id
        val colunas = arrayOf("id")

        // chama a função get, retornando id
        val cursor  = sql.getDados("UserConfig",colunas)

        // entra no TRY/CATCH, se ele ficar no try, a base de dados ja existe e a pessoa já usa o app
        // se for para o Catch, é a primeira vez do usuário, então, crio a tabela de dados e envio para apresentação

        try {
            cursor.moveToFirst()
            cursor.getString(cursor.getColumnIndex("id"))

            mHandler.postDelayed({  startActivity(Intent(this, Activity_Principal::class.java))
                finish()},2000)


        }catch (i: RuntimeException){

            val contentValues = ContentValues()
            contentValues.put("id",1);
            contentValues.put("onOff",1)
            contentValues.put("volume",100)
            contentValues.put("sensibilidade",70)
            contentValues.put("modo_som",applicationContext.getString(R.string.activity_configuracoes_modo_som_subtitulo).toString())
            contentValues.put("vibracao",1)
            contentValues.put("urlMusic","")
            contentValues.put("nameMusic","")
            contentValues.put("phoneState",1)

            if(!sql.setDados(contentValues,"UserConfig")){
                if (sql.upadateDados(contentValues,"UserConfig","id=1"))
                    mHandler.postDelayed({ startActivity(Intent(this, Activity_Apresentacao::class.java))},2000)
            }else{
                    mHandler.postDelayed({ startActivity(Intent(this, Activity_Apresentacao::class.java))},2000)

            }



        }

    }


}
