package yourdev.noisealert.Class;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FuncSQLiteDB {

    SQLiteDatabase db;
    private SQLiteDB banco;

    public FuncSQLiteDB(Context context) {

        banco = new SQLiteDB(context);

    }

    public boolean setDados(ContentValues contentValues, String tabela){

        long result;

        db = banco.getWritableDatabase();

        result = db.insert(tabela,null,contentValues);
        db.close();

        if (result ==-1)
            return false;
        else
            return true;
    }

    public Cursor getDados(String tabela, String[] colunas){

        Cursor cursor;
        db = banco.getReadableDatabase();
        cursor = db.query(tabela,colunas, null, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();

        return cursor;

    }

    @SuppressLint("Recycle")
    public boolean getMusicExist(String[] nameMusic){

        Cursor cursor;
        db = banco.getReadableDatabase();

        String query = "SELECT nameMusic FROM MusicasGravadas where nameMusic = ? ";

        cursor = db.rawQuery(query,nameMusic);
                //.query(tabela,colunas, selectionColuna, selectionArgs, null, null, null, null);
        db.close();
        if(cursor==null){
            return  false;
        }else{
            return true;
        }

    }

    public boolean upadateDados(ContentValues contentValues,String tabela, String where){

        db = banco.getWritableDatabase();
        long result = db.update(tabela,contentValues,where,null);
        db.close();
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean deleteDados(String tabela, String where){

        db = banco.getReadableDatabase();
        long result = db.delete(tabela,where,null);
        db.close();
        if (result == -1)
            return false;
        else
            return true;

    }

}
