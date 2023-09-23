package com.hussain.irshadi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class eventadaptor extends ArrayAdapter<List<String>> {
    Activity act;
    Context con;
    List<List<String>> evento;
    int ja;
    public Button btn;

    public eventadaptor(Context context, List<List<String>> events, int jam) {
        super(context, R.layout.event,events);
        con = context;
        evento = events;
    ja=jam;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inf.inflate(R.layout.event, null,true);


        final CircleImageView profile;
        final TextView usernametxt;
final         ImageView status,imageevent;
      final  LinearLayout profilebtn;
        final TextView date;
        final TextView detail,head;
        profile = (CircleImageView) rootView.findViewById(R.id.profile);
        usernametxt = (TextView) rootView.findViewById(R.id.usernametxt);
        status = (ImageView) rootView.findViewById(R.id.status);
        profilebtn = (LinearLayout) rootView.findViewById(R.id.profilebtn);
        date = (TextView) rootView.findViewById(R.id.date);
        detail = (TextView) rootView.findViewById(R.id.detail);
        btn = (Button) rootView.findViewById(R.id.btn);
        imageevent=(ImageView)rootView.findViewById(R.id.imageevent) ;
        head=(TextView)rootView.findViewById(R.id.head);

        final List<String> eve = evento.get(position);
if(eve.get(2).equals("")){
    imageevent.setVisibility(View.GONE);
}
head.setText(eve.get(1));


        try {
            Picasso.get().load(eve.get(2)).into(imageevent);
        } catch (Exception ex) {
            imageevent.setVisibility(View.GONE);
        }
        date.setText("Date : "+eve.get(5));
        detail.setText(eve.get(4));
        switch (Integer.parseInt(eve.get(6))) {
            case 0:
                status.setImageResource(R.drawable.blue);
                break;

            case 1:
                status.setImageResource(R.drawable.green);
                break;
            case 2:
                status.setImageResource(R.drawable.red);
                break;

        }
        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("uid").equalTo(eve.get(0));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    usernametxt.setText(ds.child("username").getValue().toString());

                    try {
                        Picasso.get().load(ds.child("image").getValue().toString()).into(profile);

                    } catch (Exception ex) {

                        Picasso.get().load(R.drawable.profilepic).into(profile);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






if(ja==0){
    btn.setText("Edit The Post");
status.setVisibility(View.VISIBLE);
}else if(ja==2){
    btn.setText("Delete");
    status.setVisibility(View.GONE);
    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new sqldb(con).deletefav(eve.get(7)+"");
            Toast.makeText(con, "Deleted", Toast.LENGTH_SHORT).show();
            Intent in=new Intent(con,savefav.class);
            con.startActivity(in);

        }
    });
}
else{
    btn.setText("Add To Favourite");
    status.setVisibility(View.GONE);
    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new sqldb(con).insertfav(eve.get(7)+"");
            Toast.makeText(con, "Added To Favourite", Toast.LENGTH_SHORT).show();
        }
    });
}










        return rootView;
    }



}
