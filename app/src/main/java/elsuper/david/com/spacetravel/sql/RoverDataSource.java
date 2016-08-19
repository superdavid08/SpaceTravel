package elsuper.david.com.spacetravel.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import elsuper.david.com.spacetravel.model.Rover;

/**
 * Created by Andrés David García Gómez.
 */
public class RoverDataSource {
    //Sacamos a db del constructor para poder abrir y cerrar la conexión en cada método utilizado
    private SQLiteDatabase db = null;
    private MySqliteHelper helper;

    public RoverDataSource(Context context) {
        helper = new MySqliteHelper(context);
    }

    public long saveRover(Rover modelRover, int photoId){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Preparamos el modelo a guardar
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySqliteHelper.COLUMN_ROVER_PHOTO_ID, photoId);
        contentValues.put(MySqliteHelper.COLUMN_ROVER_ID, modelRover.getId());
        contentValues.put(MySqliteHelper.COLUMN_ROVER_NAME, modelRover.getName());
        contentValues.put(MySqliteHelper.COLUMN_ROVER_LANDING_DATE, modelRover.getLandingDate());
        contentValues.put(MySqliteHelper.COLUMN_ROVER_MAX_SOL, modelRover.getMaxSol());
        contentValues.put(MySqliteHelper.COLUMN_ROVER_MAX_DATE, modelRover.getMaxDate());
        contentValues.put(MySqliteHelper.COLUMN_ROVER_TOTAL_PHOTOS, modelRover.getTotalPhotos());

        //Insertamos el registro
        long result = db.insert(MySqliteHelper.TABLENAME_ROVER, null, contentValues);

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return result;
    }

    public List<Rover> getAllRoversByIdPhoto(int photoId){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Consultamos toda la tabla photo_table
        List<Rover> modelRoverList = new ArrayList<>();
        Cursor cursor = db.query(MySqliteHelper.TABLENAME_ROVER,null,MySqliteHelper.COLUMN_ROVER_PHOTO_ID + "=?",
                new String[]{String.valueOf(photoId)},null,null,null);

        //Agregamos cada elemento del cursor a la lista
        while(cursor.moveToNext()){
            Rover modelRover = new Rover();
            modelRover.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ROVER_ID)));
            modelRover.setName(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ROVER_NAME)));
            modelRover.setLandingDate(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ROVER_LANDING_DATE)));
            modelRover.setMaxSol(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ROVER_MAX_SOL)));
            modelRover.setMaxDate(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ROVER_MAX_DATE)));
            modelRover.setTotalPhotos(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ROVER_TOTAL_PHOTOS)));
            modelRoverList.add(modelRover);
        }

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return  modelRoverList;
    }

    public Rover getRover(int idPhoto){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Objeto a devolver
        Rover modelRover = new Rover();

        //Consultamos
        Cursor cursor = db.query(MySqliteHelper.TABLENAME_ROVER, null, MySqliteHelper.COLUMN_ROVER_PHOTO_ID + "=?",
                new String[]{String.valueOf(idPhoto)},null,null,null);

        //Obtenemos el primer registro del cursor
        if(cursor.moveToFirst()){
            modelRover.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ROVER_ID)));
            modelRover.setName(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ROVER_NAME)));
            modelRover.setLandingDate(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ROVER_LANDING_DATE)));
            modelRover.setMaxSol(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ROVER_MAX_SOL)));
            modelRover.setMaxDate(cursor.getString(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ROVER_MAX_DATE)));
            modelRover.setTotalPhotos(cursor.getInt(cursor.getColumnIndexOrThrow(MySqliteHelper.COLUMN_ROVER_TOTAL_PHOTOS)));
        }
        else
            modelRover = null;

        //Cerramos la conexión
        if(db.isOpen())
            db.close();

        return modelRover;
    }

    public void deleteRoversByIdPhoto(int photoId){
        //Abrimos la conexión
        db = helper.getWritableDatabase();

        //Eliminamos el registro
        db.delete(MySqliteHelper.TABLENAME_ROVER, MySqliteHelper.COLUMN_CAMERA_PHOTO_ID + "=?",
                new String[]{String.valueOf(photoId)});

        //Cerramos la conexión
        if(db.isOpen())
            db.close();
    }
}
