package com.angel.act9.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private static final String DB_NAME = "company.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creando base de datos...");
        db.execSQL(DBContract.EmployeeEntry.SQL_CREATE_TABLE);
        Log.d(TAG, "Base de datos creada exitosamente");

        // Insertar datos de ejemplo
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Actualizando base de datos de versión " + oldVersion + " a " + newVersion);
        db.execSQL(DBContract.EmployeeEntry.SQL_DELETE_TABLE);
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Insertar empleados de ejemplo
        db.execSQL("INSERT INTO " + DBContract.EmployeeEntry.TABLE_NAME +
                   " (name, position, salary) VALUES ('Juan Pérez', 'Desarrollador', 45000.0)");
        db.execSQL("INSERT INTO " + DBContract.EmployeeEntry.TABLE_NAME +
                   " (name, position, salary) VALUES ('María García', 'Diseñadora', 42000.0)");
        db.execSQL("INSERT INTO " + DBContract.EmployeeEntry.TABLE_NAME +
                   " (name, position, salary) VALUES ('Carlos López', 'Gerente', 55000.0)");
        Log.d(TAG, "Datos de ejemplo insertados");
    }
}
