package com.hussain.irshadi;

import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class showevent extends AppCompatActivity {

    private Toolbar tool;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showevent);
        initView();
        setSupportActionBar(tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {


        if (new AddEvent().cake == 1) {
            Intent in = new Intent(getApplicationContext(), dashboard.class);


            startActivity(in);

        } else {
            onBackPressed();
        }
        return true;
    }
int i=0;
    private void initView() {
        tool = (Toolbar) findViewById(R.id.tool);
        list = (ListView) findViewById(R.id.list);
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("events");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        Query query=ref.orderByChild("userid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                List<List<String>> lo=new ArrayList<List<String>>();

                for (DataSnapshot ds:dataSnapshot.getChildren()){


                    List<String> even=new ArrayList<String>();

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
eventadaptor eveadap=new eventadaptor(showevent.this,lo,0);
                list.setAdapter(eveadap);
//                AlertDialog.Builder al=new AlertDialog.Builder(showevent.this);
//                al.setMessage(lo.get(0).get+"");
//                al.setNeutralButton("ok",null);
//                al.create().show();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
