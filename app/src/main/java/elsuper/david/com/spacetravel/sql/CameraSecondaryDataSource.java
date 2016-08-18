package elsuper.david.com.spacetravel.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import elsuper.david.com.spacetravel.model.CameraSecondary;

/**
 * Created by Andrés David García Gómez.
 */
public class CameraSecondaryDataSource {
    //Sacamos a db del constructor para poder abrir y cerrar la conexión en cada método utilizado
    private SQLiteDatabase db = null;
    private MySqliteHelper helper;

    public CameraSecondaryDataSource(Context context) {
        helper = new MySqliteHelper(context);
    }

    public long saveCameraSecondary(CameraSecondary modelCameraSecondary, int photoId){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Preparamos el modelo a guardar
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySqliteHelper.COLUMN_CAMERASECONDARY_PHOTO_ID, photoId);
        contentValues.put(MySqliteHelper.COLUMN_CAMERASECONDARY_NAME, modelCameraSecondary.getName());
        contentValues.put(MySqliteHelper.COLUMN_CAMERASECONDARY_FULL_NAME, modelCameraSecondary.getFullName());

        //Insertamos el registro
        long result = db.insert(MySqliteHelper.TABLENAME_CAMERASECONDARY, null, contentValues);

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return result;
    }

    public List<CameraSecondary> getAllCamerasSecondariesByIdPhoto(int idPhoto){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Consultamos toda la tabla photo_table
        List<CameraSecondary> modelCameraSecondaryList = new ArrayList<>();
        Cursor cursor = db.query(MySqliteHelper.TABLENAME_CAMERASECONDARY,null,
                MySqliteHelper.COLUMN_CAMERASECONDARY_PHOTO_ID + "=?",
                new String[]{String.valueOf(idPhoto)},null,null,null);

        //Agregamos cada elemento del cursor a la lista
        while(cursor.moveToNext()){
            CameraSecondary modelCameraSecondary = new CameraSecondary();
            modelCameraSecondary.setName(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_CAMERASECONDARY_NAME)));
            modelCameraSecondary.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_CAMERASECONDARY_FULL_NAME)));
            modelCameraSecondaryList.add(modelCameraSecondary);
        }

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return  modelCameraSecondaryList;
    }

    public void deleteCamerasSecondariesByIdPhoto(int photoId){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Eliminamos el registro
        db.delete(MySqliteHelper.TABLENAME_CAMERASECONDARY, MySqliteHelper.COLUMN_CAMERASECONDARY_PHOTO_ID + "=?",
                new String[]{String.valueOf(photoId)});

        //Cerramos la conexión
        if(db.isOpen())
            db.close();
    }
}
