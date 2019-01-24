package com.rashid.sqliteexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLDataException;

/**
 * Created by Reshka on 18.03.2016.
 */
public class DbCon {

    Context myContext;
    SQLiteDatabase db;
    DbHelper dbHelper;


    public DbCon(Context c) {
        myContext = c;
    }

    public DbCon open() {
        try {
            dbHelper = new DbHelper(myContext);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return this;
        }
    }

    public void close() {
        db.close();
    }

    public void insert(String nome, String nota) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.NOME, nome);
        values.put(dbHelper.NOTA, nota);
        db.insert(dbHelper.TABLE_NAME, null, values);
    }

    public Cursor readAll() {
        String[] columns = new String[]{dbHelper.ID, dbHelper.NOME, dbHelper.NOTA};
        Cursor c = db.query(dbHelper.TABLE_NAME, columns, null, null, null, null, dbHelper.ID + " desc");
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor selected(long id) {
        String[] columns = new String[]{dbHelper.ID, dbHelper.NOME, dbHelper.NOTA};
        Cursor c = db.query(dbHelper.TABLE_NAME, columns, dbHelper.ID + "=" + id, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public void delete(long id) {
        open();
        db.delete(dbHelper.TABLE_NAME, dbHelper.ID + "=" + id, null);
        close();
    }

    public void update(long id, String nome, String nota) {
        open();
        ContentValues values = new ContentValues();
        values.put(dbHelper.NOME, nome);
        values.put(dbHelper.NOTA, nota);
        db.update(dbHelper.TABLE_NAME, values, dbHelper.ID + "=" + id, null);
        close();
    }

    public class DbHelper extends SQLiteOpenHelper {

        public static final String DB_NAME = "lesson.db";
        public static final String TABLE_NAME = "example";
        public static final String ID = "_id";
        public static final String NOME = "NOME";
        public static final String NOTA = "NOTA";
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + NOME + " TEXT , " + NOTA + " TEXT );";
        public static final int VERSION = 1;


        public DbHelper(Context context) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
            onCreate(db);
        }
    }

}
