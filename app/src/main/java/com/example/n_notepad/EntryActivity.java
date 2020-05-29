package com.example.n_notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EntryActivity extends AppCompatActivity {

    EditText titleEt;
    EditText bodyEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        titleEt = findViewById(R.id.editTitle);
        bodyEt = findViewById(R.id.editBody);

    }

    public void onClick_btnSave(View view) {
        String _title = titleEt.getText().toString();
        String _body = bodyEt.getText().toString();

        //データベースヘルパーオブジェクトを作成。
        DatabaseHelper helper = new DatabaseHelper(EntryActivity.this);
        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            //インサート用SQL文字列の用意。
            String sqlInsert = "INSERT INTO memo (title, body) VALUES (?, ?)";
            //SQL文字列を元にプリペアドステートメントを取得。
            SQLiteStatement stmt = db.compileStatement(sqlInsert);
            //変数のバイド。
            stmt.bindString(1, _title);
            stmt.bindString(2, _body);
            //インサートSQLの実行。
            stmt.executeInsert();

        } finally { db.close(); } //データベース接続オブジェクトの解放。

        Toast.makeText(EntryActivity.this, _title + "を新規登録しました。" ,Toast.LENGTH_LONG).show();

        Intent intent = new Intent(EntryActivity.this, MainActivity.class);
        startActivity(intent);

    }

    public void onClick_btnCansel(View view) { finish(); }
}
