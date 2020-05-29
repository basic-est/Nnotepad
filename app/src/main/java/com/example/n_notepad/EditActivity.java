package com.example.n_notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    EditText titleEt;
    EditText bodyEt;
    String _title;
    String _body;
    int _memoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        _memoId = intent.getIntExtra("MEMO_ID", -1);
        _title = intent.getStringExtra("MEMO_TITLE");
        _body = intent.getStringExtra("MEMO_BODY");

        titleEt = findViewById(R.id.editTitle);
        bodyEt = findViewById(R.id.editBody);

        titleEt.setText(_title);
        bodyEt.setText(_body);
    }

    public void onClick_btnUpdate(View view) {
        _title = titleEt.getText().toString();
        _body = bodyEt.getText().toString();

        //データベースヘルパーオブジェクトを作成。
        DatabaseHelper helper = new DatabaseHelper(EditActivity.this);
        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            //削除用SQL文字列を用意。
            String sqlDelete = "DELETE FROM memo WHERE _id = ?";
            //SQL文字列を元にプリペアドステートメントを取得。
            SQLiteStatement stmt = db.compileStatement(sqlDelete);
            //変数のバイド。
            stmt.bindLong(1, _memoId);
            //削除SQLの実行。
            stmt.executeUpdateDelete();
            //インサート用SQL文字列の用意。
            String sqlInsert = "INSERT INTO memo (title, body) VALUES (?, ?)";
            //SQL文字列を元にプリペアドステートメントを取得。
            stmt = db.compileStatement(sqlInsert);
            //変数のバイド。
            stmt.bindString(1, _title);
            stmt.bindString(2, _body);
            //インサートSQLの実行。
            stmt.executeInsert();

        } finally { db.close(); } //データベース接続オブジェクトの解放。

        Toast.makeText(EditActivity.this, _title + "を保存しました。" ,Toast.LENGTH_LONG).show();

        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        startActivity(intent);

    }

    public void onClick_btnNonUpdate(View view) {
        finish();
    }
}
