package com.example.n_notepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * データベースファイル名の定数フィールド。
     */
    private static final String DATABASE_NAME = "n_notepad.db";
    /**
     * バージョン情報の定数フィールド。
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * コンストラクタ。
     */
    public DatabaseHelper(Context context) {
        //親クラスのコンストラクタの呼び出し。
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQLの実行。
        db.execSQL(createTable_memo());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private String createTable_memo() {
        //テーブル作成用SQL文字列の作成。
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE memo (");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("title TEXT,");
        sb.append("body TEXT");
        sb.append(");");
        String sql = sb.toString();

        return sql;
    }

}
