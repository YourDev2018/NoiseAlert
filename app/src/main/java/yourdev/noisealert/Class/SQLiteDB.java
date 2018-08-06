package yourdev.noisealert.Class;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDB extends SQLiteOpenHelper {

    // SQLiteOpenHelper: Classe responsável pela criação do banco e também responsável pelo versionamento do mesmo.

    // Método onCreate(): é chamado quando a aplicação cria o banco de dados pela primeira vez.
    // Nesse método devem ter todas as diretrizes de criação e população inicial do banco.

    //Método onUpgrade(): é o método responsável por atualizar o banco de dados com alguma informação estrutural que
    // tenha sido alterada. Ele sempre é chamado quando uma atualização é necessária, para não ter nenhum tipo
    // de inconsistência de dados entre o banco existente no aparelho e o novo que a aplicação irá utilizar.

    // tabela 1 = id, volume;
    // tabela 2 = id, audioGravado;

    private static final String NOME_BANCO = "NoiseAlertDB.db";

    public final String TABELA = "UserConfig";


    private static final int VERSAO = 3;

    public SQLiteDB(Context context) {
        super(context, NOME_BANCO,null,VERSAO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String script = "CREATE TABLE "+ TABELA + " (id integer primary key, onOff integer, volume integer, sensibilidade integer, modo_som text, vibracao int, urlMusic text, nameMusic text, phoneState integer )";
      //  String script = "CREATE TABLE "+ TABELA + " (id integer primary key , nameMusic text )";

        db.execSQL(script);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       //  db.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);

    }


}
