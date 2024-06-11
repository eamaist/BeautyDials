package com.example.BeautyDials;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteHelper extends android.database.sqlite.SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2; // увеличен номер версии базы данных
    public static final String DATABASE_NAME = "sample_database";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLiteTable.User.CREATE_TABLE);
        for (int i = 0; i < SQLiteTable.User.FILL_DATA.length; i++) {
            db.execSQL(SQLiteTable.User.FILL_DATA[i]);
        }
        db.execSQL(SQLiteTable.Ads.CREATE_TABLE);
        for (int i = 0; i < SQLiteTable.Ads.FILL_DATA.length; i++) {
            db.execSQL(SQLiteTable.Ads.FILL_DATA[i]);
        }
        db.execSQL(SQLiteTable.Favourites.CREATE_TABLE);
        for (int i = 0; i < SQLiteTable.Favourites.FILL_DATA.length; i++) {
            db.execSQL(SQLiteTable.Favourites.FILL_DATA[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) { // добавлена проверка на номер версии
            db.execSQL("ALTER TABLE \"" + SQLiteTable.User.TABLE_NAME + "\" ADD COLUMN \"" +
                    SQLiteTable.User.COLUMN_USER_LOGIN + "\" TEXT NOT NULL DEFAULT 'temp_login';"); // добавлен код для обновления существующей таблицы
            db.execSQL("UPDATE \"" + SQLiteTable.User.TABLE_NAME + "\" SET \"" +
                    SQLiteTable.User.COLUMN_USER_LOGIN + "\" = SUBSTR(\"" + SQLiteTable.User.COLUMN_PHONE + "\", 1, 10);"); // добавлен код для заполнения нового столбца данными из существующего
        }
        db.execSQL("DROP TABLE IF EXISTS " + SQLiteTable.User.TABLE_NAME);
        onCreate(db);
    }
}




