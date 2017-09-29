package com.hanbit.kakaotalk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        final Context context = MemberList.this;

        ListView listView = (ListView) findViewById(R.id.listView);
        final FriendList friendList = new FriendList(context);
        ArrayList<Member> friends = (ArrayList<Member>)new Service.IList() {
            @Override
            public ArrayList<?> excute(Objects o) {
                return friendList.execute();
            }
        }.excute(null);

        listView.setAdapter(new MemberAdapter(context, friends));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        findViewById(R.id.goAddBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MemberAdd.class));
            }
        });

        findViewById(R.id.goDetailBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MemberDetail.class));
            }
        });

        findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Login.class));
            }
        });
    }

    private abstract class ListQuery extends Index.QueryFactory{

        SQLiteOpenHelper helper;

        //override : alt + Enter
        public ListQuery(Context context) {
            super(context);
            helper = new Index.SQLiteHelper(context);
        }

        // alt + Insert
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class FriendList extends ListQuery{

        public FriendList(Context context) {
            super(context);
        }

        public ArrayList<Member> execute(){
            ArrayList<Member> list = new ArrayList<>();
            String sql = String.format(" select * from %s; ", Cons.MEM_TBL);

            //Cursor : ResultSet
            Cursor cursor = super.getDatabase().rawQuery(sql,null);
            Member member = null;
            if(cursor != null){
                //첫번쨰 data도 넣기 위함
                if(cursor.moveToFirst()){
                    do{
                        member = new Member();
                        member.setSeq(cursor.getString(cursor.getColumnIndex(Cons.MEM_SEQ)));
                        member.setPass(cursor.getString(cursor.getColumnIndex(Cons.MEM_PASS)));
                        member.setName(cursor.getString(cursor.getColumnIndex(Cons.MEM_NAME)));
                        member.setEmail(cursor.getString(cursor.getColumnIndex(Cons.MEM_EMAIL)));
                        member.setPhone(cursor.getString(cursor.getColumnIndex(Cons.MEM_PHONE)));
                        member.setProfileimage(cursor.getString(cursor.getColumnIndex(Cons.MEM_PROFIMG)));
                        member.setAddr(cursor.getString(cursor.getColumnIndex(Cons.MEM_ADDR)));
                        list.add(member);
                    }while (cursor.moveToNext());
                }
            }

            return list;
        }
    }
    class MemberAdapter extends BaseAdapter{
        ArrayList<Member> list;
        LayoutInflater inflater;

        public MemberAdapter(Context context, ArrayList<Member>list) {
            this.list = list;
            this.inflater = LayoutInflater.from(context);
        }
        private int[] photos={
                R.drawable.cupcake,
                R.drawable.donut,
                R.drawable.eclair,
                R.drawable.froyo,
                R.drawable.gingerbread
        };

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder viewHolder;
            if(v == null){
                v = inflater.inflate(R.layout.member_adapter, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) v.findViewById(R.id.imgView);
                viewHolder.name = (TextView) v.findViewById(R.id.name);
                viewHolder.phone = (TextView) v.findViewById(R.id.phone);
                v.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) v.getTag();
            }
            viewHolder.imageView.setImageResource(photos[i]);
            viewHolder.name.setText(list.get(i).getName());
            viewHolder.phone.setText(list.get(i).getPhone());
            return v;
        }
    }
    static class ViewHolder{
        ImageView imageView;
        TextView name;
        TextView phone;
    }
}
