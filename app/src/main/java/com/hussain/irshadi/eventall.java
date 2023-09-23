package com.hussain.irshadi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link eventall.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link eventall#newInstance} factory method to
 * create an instance of this fragment.
 */
public class eventall extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView list;

    public eventall() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment eventall.
     */
    // TODO: Rename and change types and number of parameters
    public static eventall newInstance(String param1, String param2) {
        eventall fragment = new eventall();
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
       View v=inflater.inflate(R.layout.fragment_eventall, container, false);
        list=(ListView)v.findViewById(R.id.list);
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("events");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        Query query=ref.orderByChild("Accept").equalTo("1");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

if(getContext()!=null){

                    List<List<String>> lo = new ArrayList<List<String>>();

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
                    eventadaptor eveadap = new eventadaptor(getContext(), lo, 1);
                    list.setAdapter(eveadap);
//                AlertDialog.Builder al=new AlertDialog.Builder(showevent.this);
//                al.setMessage(lo.get(0).get+"");
//                al.setNeutralButton("ok",null);
//                al.create().show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
