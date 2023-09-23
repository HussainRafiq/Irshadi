package com.hussain.irshadi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link sharequestion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sharequestion extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private CircleImageView profile;
    private TextView usernametxt;
    private LinearLayout profilebtn;
    private Button postbtn;
    private EditText question;
    ProgressDialog pg;
    private ListView li;

    public sharequestion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sharequestion.
     */
    // TODO: Rename and change types and number of parameters
    public static sharequestion newInstance(String param1, String param2) {
        sharequestion fragment = new sharequestion();
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
        View v = inflater.inflate(R.layout.fragment_sharequestion, container, false);
initView(v);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Question");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query query=ref.orderByChild("Uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

if(getContext()!=null) {
    List<List<String>> lk = new ArrayList<List<String>>();
    for (DataSnapshot ds : dataSnapshot.getChildren()) {
        List<String> kl = new ArrayList<String>();
        kl.add(ds.child("question").getValue().toString());
        kl.add(ds.child("reply").getValue().toString());

        lk.add(kl);
    }
    Collections.reverse(lk);
    questionadap sd = new questionadap(getContext(), lk);
    li.setAdapter(sd);


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

    private void initView(View v) {
        profile = (CircleImageView) v.findViewById(R.id.profile);
        usernametxt = (TextView) v.findViewById(R.id.usernametxt);
        profilebtn = (LinearLayout) v.findViewById(R.id.profilebtn);
        pg = new ProgressDialog(getContext());

        profilebtn.setOnClickListener(this);
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
        postbtn = (Button) v.findViewById(R.id.postbtn);
        postbtn.setOnClickListener(this);
        question = (EditText) v.findViewById(R.id.question);
        question.setOnClickListener(this);
        li = (ListView) v.findViewById(R.id.li);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profilebtn:

                break;
            case R.id.postbtn:
                if (question.getText().length() > 15) {
                    pg.setMessage("Posting....");
                    pg.show();

                    Map<Object, String> ob = new HashMap<Object, String>();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    ob.put("Uid", user.getUid());
                    ob.put("question", question.getText().toString());
                    ob.put("reply", "");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("Question");
                    ref.push().setValue(ob);


                    question.setText("");

                    pg.dismiss();


                } else {
                    Toast.makeText(getContext(), "Question Should maximum 15 letter", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    private void submit() {
        // validate
        String questionString = question.getText().toString().trim();
        if (TextUtils.isEmpty(questionString)) {
            Toast.makeText(getContext(), "What's Your Question", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


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
