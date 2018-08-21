package yourdev.noisealert.Activity

import android.content.ContentValues
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import yourdev.noisealert.Class.FuncSQLiteDB
import yourdev.noisealert.R

class ActivityLanding: AppCompatActivity() {

    val mHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        // trabalhar futuramente com modificação de amplitude

        // inicializa classe de funções sql
        val sql = FuncSQLiteDB(applicationContext)

        // cria array para retornar o id
        val colunas = arrayOf("id","sensibilidade","modo_som","vibracao","urlMusicBuscar","urlMusicGravar","nameMusic","phoneState","tempoDeToque")



        // chama a função get, retornando id
        val cursor  = sql.getDados("UserConfig",colunas)

        // entra no TRY/CATCH, se ele ficar no try, a base de dados ja existe e a pessoa já usa o app
        // se for para o Catch, é a primeira vez do usuário, então, crio a tabela de dados e envio para apresentação
        try {

            cursor.moveToFirst()
            val intent = Intent(this,ActivityPrincipal::class.java)
            intent.putExtra("id",cursor.getInt(cursor.getColumnIndex("id")))
            intent.putExtra("sensibilidade",cursor.getInt(cursor.getColumnIndex("sensibilidade")))
            intent.putExtra("modo_som",cursor.getString(cursor.getColumnIndex("modo_som")))
            intent.putExtra("vibracao",cursor.getInt(cursor.getColumnIndex("vibracao")))
            intent.putExtra("urlMusicBuscar",cursor.getString(cursor.getColumnIndex("urlMusicBuscar")))
            intent.putExtra("urlMusicGravar",cursor.getString(cursor.getColumnIndex("urlMusicGravar")))
            intent.putExtra("nameMusic",cursor.getString(cursor.getColumnIndex("nameMusic")))
            intent.putExtra("phoneState",cursor.getInt(cursor.getColumnIndex("phoneState")))
            intent.putExtra("tempoDeToque",cursor.getInt(cursor.getColumnIndex("tempoDeToque")))



            mHandler.postDelayed({  startActivity(intent)
                finish()},2000)


        }catch (i: RuntimeException){

            Log.i("Noise_Console","Landing: Entrou no catch")

            val contentValues = ContentValues()
            contentValues.put("id",1)
            contentValues.put("sensibilidade",10000)
            contentValues.put("modo_som","padrao")
            contentValues.put("vibracao",1)
            contentValues.put("urlMusicBuscar","")
            contentValues.put("urlMusicGravar","")
            contentValues.put("nameMusic","")
            contentValues.put("phoneState",1)
            contentValues.put("tempoDeToque",4)

            val intent = Intent(this,ActivityPrincipal::class.java)
            intent.putExtra("id",1)
            intent.putExtra("sensibilidade",10000)
            intent.putExtra("modo_som","padrao")
            intent.putExtra("vibracao",1)
            intent.putExtra("urlMusicBuscar","")
            intent.putExtra("urlMusicGravar","")
            intent.putExtra("nameMusic","")
            intent.putExtra("phoneState",1)
            intent.putExtra("tempoDeToque",4)



            if(!sql.setDados(contentValues,"UserConfig")){
                if (sql.upadateDados(contentValues,"UserConfig","id=1"))
                    mHandler.postDelayed({startActivity(intent);finish();},2000)
            }else{
                    mHandler.postDelayed({ startActivity(intent);finish();},2000)

            }

        }




    }
}
