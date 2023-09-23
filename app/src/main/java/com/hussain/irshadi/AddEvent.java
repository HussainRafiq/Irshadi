package com.hussain.irshadi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class AddEvent extends AppCompatActivity implements View.OnClickListener {

    private Toolbar tool;
    private EditText head;
    private ImageView img;
    private Button pic;
    private Button date;
    Uri selectedImage;
    private Button postbtn;
    private EditText detail;
    String imgo="",imgname="";
    int j=0;
    public  static int  cake=0;
    int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        initView();

        setSupportActionBar(tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
ProgressDialog pg;
    private void initView() {
        tool = (Toolbar) findViewById(R.id.tool);
        head = (EditText) findViewById(R.id.head);
        head.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.img);
        cake=0;
        img.setOnClickListener(this);
        pic = (Button) findViewById(R.id.pic);
        pic.setOnClickListener(this);
        date = (Button) findViewById(R.id.date);
        date.setOnClickListener(this);
        postbtn = (Button) findViewById(R.id.postbtn);
        postbtn.setOnClickListener(this);
        detail = (EditText) findViewById(R.id.detail);
        detail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pic:
                AlertDialog.Builder alert=new AlertDialog.Builder(AddEvent.this);
                alert.setItems(new String[]{"Select Picture", "Removed Picture"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 93);
                                break;
                            case 1:
                                img.setVisibility(View.GONE);
                                pic.setText("Add The Picture");
                                j=0;
                        }



                    }
                });
                alert.create().show();
                break;
            case R.id.date:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.postbtn:
              final  FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                if (!head.getText().toString().equals("") && !detail.getText().toString().equals("") && !date.getText().toString().equals("Add The Date")){
                    pg=new ProgressDialog(AddEvent.this);
                    pg.setMessage("Creating Post..");
                    pg.show();
                    if(j==1){

                        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("eventpic_images/"+user.getUid()+selectedImage.getLastPathSegment());
                        UploadTask uploadTask = riversRef.putFile(selectedImage);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploadsn
                                android.support.v7.app.AlertDialog.Builder ab= new android.support.v7.app.AlertDialog.Builder(AddEvent.this);
                                ab.setMessage(exception.getMessage());
                                ab.setPositiveButton("ok",null);
                                ab.create().show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                                Task<Uri> uritask=taskSnapshot.getStorage().getDownloadUrl();
                                while (!uritask.isSuccessful());
                                Uri down=uritask.getResult();

                                if(uritask.isSuccessful()){
                                    imgo=down.toString();
imgname="eventpic_images/"+user.getUid()+selectedImage.getLastPathSegment();
                                    HashMap<Object, String> ob = new HashMap<>();
                                    ob.put("userid", user.getUid());
                                    ob.put("heading", head.getText().toString());
                                    ob.put("image", imgo);
                                    ob.put("imagename", imgname);
                                    ob.put("detail", detail.getText().toString());
                                    ob.put("date", date.getText().toString());
                                    ob.put("Accept", 0+"");

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    final DatabaseReference ref = database.getReference("events");
                                    Random r=new Random();
                                    String code=r.nextInt(999999)+""+r.nextInt(999999);
                                    ref.child(code).setValue(ob);



                                    pg.dismiss();
cake=1;
                                    Intent in=new Intent(getApplicationContext(),showevent.class);
                                 in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(in);


                                }else {
                                imgo="";
                                    HashMap<Object, String> ob = new HashMap<>();
                                    ob.put("userid", user.getUid());
                                    ob.put("heading", head.getText().toString());
                                    ob.put("image", imgo);
                                    ob.put("imagename", imgname);
                                    ob.put("detail", detail.getText().toString());
                                    ob.put("date", date.getText().toString());
                                    ob.put("Accept", 0+"");

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    final DatabaseReference ref = database.getReference("events");
                                    Random r=new Random();
                                    String code=r.nextInt(999999)+""+r.nextInt(999999);
                                    ref.child(code).setValue(ob);


                                    pg.dismiss();
                                    cake=1;
                                    Intent in=new Intent(getApplicationContext(),showevent.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(in);


                                }

                            }
                        });
                    }else {
                        HashMap<Object,String> ob = new HashMap<>();
                        ob.put("userid", user.getUid());
                        ob.put("heading", head.getText().toString());
                        ob.put("image", "");
                        ob.put("imagename", "");
                        ob.put("detail", detail.getText().toString());
                        ob.put("date", date.getText().toString());
                        ob.put("Accept", 0+"");

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference ref = database.getReference("events");
                        Random r=new Random();
                        String code=r.nextInt(999999)+""+r.nextInt(999999);
                        ref.child(code).setValue(ob);




                        pg.dismiss();
                        cake=1;
                        Intent in=new Intent(getApplicationContext(),showevent.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);



                    }




























                }else{
                    Toast.makeText(this.getApplicationContext(), "Enter All Fields", Toast.LENGTH_SHORT).show();
                }








                break;
        }
    }

    private void submit() {
        // validate
        String headString = head.getText().toString().trim();
        if (TextUtils.isEmpty(headString)) {
            Toast.makeText(this, "Event Heading", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


        String detailString = detail.getText().toString().trim();
        if (TextUtils.isEmpty(detailString)) {
            Toast.makeText(this, "Event Detail(Date,Time,Venue,Phone Number)", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 93 && resultCode == Activity.RESULT_OK)
        {
            selectedImage = data.getData();
            img.setVisibility(View.VISIBLE);
            img.setImageURI(selectedImage);
            pic.setText("Edit Picture");
            j=1;
        }
    }
}
