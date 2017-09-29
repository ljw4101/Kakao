package com.hanbit.kakaotalk;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final Context context = Login.this;
        final EditText inputId =  (EditText)findViewById(R.id.inputId);
        final EditText inputPwd =  (EditText)findViewById(R.id.inputPwd);
        final MemberLogin login = new MemberLogin(context);

        findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String valId = String.valueOf(inputId.getText());
                final String valPwd = String.valueOf(inputPwd.getText());

                Toast.makeText(context, "입력된 ID: "+valId, Toast.LENGTH_SHORT).show();
                Log.d("입력된 ID: ", valId);
                Log.d("입력된 PW: ", valPwd);

                new Service.iPredicate() {
                    @Override
                    public void execute() {
                        if(login.execute(valId, valPwd)){
                            Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(context, MemberList.class));
                        }else{
                            Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(context, Login.class));
                        };
                    }
                }.execute();

            }
        });

        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputId.setText("");
                inputPwd.setText("");
            }
        });
    }

    private abstract class LoginQuery extends Index.QueryFactory{

        SQLiteOpenHelper helper;

        //override : alt + Enter
        public LoginQuery(Context context) {
            super(context);
            helper = new Index.SQLiteHelper(context);
        }

        // alt + Insert
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    private class MemberLogin extends LoginQuery{

        public MemberLogin(Context context) {
            super(context);
        }

        public boolean execute(String id, String pass){
            return super
                    .getDatabase()
                    .rawQuery(String.format(" select * from %s where %s = '%s' and %s = '%s';", Cons.MEM_TBL, Cons.MEM_SEQ, id, Cons.MEM_PASS, pass),null)
                    .moveToNext();
        }
    }
}
