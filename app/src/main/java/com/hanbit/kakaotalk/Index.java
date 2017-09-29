package com.hanbit.kakaotalk;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Index extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        final Context context = Index.this;

        Handler handler = new Handler();
        SQLiteHelper helper = new SQLiteHelper(context); //DB생성

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(context, Main.class));
                finish();
            }
        }, 2000);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public static class  SQLiteHelper extends SQLiteOpenHelper{

        //init()
        public SQLiteHelper(Context context) {
            //null : 기존 팩토리가 제공하는 sQLite3 를 사용하겠다는 것이 아니라 개발자가 만든것을 사용하겠다 => 커스텀마이징
            //hanbit.db 생성함
            super(context, "hanbit.db", null, 1);
            this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //SQLite 문법
            String sql = String.format("CREATE TABLE IF NOT EXISTS "+
                    " %s ( "+
                    "         %s INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "         %s TEXT, %s TEXT, %s TEXT, %s TEXT, "+
                    "         %s TEXT, %s TEXT"+
                    " ); ", Cons.MEM_TBL, Cons.MEM_SEQ, Cons.MEM_PASS, Cons.MEM_NAME, Cons.MEM_EMAIL, Cons.MEM_PHONE, Cons.MEM_PROFIMG, Cons.MEM_ADDR);
            db.execSQL(sql);

            /*for(int i=1; i<6; i++){
                db.execSQL(String.format(" INSERT INTO %s (%s, %s, %s, %s, %s, %s) " +
                                " VALUES ('%s', '%s', '%s', '%s', '%s', '%s'); ",
                                Cons.MEM_TBL, Cons.MEM_PASS, Cons.MEM_NAME, Cons.MEM_EMAIL, Cons.MEM_PHONE, Cons.MEM_PROFIMG, Cons.MEM_ADDR,
                                "1", "홍길동"+i, "hong"+i+"@test.com", "010-1234-567"+i, "default_img", "서울"+i
                ));
            }*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public static abstract class QueryFactory {
        Context context;

        public QueryFactory(Context context) {
            this.context = context;
        }

        public abstract SQLiteDatabase getDatabase();
    }
}
