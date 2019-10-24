package com.example.aws.blogapp.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aws.blogapp.Fragments.HomeFragment;
import com.example.aws.blogapp.Fragments.ProfileFragment;
import com.example.aws.blogapp.Fragments.CalendarFragment;
import com.example.aws.blogapp.Models.Post;
import com.example.aws.blogapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    Dialog popAddPost ;
    ImageView popupAddBtn;
    TextView popupTitle,popupDescription,popupDescription2;
    ProgressBar popupClickProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ini

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // ini popup
        iniPopup();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        updateNavHeader();


        // set the home fragment as the default one

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();



    }







    private void iniPopup() {

        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        // ini popup widgets
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupDescription2 = popAddPost.findViewById(R.id.popup_description2);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);


        // Add post click Listener

        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                // we need to test all input fields (Title and description ) and post image

                if (!popupTitle.getText().toString().isEmpty()
                    && !popupDescription.getText().toString().isEmpty()
                    && !popupDescription2.getText().toString().isEmpty()) {

                    //everything is okey no empty or null value
                    // TODO Create Post Object and add it to firebase database
                    // first we need to upload post Image
                    // access firebase storage

                    Post post = new Post(popupTitle.getText().toString(),
                            popupDescription.getText().toString(),
                            popupDescription2.getText().toString(),
                            currentUser.getUid());


                    addPost(post);


                    }
                else {
                    showMessage("모든 항목을 입력해주세요") ;
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);

                }



            }
        });



    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        String key = myRef.getKey();
        post.setPostKey(key);



        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("게시물이 등록되었습니다");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
            }
        });





    }


    private void showMessage(String message) {

        Toast.makeText(Home.this,message,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            getSupportActionBar().setTitle("휴가 게시판");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

        } else if (id == R.id.nav_profile) {

            getSupportActionBar().setTitle("프로필");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ProfileFragment()).commit();


        } else if (id == R.id.nav_calendar) {

            getSupportActionBar().setTitle("월력");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new CalendarFragment()).commit();


        } else if (id == R.id.nav_signout) {

            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(loginActivity);
            finish();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);

        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());




    }


}
