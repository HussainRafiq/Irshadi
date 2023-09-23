package com.hussain.irshadi;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity implements View.OnClickListener {

    private Toolbar head;
    int j=0;

    ProgressDialog pg;
    private CircleImageView profile;
    private FloatingActionButton fab;
    private EditText username;
    private Button proceed;
    String img="",limg="";

    Uri selectedImage;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        head = (Toolbar) findViewById(R.id.head);
        profile = (CircleImageView) findViewById(R.id.profile);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        username = (EditText) findViewById(R.id.username);
        proceed = (Button) findViewById(R.id.proceed);
pg=new ProgressDialog(profile.this);
        pg.setMessage("Getting Information");
        pg.create();
        pg.show();

        fab.setOnClickListener(this);
        proceed.setOnClickListener(this);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("users");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        Query query=ref.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    username.setText(ds.child("username").getValue().toString());
                  limg=ds.child("imagename").getValue().toString();
                    img=ds.child("image").getValue().toString();

                    try {
                        Picasso.get().load(ds.child("image").getValue().toString()).into(profile);
                        pg.dismiss();
                    }catch (Exception ex){

                        Picasso.get().load(R.drawable.profilepic).into(profile);
if(pg.isShowing()) {
    pg.dismiss();
}
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pg.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                AlertDialog.Builder alert=new AlertDialog.Builder(profile.this);
                alert.setItems(new String[]{"Capture Picture", "Upload From Gallery","Remove Profile Picture"}, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which==0){
    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                // Create the File where the photo should go
                                File photoFile = null;
                                try {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        photoFile = createImageFile();
                                    }
                                } catch (IOException ex) {
                                    // Error occurred while creating the File
                                }
                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    selectedImage = FileProvider.getUriForFile(profile.this,
                                            "com.example.android.fileprovider",
                                            photoFile);
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                                    startActivityForResult(cameraIntent, 92);
                                }
                            }

}else if(which==1){
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 93);
                        }else if(which==2){
                            profile.setImageResource(R.drawable.profilepic);
                        j=0;
                            if(!limg.equals("")){
                                // Create a storage reference from our app
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();

// Create a reference to the file to delete
                                StorageReference desertRef = storageRef.child(limg);

// Delete the file
                                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // File deleted successfully
                             img="";
                             limg="";
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Uh-oh, an error occurred!
                                    }
                                });

                            }


                        }
                    }
                });
                alert.create().show();








                break;
            case R.id.proceed:
if(!username.getText().toString().equals("")){
  pg.setMessage("Proceeding...");
  pg.create();
  pg.show();
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    final                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    final                      DatabaseReference ref=database.getReference("users");
    if(j==1){
if(!limg.equals("")){
    // Create a storage reference from our app
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

// Create a reference to the file to delete
    StorageReference desertRef = storageRef.child(limg);

// Delete the file
    desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            // File deleted successfully
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // Uh-oh, an error occurred!
        }
    });

}

        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("profilepic_images/"+user.getUid()+selectedImage.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(selectedImage);






// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploadsn
              AlertDialog.Builder ab= new AlertDialog.Builder(profile.this);
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
                    img=down.toString();

                    HashMap<String,Object> ob=new HashMap<>();
                    ob.put("username",username.getText().toString());
                    ob.put("image",img);
                    limg="profilepic_images/"+user.getUid()+selectedImage.getLastPathSegment();
                    ob.put("imagename",limg);
                    ref.child(user.getUid()).updateChildren(ob);
                    pg.dismiss();
                    j=0;

                    Intent in=new Intent(getApplicationContext(),dashboard.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                }else {
                    HashMap<String,Object> ob=new HashMap<>();
                    ob.put("username",username.getText().toString());
                    ob.put("image",img);
                    ref.child(user.getUid()).updateChildren(ob);
                    pg.dismiss();
                    j=0;

                    Intent in=new Intent(getApplicationContext(),dashboard.class);

                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                }

            }
        });

    }else{
        HashMap<String,Object> ob=new HashMap<>();
        ob.put("username",username.getText().toString());
        ob.put("image",img);
        ob.put("imagename",limg);

        ref.child(user.getUid()).updateChildren(ob);
        pg.dismiss();

        Intent in=new Intent(getApplicationContext(),dashboard.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
    }












    //ref.child(user.getUid()).updateChildren()

}
                break;
        }
    }

    private void submit() {
        // validate
        String usernameString = username.getText().toString().trim();
        if (TextUtils.isEmpty(usernameString)) {
            Toast.makeText(this, "Your Username", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 786)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 92);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 92 && resultCode == Activity.RESULT_OK)
        {
            if(resultCode != RESULT_CANCELED){
                if (requestCode == 92) {
                    try {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        profile.setImageBitmap(photo);
                    }catch (Exception ex){

                        profile.setImageURI(selectedImage);

                    }
                    }
            }            // CALL THIS METHOD TO GET THE ACTUAL PATH
        j=1;
        }

        else if (requestCode == 93 && resultCode == Activity.RESULT_OK)
        {
            selectedImage = data.getData();
            profile.setImageURI(selectedImage);
        j=1;
        }
    }
    String currentPhotoPath;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        }
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
