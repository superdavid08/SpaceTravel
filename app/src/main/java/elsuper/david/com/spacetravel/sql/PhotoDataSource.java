package elsuper.david.com.spacetravel.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import elsuper.david.com.spacetravel.model.Photo;

/**
 * Created by Andrés David García Gómez.
 */
public class PhotoDataSource {

    //Sacamos a db del constructor para poder abrir y cerrar la conexión en cada método utilizado
    private SQLiteDatabase db = null;
    private MySqliteHelper helper;

    public PhotoDataSource(Context context) {
        helper = new MySqliteHelper(context);
    }

    public long savePhoto(Photo modelPhoto){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Preparamos el modelo a guardar
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySqliteHelper.COLUMN_PHOTO_ID, modelPhoto.getId());
        contentValues.put(MySqliteHelper.COLUMN_PHOTO_SOL, modelPhoto.getSol());
        contentValues.put(MySqliteHelper.COLUMN_PHOTO_IMG_SRC, modelPhoto.getImgSrc());
        contentValues.put(MySqliteHelper.COLUMN_PHOTO_EARTH_DATE, modelPhoto.getEarthDate());

        //Insertamos el registro
        long result = db.insert(MySqliteHelper.TABLENAME_PHOTO, null, contentValues);

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return result;
    }

    public List<Photo> getAllPhotos(){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Consultamos toda la tabla photo_table
        List<Photo> modelPhotoList = new ArrayList<>();
        Cursor cursor = db.query(MySqliteHelper.TABLENAME_PHOTO,null,null,null,null,null,null);

        //Agregamos cada elemento del cursor a la lista
        while(cursor.moveToNext()){
            Photo modelPhoto = new Photo();
            modelPhoto.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_PHOTO_ID)));
            modelPhoto.setSol(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_PHOTO_SOL)));
            modelPhoto.setImgSrc(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_PHOTO_IMG_SRC)));
            modelPhoto.setEarthDate(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_PHOTO_EARTH_DATE)));
            modelPhotoList.add(modelPhoto);
        }

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return  modelPhotoList;
    }

    public void deletePhoto(Photo modelPhoto){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Eliminamos el registro
        db.delete(MySqliteHelper.TABLENAME_PHOTO, MySqliteHelper.COLUMN_PHOTO_ID + "=?",
                new String[]{String.valueOf(modelPhoto.getId())});

        //Cerramos la conexión
        if(db.isOpen())
            db.close();
    }

    public Photo getPhoto(int id){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Objeto a devolver
        Photo modelPhoto = new Photo();

        //Consultamos
        Cursor cursor = db.query(MySqliteHelper.TABLENAME_PHOTO, null, MySqliteHelper.COLUMN_PHOTO_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null);

        //Obtenemos el primer registro del cursor
        if(cursor.moveToFirst()){
            modelPhoto.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_PHOTO_ID)));
            modelPhoto.setSol(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_PHOTO_SOL)));
            modelPhoto.setImgSrc(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_PHOTO_IMG_SRC)));
            modelPhoto.setEarthDate(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_PHOTO_EARTH_DATE)));
        }
        else
            modelPhoto = null;

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return modelPhoto;
    }
}
