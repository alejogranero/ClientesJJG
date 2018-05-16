package com.grafflersys.alejo.clientes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {


    SQLiteHelper(Context context, String name,
                 SQLiteDatabase.CursorFactory factory,
                 int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String name, String apellido, String DNI, String domicilio, String telefono, byte[] image) {
        SQLiteDatabase database = getWritableDatabase();
        String SQL = "INSERT INTO CLIENTES VALUES (NULL, ?, ?, ?, ?, ?,?)";

        SQLiteStatement statement = database.compileStatement(SQL);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, apellido);
        statement.bindString(3, DNI);
        statement.bindString(4, domicilio);
        statement.bindString(5, telefono);
        statement.bindBlob(6, image);

        statement.executeInsert();
    }

    public void updateData(String name, String apellido, String DNI, String domicilio, String telefono, byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE CLIENTES SET name =?, apellido =?, DNI=? , domicilio =?, telefono= ?, image=? WHERE id =?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1, name);
        statement.bindString(2, apellido);
        statement.bindString(3, DNI);
        statement.bindString(4, domicilio);
        statement.bindString(5, telefono);
        statement.bindBlob(6, image);
        statement.bindDouble(7, (double) id);

        statement.execute();
        database.close();
    }

    public void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM CLIENTES WHERE id=?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
