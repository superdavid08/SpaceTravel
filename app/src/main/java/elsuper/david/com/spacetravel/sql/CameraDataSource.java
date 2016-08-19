package elsuper.david.com.spacetravel.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import elsuper.david.com.spacetravel.model.Camera;

/**
 * Created by Andrés David García Gómez.
 */
public class CameraDataSource {
    //Sacamos a db del constructor para poder abrir y cerrar la conexión en cada método utilizado
    private SQLiteDatabase db = null;
    private MySqliteHelper helper;

    public CameraDataSource(Context context) {
        helper = new MySqliteHelper(context);
    }

    public long saveCamera(Camera modelCamera, int photoId){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Preparamos el modelo a guardar
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySqliteHelper.COLUMN_CAMERA_PHOTO_ID, photoId);
        contentValues.put(MySqliteHelper.COLUMN_CAMERA_ID, modelCamera.getId());
        contentValues.put(MySqliteHelper.COLUMN_CAMERA_NAME, modelCamera.getName());
        contentValues.put(MySqliteHelper.COLUMN_CAMERA_ROVER_ID, modelCamera.getRoverId());
        contentValues.put(MySqliteHelper.COLUMN_CAMERA_FULL_NAME, modelCamera.getFullName());

        //Insertamos el registro
        long result = db.insert(MySqliteHelper.TABLENAME_CAMERA, null, contentValues);

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return result;
    }

    public List<Camera> getAllCamerasByIdPhoto(int photoId){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Consultamos toda la tabla photo_table
        List<Camera> modelCameraList = new ArrayList<>();
        Cursor cursor = db.query(MySqliteHelper.TABLENAME_CAMERA,null,MySqliteHelper.COLUMN_CAMERA_PHOTO_ID + "=?",
                new String[]{String.valueOf(photoId)},null,null,null);

        //Agregamos cada elemento del cursor a la lista
        while(cursor.moveToNext()){
            Camera modelCamera = new Camera();
            modelCamera.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_CAMERA_ID)));
            modelCamera.setName(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_CAMERA_NAME)));
            modelCamera.setRoverId(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_CAMERA_ROVER_ID)));
            modelCamera.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_CAMERA_FULL_NAME)));
            modelCameraList.add(modelCamera);
        }

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return  modelCameraList;
    }

    public Camera getCamera(int idPhoto){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Objeto a devolver
        Camera modelCamera = new Camera();

        //Consultamos
        Cursor cursor = db.query(MySqliteHelper.TABLENAME_CAMERA, null, MySqliteHelper.COLUMN_CAMERA_PHOTO_ID + "=?",
                new String[]{String.valueOf(idPhoto)},null,null,null);

        //Obtenemos el primer registro del cursor
        if(cursor.moveToFirst()){
            modelCamera.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_CAMERA_ID)));
            modelCamera.setName(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_CAMERA_NAME)));
            modelCamera.setRoverId(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_CAMERA_ROVER_ID)));
            modelCamera.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_CAMERA_FULL_NAME)));
        }
        else
            modelCamera = null;

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return modelCamera;
    }

    public void deleteCamerasByIdPhoto(int photoId){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Eliminamos el registro
        db.delete(MySqliteHelper.TABLENAME_CAMERA, MySqliteHelper.COLUMN_CAMERA_PHOTO_ID + "=?",
                new String[]{String.valueOf(photoId)});

        //Cerramos la conexión
        if(db.isOpen())
            db.close();
    }
}
