package elsuper.david.com.spacetravel.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import elsuper.david.com.spacetravel.model.Apod;

/**
 * Created by Andrés David García Gómez.
 */
public class ApodDataSource {
    //Sacamos a db del constructor para poder abrir y cerrar la conexión en cada método utilizado
    private SQLiteDatabase db = null;
    private MySqliteHelper helper;

    public ApodDataSource(Context context) {
        helper = new MySqliteHelper(context);
    }

    public long saveApod(Apod modelApod){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Preparamos el modelo a guardar
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySqliteHelper.COLUMN_APOD_COPYRIGHT, modelApod.getCopyright());
        contentValues.put(MySqliteHelper.COLUMN_APOD_DATE, modelApod.getDate());
        contentValues.put(MySqliteHelper.COLUMN_APOD_EXPLANATION, modelApod.getExplanation());
        contentValues.put(MySqliteHelper.COLUMN_APOD_HDURL, modelApod.getHdurl());
        contentValues.put(MySqliteHelper.COLUMN_APOD_MEDIA_TYPE, modelApod.getMediaType());
        contentValues.put(MySqliteHelper.COLUMN_APOD_SERVICE_VERSION, modelApod.getServiceVersion());
        contentValues.put(MySqliteHelper.COLUMN_APOD_TITLE, modelApod.getTitle());
        contentValues.put(MySqliteHelper.COLUMN_APOD_URL, modelApod.getUrl());

        //Insertamos el registro
        long result = db.insert(MySqliteHelper.TABLENAME_APOD, null, contentValues);

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return result;
    }

    public List<Apod> getAllApods(){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Consultamos toda la tabla photo_table
        List<Apod> modelApodList = new ArrayList<>();
        Cursor cursor = db.query(MySqliteHelper.TABLENAME_APOD,null,null,null,null,null,null);

        //Agregamos cada elemento del cursor a la lista
        while(cursor.moveToNext()){
            Apod modelApod = new Apod();
            modelApod.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ID)));
            modelApod.setCopyright(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_COPYRIGHT)));
            modelApod.setDate(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_DATE)));
            modelApod.setExplanation(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_EXPLANATION)));
            modelApod.setHdurl(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_HDURL)));
            modelApod.setMediaType(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_MEDIA_TYPE)));
            modelApod.setServiceVersion(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_SERVICE_VERSION)));
            modelApod.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_TITLE)));
            modelApod.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_URL)));
            modelApodList.add(modelApod);
        }

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return  modelApodList;
    }

    public void deleteApod(int apodId){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Eliminamos el registro
        db.delete(MySqliteHelper.TABLENAME_APOD, MySqliteHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(apodId)});

        //Cerramos la conexión
        if(db.isOpen())
            db.close();
    }

    public Apod getApod(int apodId){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Objeto a devolver
        Apod modelApod = new Apod();

        //Consultamos
        Cursor cursor = db.query(MySqliteHelper.TABLENAME_APOD, null, MySqliteHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(apodId)},null,null,null);

        //Obtenemos el primer registro del cursor
        if(cursor.moveToFirst()){
            modelApod.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ID)));
            modelApod.setCopyright(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_COPYRIGHT)));
            modelApod.setDate(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_DATE)));
            modelApod.setExplanation(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_EXPLANATION)));
            modelApod.setHdurl(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_HDURL)));
            modelApod.setMediaType(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_MEDIA_TYPE)));
            modelApod.setServiceVersion(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_SERVICE_VERSION)));
            modelApod.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_TITLE)));
            modelApod.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_URL)));
        }
        else
            modelApod = null;

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return modelApod;
    }

    public Apod getApod(String title, String date){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Objeto a devolver
        Apod modelApod = new Apod();

        //Consultamos
        Cursor cursor = db.query(MySqliteHelper.TABLENAME_APOD, null,
                MySqliteHelper.COLUMN_APOD_TITLE + "=? and " + MySqliteHelper.COLUMN_APOD_DATE + "=?",
                new String[]{title,date},null,null,null);

        //Obtenemos el primer registro del cursor
        if(cursor.moveToFirst()){
            modelApod.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ID)));
            modelApod.setCopyright(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_COPYRIGHT)));
            modelApod.setDate(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_DATE)));
            modelApod.setExplanation(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_EXPLANATION)));
            modelApod.setHdurl(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_HDURL)));
            modelApod.setMediaType(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_MEDIA_TYPE)));
            modelApod.setServiceVersion(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_SERVICE_VERSION)));
            modelApod.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_TITLE)));
            modelApod.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_APOD_URL)));
        }
        else
            modelApod = null;

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return modelApod;
    }
}
