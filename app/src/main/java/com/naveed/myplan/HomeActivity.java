package com.naveed.myplan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.naveed.myplan.SignupActivity.MY_PREFS_NAME;
import static com.naveed.myplan.SignupActivity.PREF_NAME;
import static com.naveed.myplan.SignupActivity.PREF_PASSWORD;
import static com.naveed.myplan.SignupActivity.PREF_STATUS;
import static com.naveed.myplan.SignupActivity.PREF_USERNAME;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

     String[] values = {"Diet", "Meetings", "Exercise", "Medicines", "Expanses"};

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    MyDatabase db;
    public static Context contextExcer;
    NavigationView navigationView;
    SharedPreferences prefs;
    String prefsUname , prefsPass , prefsName;
    boolean status;
    public static boolean inside  = false;
    MyBeansClass beans ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextExcer=this;
        setupRefs();
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String tit = extras.getString("title");
            String des = extras.getString("desc");

            beans = new MyBeansClass(0 ,tit , des ,"" );
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content_home, new CancelAlarmFragment()).commit();
        }else{
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content_home, callFragment(1)).commit();
        }



        navigationView = findViewById(R.id.nav_view);
        final Menu menu = navigationView.getMenu();
        for (int i = 0; i < values.length; i++) {
            menu.add(values[i]);
        }
        View headerView = navigationView.getHeaderView(0);
        //    ImageView drawerImage = headerView.findViewById(R.id.drawer_image);
        TextView drawername =  headerView.findViewById(R.id.navName);
        TextView drawerusername =  headerView.findViewById(R.id.navUsername);
        //     drawerImage.setImageDrawable(R.drawable.ic_user);
        drawername.setText(prefsName);
        drawerusername.setText(prefsUname);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupRefs() {

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new MyDatabase(this);
        setTitle(values[0]);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        checkUserLogin();
    }

    private void checkUserLogin() {

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        prefsPass = prefs.getString(PREF_PASSWORD, null);
        status = prefs.getBoolean(PREF_STATUS, false);


        if (prefsUname != null && prefsPass != null) {

            if (status){
                prefsName = prefs.getString(PREF_NAME, "No Name");

            }else {

                Log.e("My Plan", "checkUserLogin: In Home");
                Log.e("checkUserLogin: ",Boolean.toString(status));
                startActivity(new Intent(this , MainActivity.class));
            }

        }else if (!status) {
            startActivity(new Intent(this , SignupActivity.class));

        }



    }


    public void changeFragment(Fragment f) {
        // Pop off everything up to and including the current tab
        //this block of code saves fragment to backstack so don't call it if u don't want to save previous fragment to backstack
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentManager.beginTransaction()
                .replace(R.id.content_home, f)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit();
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!status || inside){
                inside = false ;
                super.onBackPressed();
            }else{
                finishAffinity();
            }


        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_log_out) {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(PREF_STATUS , false);
            editor.apply();
            startActivity(new Intent(HomeActivity.this , MainActivity.class));
            Log.e("in Signout :", Boolean.toString(prefs.getBoolean(PREF_STATUS , true)));
            return true;
        }else if (id == R.id.action_add){
            String title = (String) this.getTitle();
            switch (title){
                case "Aim":
                    Toast.makeText(this, "Please Select specific task to add :"+title
                                                            , Toast.LENGTH_SHORT).show();
                    break;
                case "Diet":
                    // alert dialog builder call
                    alertDietFormElements();
                    break;
                case "Meetings":
                    changeFragment(callFragment(2));
                    break;
                case "Exercise":
                    changeFragment(callFragment(3));
                    break;
                case "Medicines":
                    changeFragment(callFragment(4));
                    break;
                case "Expanses":
                    alertExpancesFormElements();
                    break;
                default:
                    Toast.makeText(this, "Please Select specific task to add :"+title
                            , Toast.LENGTH_SHORT).show();
                    break;

            }

        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        String nav_t = (String) item.getTitle();
        drawer.setFocusable(true);

//        Toast.makeText(this, nav_id, Toast.LENGTH_SHORT).show();

        switch (nav_t){
            case "Aim":
                setTitle(nav_t);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_home, callFragment(0)).commit();
                break;
            case "Diet":
                setTitle(nav_t);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_home, callFragment(1)).commit();
                break;
            case "Meetings":
                setTitle(nav_t);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_home, callFragment(7)).commit();
                break;
            case "Exercise":
                setTitle(nav_t);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_home, callFragment(6)).commit();
                break;
            case "Medicines":
                setTitle(nav_t);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_home, callFragment(8)).commit();
                break;
            case "Expanses":
                setTitle(nav_t);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_home, callFragment(5)).commit();
                break;

        }



        return true;
    }


    private Fragment callFragment(int pos){
        Fragment[]fragments = new Fragment[]{
                new AimFragment() , new DietFragment() , new MeetingsFragment() ,
                new ExerciseFragment(), new MedicinesFragment() , new ExpancesFragment() ,
                new ExerciseListFragment() , new MeetingsListFragment() , new MedicinesListFragment()};
        return fragments[pos];
    }


    private void alertDietFormElements() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.form_elements_diet,
                null, false);



        final EditText etDiet = formElementsView
                .findViewById(R.id.formEtDietInDiet);

        final EditText etQty = formElementsView
                .findViewById(R.id.formEtQtyInDiet);

        // the alert dialog
        new AlertDialog.Builder(this).setView(formElementsView)
                .setTitle("Enter Details")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        String diet = etDiet.getText().toString();
                        String qty = etQty.getText().toString();

                        addData(diet , qty ,"0",MyDatabase.TABLE_DIET);
                        Toast.makeText(HomeActivity.this, diet+" : "+qty,
                                Toast.LENGTH_SHORT).show();

                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.content_home, callFragment(1)).commit();
                        dialog.cancel();
                    }

                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        }).show();
    }


    private void alertExpancesFormElements() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.form_elements_expances,
                null, false);



        final EditText etName = formElementsView
                .findViewById(R.id.formEtNameExpances);


        final EditText etPrice = formElementsView
                .findViewById(R.id.formEtPriceExpances);



        // the alert dialog
        new AlertDialog.Builder(this).setView(formElementsView)
                .setTitle("Enter Details")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        String name = etName.getText().toString();
                        String qty = etPrice.getText().toString();

                        addData(name  ,qty , "0" , MyDatabase.TABLE_EXPANCES);
                      //  Toast.makeText(HomeActivity.this, n+" : "+q, Toast.LENGTH_SHORT).show();

                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.content_home, callFragment(5)).commit();
                        dialog.cancel();
                    }

                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        }).show();
    }



    public void addData(String title , String desc , String timeId , String table){

        Log.e( "addData Method : ",title+"  "+desc+"  "+timeId );

        boolean insertData = db.insertData(title , desc , timeId , table);
        if (insertData)
            Toast.makeText(this, "Data inserted Successfully", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();

    }




}
