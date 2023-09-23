package com.hussain.irshadi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity implements View.OnClickListener {
    public String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    ProgressDialog pg;
    static public String phnumber = "";
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth auth;
    private EditText code;
    private TextView head;
    private TextView disc;
    int sec = 60;
    private Button btn;
    Handler hand;
    Runnable r;
    int n=0;
    private TextView link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        disabled();
        hand = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                sec--;
                if (sec < 10) {
                    btn.setText("Resend SMS 00:0" + sec);
                } else {
                    btn.setText("Resend SMS 00:" + sec);
                }
                if (sec == 0) {

                    btn.setText("Resend SMS");
                    sec = 60;
                    enabled();
                } else {
                    disabled();
                    hand.postDelayed(r, 1000);
                }
            }
        };
        hand.postDelayed(r, 1000);

        pg = new ProgressDialog(login.this);
        head.setText("Verify +92" + phnumber);
        disc.setText("Waiting to automatically detect an SMS sent to +92" + phnumber);

        auth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                code.setText(credential.getSmsCode());
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                AlertDialog.Builder alert = new AlertDialog.Builder(login.this);
                alert.setTitle("Error");
                alert.setMessage("" + e.getMessage());
                alert.setPositiveButton("OK", null);
                alert.create().show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


                // ...
            }
        };

        if (!phnumber.equals("")) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+92" + phnumber,        // Phone number to verify
                    55,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks


        }
        code.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (code.getText().toString().length() == 6) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code.getText().toString());
                    signInWithPhoneAuthCredential(credential);

                }


                return false;
            }
        });


    }

    @SuppressLint("ResourceAsColor")
    public void enabled() {

        btn.setEnabled(true);

    }

    @SuppressLint("ResourceAsColor")
    public void disabled() {
        btn.setEnabled(false);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        pg.setMessage("Sign In....");
        pg.show();
        auth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

final                            FirebaseUser user = task.getResult().getUser();

                            FirebaseDatabase database=FirebaseDatabase.getInstance();
      final                      DatabaseReference ref=database.getReference("users");
                            Query query=ref.orderByChild("uid").equalTo(user.getUid());
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue()==null){
                                       if(getApplicationContext()!=null) {
                                           HashMap<Object, String> ob = new HashMap<>();
                                           ob.put("uid", user.getUid());
                                           ob.put("username", "");
                                           ob.put("image", "");
                                           ob.put("imagename", "");

                                           ref.child(user.getUid()).setValue(ob);

                                       }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });   pg.dismiss();
if(n==0) {
    Intent in = new Intent(login.this, profile.class);
    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(in);
n=1;
}
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            pg.dismiss();
                            Toast.makeText(getApplicationContext(), "Sign In Authentication Failed", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }

                       }
                });

    }

    private void initView() {
        code = (EditText) findViewById(R.id.code);
        head = (TextView) findViewById(R.id.head);
        disc = (TextView) findViewById(R.id.disc);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
        link = (TextView) findViewById(R.id.link);
        link.setOnClickListener(this);
    }

    private void submit() {
        // validate
        String codeString = code.getText().toString().trim();
        if (TextUtils.isEmpty(codeString)) {
            Toast.makeText(this, "6 Digit Code", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                if (!phnumber.equals("")) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+92" + phnumber,        // Phone number to verify
                            55,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks

                    hand.postDelayed(r, 1000);
                    disabled();
                }

                break;
            case R.id.link:
onBackPressed();
                break;
        }
    }
}
