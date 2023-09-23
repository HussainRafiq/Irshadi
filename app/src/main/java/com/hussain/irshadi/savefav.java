package com.hussain.irshadi;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class savefav extends AppCompatActivity {

    private Toolbar tool;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savefav);
        initView();
        setSupportActionBar(tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Cursor cursor=new sqldb(savefav.this).getfav();

        final List<List<String>> lo=new ArrayList<List<String>>();

        if (cursor.moveToFirst()){
            do{
                String datas = cursor.getString(cursor.getColumnIndex("postid"));
                // do what ever you want here
                final FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference("events");
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                Query query=ref.orderByKey().equalTo(datas);
                query.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {


                                                        List<String> even = new ArrayList<String>();

                                                        even.add(ds.child("userid").getValue().toString());
                                                        even.add(ds.child("heading").getValue().toString());
                                                        even.add(ds.child("image").getValue().toString());
                                                        even.add(ds.child("imagename").getValue().toString());
                                                        even.add(ds.child("detail").getValue().toString());
                                                        even.add(ds.child("date").getValue().toString());
                                                        even.add(ds.child("Accept").getValue().toString());
                                                        even.add(ds.getKey().toString());

                                                        lo.add(even);
                                                    }

                                                    Collections.reverse(lo);
                                                    eventadaptor eveadap = new eventadaptor(savefav.this, lo, 2);
                                                    list.setAdapter(eveadap);
                                                }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }while(cursor.moveToNext());
        }
        cursor.close();

















    }

    @Override
    public boolean onSupportNavigateUp() {


        onBackPressed();
        return true;

    }

    private void initView() {
        tool = (Toolbar) findViewById(R.id.tool);
        list = (ListView) findViewById(R.id.list);
            }
}
