package com.hussain.irshadi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


public class questionadap extends ArrayAdapter<List<String>> {
Context con;
    List<List<String>> questiono;

    public questionadap(Context context, List<List<String>> lists) {
        super(context,R.layout.questionlayout, lists);
        con=context;
        questiono=lists;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        LayoutInflater inf=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inf.inflate(R.layout.questionlayout, null,true);
        TextView question=rootView.findViewById(R.id.ques);
        TextView answer=rootView.findViewById(R.id.ans);
        List<String> lo=questiono.get(position);
if(lo.get(1).toString().equals("")){
    answer.setVisibility(View.GONE);
}
        question.setText(lo.get(0));
        answer.setText(lo.get(1));

        return rootView;
    }
}
