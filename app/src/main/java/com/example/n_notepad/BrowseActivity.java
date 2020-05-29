package com.example.n_notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BrowseActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvBody;
    int _memoId;

    //データベースから取得した値を格納する変数の用意。データがなかった時のための初期値も用意。
    String _memoTitle = ("");
    String _memoBody = ("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        tvTitle = findViewById(R.id.browseTitle);
        tvBody = findViewById(R.id.browseBody);

        Intent intent = getIntent();
        _memoId = intent.getIntExtra("MEMO_ID",-1);

        //データベースヘルパーオブジェクトを作成。
        DatabaseHelper helper = new DatabaseHelper(BrowseActivity.this);
        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            //主キーによる検索SQL文字列の用意。
            String sql = "SELECT * FROM memo WHERE _id = " + _memoId;
            //SQLの実行。
            Cursor cursor = db.rawQuery(sql, null);
            //SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベース内のデータを取得。
            while (cursor.moveToNext()) {
                //カラムのインデックス値を取得。
                int idxTitle = cursor.getColumnIndex("title");
                int idxBody = cursor.getColumnIndex("body");
                //カラムのインデックス値を元に実際のデータを取得。
                _memoTitle = cursor.getString(idxTitle);
                _memoBody = cursor.getString(idxBody);
            }
        } finally { db.close(); } //データベース接続オブジェクトの解放。

        tvTitle.setText(_memoTitle);
        tvBody.setText(_memoBody);

    }

    // 編集ボタンが押された時の処理
    public void onClick_btnEditStart(View view) {
        Intent intent = new Intent(BrowseActivity.this, EditActivity.class);
        intent.putExtra("MEMO_ID",_memoId);
        intent.putExtra("MEMO_TITLE",_memoTitle);
        intent.putExtra("MEMO_BODY",_memoBody);
        startActivity(intent);
    }

    public void onClick_btnBrowseDel(View view) {
        //データベースヘルパーオブジェクトを作成。
        DatabaseHelper helper = new DatabaseHelper(BrowseActivity.this);
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
            Toast.makeText(BrowseActivity.this, _memoTitle + "を削除しました。" ,Toast.LENGTH_LONG).show();

        } finally { db.close(); } //データベース接続オブジェクトの解放。

        // 一覧画面へ遷移する
        Intent intent = new Intent(BrowseActivity.this, MainActivity.class);
        startActivity(intent);
    }

    // 戻るボタンが押された時の処理
    public void onClick_btnBrowseBack(View view) {
        Intent intent = new Intent(BrowseActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    // 端末の戻るボタンが押された時の処理
    public void onBackPressed(){
        Intent intent = new Intent(BrowseActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
