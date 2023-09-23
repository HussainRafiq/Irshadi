package com.hussain.irshadi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class dashboard extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        page.setVisibility(View.VISIBLE);

            switch (item.getItemId()) {
                case R.id.upevent:
page.setText("Upcoming Event");

                    getSupportFragmentManager().beginTransaction().replace(R.id.content,new eventall()).commit();

                    return true;
                case R.id.yourques:

                    page.setText("Your Question");

                  getSupportFragmentManager().beginTransaction().replace(R.id.content,new sharequestion()).commit();

                    return true;
                case R.id.chat:

                    page.setText("Chat");

                    return true;

                case R.id.other:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,new Other()).commit();

                    page.setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };
    private Toolbar act;
    private BottomNavigationView nav_view;
    private RelativeLayout container;
    private TextView page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initView();
        setSupportActionBar(act);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent in = new Intent(getApplicationContext(), Agree.class);
            startActivity(in);
        }
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.content,new eventall()).commit();

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        return true;
    }

    private void initView() {
        act = (Toolbar) findViewById(R.id.act);
        nav_view = (BottomNavigationView) findViewById(R.id.nav_view);
        container = (RelativeLayout) findViewById(R.id.container);
        page = (TextView) findViewById(R.id.page);
        page.setText("Upcoming Event");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                Intent in = new Intent(getApplicationContext(), Agree.class);
                startActivity(in);
                break;
            }
        return true;
    }
}

