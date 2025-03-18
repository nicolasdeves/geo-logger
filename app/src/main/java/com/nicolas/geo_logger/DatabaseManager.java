package com.nicolas.geo_logger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    private String createTableSQL = null;
    private String dropTableSQL = null;

    public DatabaseManager(Context context, String dataBaseName, int version, String createTableSQL, String dropTableSQL) {
        super(context, dataBaseName, null, version);

        this.createTableSQL = createTableSQL;
        this.dropTableSQL = dropTableSQL;
    }

    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTableSQL);
        onCreate(db);
    }
}

