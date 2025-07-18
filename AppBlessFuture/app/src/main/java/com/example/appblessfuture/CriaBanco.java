package com.example.appblessfuture;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CriaBanco extends SQLiteOpenHelper {
    private static final String NOME_DB = "BlessFuture.db";
    private static final int VERSAO = 1;
    public CriaBanco(Context context){
        super(context, NOME_DB, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "codigo INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT,"
                + "user TEXT,"
                + "setor TEXT,"
                + "celular TEXT,"
                + "email TEXT UNIQUE,"
                + "senha TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }
}