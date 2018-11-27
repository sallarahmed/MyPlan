package com.naveed.myplan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.naveed.myplan.SignupActivity.MY_PREFS_NAME;
import static com.naveed.myplan.SignupActivity.PREF_PASSWORD;
import static com.naveed.myplan.SignupActivity.PREF_STATUS;
import static com.naveed.myplan.SignupActivity.PREF_USERNAME;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    EditText etUname , etPass;
    String sUname , sPass;
    SharedPreferences prefs;
    String prefsUname , prefsPass;
    public static String TAG = "MyPlan";
    boolean status , prefsStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sign In");

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        prefsUname = prefs.getString(PREF_USERNAME, "null");
        prefsPass = prefs.getString(PREF_PASSWORD, "null");
        prefsStatus = prefs.getBoolean(PREF_STATUS , false);

        setupRefs();
        checkPrefs();




    }

    private void checkPrefs() {



        if (prefsUname != "null" && prefsPass != "null") {

            if (prefsStatus){
                startActivity(new Intent(this , HomeActivity.class));

            }else {
                etUname.setText(prefsUname);
            }

        }else {
            startActivity(new Intent(this , SignupActivity.class));
        }

    }


    private void setupRefs() {
        etUname = findViewById(R.id.et_username_main);
        etPass = findViewById(R.id.et_password_main);

        btnLogin = findViewById(R.id.btnLogin_main);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onClick(View v) {


                sUname = etUname.getText().toString();
                sPass = etPass.getText().toString();

                if (prefsUname != null && prefsPass != null) {
                    Log.e(TAG, "if Uname: " +sUname);
                    Log.e(TAG, "if pass:"+sPass );
                    if (sUname.equals(prefsUname) && sPass.equals(prefsPass) ){
                        Log.e(TAG, "nested if: etpass"+ sPass );
                        Log.e(TAG, "nested if : prefs pass :"+ prefsPass);
                        //redirect to home
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean(PREF_STATUS , true);
                        editor.apply();
                        startActivity(new Intent(MainActivity.this , HomeActivity.class));
                    } else{
                        Toast.makeText(MainActivity.this, "UserName and Password don't match",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    startActivity(new Intent(MainActivity.this
                                                            , SignupActivity.class));
                }

            }
        });
    }
}
