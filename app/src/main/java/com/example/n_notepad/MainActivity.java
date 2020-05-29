package com.example.n_notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // リストビューのポジションと_idの紐づけをする為のworkフィールド
    List<Integer> memoListId = new ArrayList<>();
    // リストビューに表示する文字列を扱うフィールド
    List<String> memoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ListViewを取得する
        ListView lvMemo = findViewById(R.id.memoList);

        DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            //主キーによる検索SQL文字列の用意。
            String sql = "SELECT * FROM memo WHERE 1 = 1";
            //SQLの実行。
            Cursor cursor = db.rawQuery(sql, null);
            //SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベース内のデータを取得。
            while (cursor.moveToNext()) {
                //カラムのインデックス値を取得。
                int idxId = cursor.getColumnIndex("_id");
                int idxTitle = cursor.getColumnIndex("title");
                //カラムのインデックス値を元に実際のデータを取得。
                memoListId.add((int) cursor.getInt(idxId));
                memoList.add(cursor.getString(idxTitle));
            }
        } finally {
            //データベース接続オブジェクトの解放。
            db.close();
        }

        //アダプタオブジェクトを生成。
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, memoList);
        //リストビューにアダプタオブジェクトを設定。
        lvMemo.setAdapter(adapter);
        //リストビューにリスナを設定。
        lvMemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int _memoId = memoListId.get(position);
                Intent intent = new Intent(MainActivity.this, BrowseActivity.class);
                intent.putExtra("MEMO_ID",_memoId);
                startActivity(intent);
            }
        });
    }

    public void onClick_MainEntry(View view) {
        Intent intent = new Intent(MainActivity.this, EntryActivity.class);
        startActivity(intent);
    }
}
