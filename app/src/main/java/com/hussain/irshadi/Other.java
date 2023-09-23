package com.hussain.irshadi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Other#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Other extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    ListView list;
    private String mParam2;
    ProgressDialog pg;
    private OnFragmentInteractionListener mListener;
    private CircleImageView profile;
    private TextView usernametxt;
    private ListView list_menu;
    private LinearLayout profilebtn;

    public Other() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Other.
     */
    // TODO: Rename and change types and number of parameters
    public static Other newInstance(String param1, String param2) {
        Other fragment = new Other();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pg = new ProgressDialog(getContext());
        pg.setMessage("Loading....");

        pg.show();
        View v = inflater.inflate(R.layout.fragment_other, container, false);
        list = (ListView) v.findViewById(R.id.list_menu);
        String menu[] = {"Events","Favourite Events", "Change The Password", "Credit", "Log Out"};
        ListAdapter li = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, menu);
        list.setAdapter(li);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        AlertDialog.Builder abd=new AlertDialog.Builder(getContext());
                        abd.setTitle("Action");
                        abd.setItems(new String[]{"Add The Event", "Shows Events Added By You"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
switch (which){
    case 0:
        Intent in=new Intent(getContext(),AddEvent.class);
        startActivity(in);
        break;

    case 1:
        Intent ion=new Intent(getContext(),showevent.class);
        startActivity(ion);
        break;
}
                            }
                        });
                        abd.create().show();


                        break;
                    case 1:

                        Intent in=new Intent(getContext(),savefav.class);
                        startActivity(in);
                        break;

                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:
                        FirebaseAuth.getInstance().signOut();
                        Intent into = new Intent(getContext(), Agree.class);
                        startActivity(into);
                        break;

                }
            }
        });

        initView(v);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = ref.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    usernametxt.setText(ds.child("username").getValue().toString());

                    try {
                        if (pg.isShowing()){
                            pg.dismiss();
                    }Picasso.get().load(ds.child("image").getValue().toString()).into(profile);

                    } catch (Exception ex) {

                        if (pg.isShowing()){
                            pg.dismiss();
                        }   Picasso.get().load(R.drawable.profilepic).into(profile);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (pg.isShowing()){
                    pg.dismiss();
                }
            }
        });


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initView(View v) {
        profile = (CircleImageView) v.findViewById(R.id.profile);
        usernametxt = (TextView) v.findViewById(R.id.usernametxt);
        list_menu = (ListView) v.findViewById(R.id.list_menu);
        profilebtn = (LinearLayout) v.findViewById(R.id.profilebtn);
        profilebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profilebtn:
                Intent in = new Intent(getContext(), profile.class);

                startActivity(in);
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
