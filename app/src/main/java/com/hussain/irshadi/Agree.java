package com.hussain.irshadi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Agree extends AppCompatActivity implements View.OnClickListener {

    private EditText phno;
    private Button probtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);
        initView();
    }

    private void initView() {
        phno = (EditText) findViewById(R.id.phno);
        probtn = (Button) findViewById(R.id.probtn);

        probtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.probtn:
if(phno.getText().toString().length()==10){
    AlertDialog.Builder alert=new AlertDialog.Builder(Agree.this);
    alert.setMessage("We Will be Verifying the phone number:\n+92 "+phno.getText().toString()+"\nIs this Ok or would you like to edit it?");
    alert.setNeutralButton("EDIT",null);
    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            new login().phnumber=phno.getText().toString();
            Intent in=new Intent(Agree.this,login.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(in);
        }
    });
alert.create().show();


}else{
    Toast.makeText(this, "Insert a correct Phone Number.", Toast.LENGTH_SHORT).show();
}
                break;
        }
    }

    private void submit() {
        // validate
        String phnoString = phno.getText().toString().trim();
        if (TextUtils.isEmpty(phnoString)) {
            Toast.makeText(this, "Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
