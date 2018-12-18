package com.naveed.myplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    Button btnSinup;
    EditText etName , etUname , etPass , etcPass;
    public static String MY_PREFS_NAME = "myprefs";
    public static String PREF_NAME = "name";
    public static String PREF_USERNAME = "uName";
    public static String PREF_PASSWORD = "pass";
    public static String PREF_STATUS = "login_status";
    String s_name , s_uname , s_pass ,s_c_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");
        etName = findViewById(R.id.et_name_signup);
        etUname = findViewById(R.id.et_username_signup);
        etPass = findViewById(R.id.et_password_signup);
        etcPass = findViewById(R.id.et_c_password_signup);



       // checkUserLogedin();

        btnSinup = findViewById(R.id.btnSignup_signup);
        btnSinup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s_name = etName.getText().toString();
                s_uname = etUname.getText().toString();
                s_pass = etPass.getText().toString();
                s_c_pass = etcPass.getText().toString();
                if (!s_name.equals("") &&!s_uname.equals("") &&!s_pass.equals("") ){
                  if (s_pass.equals(s_c_pass)){
                        setPrefs(s_name , s_uname , s_pass);
                        startActivity(new Intent(SignupActivity.this , MainActivity.class));
                    }else{
                        Toast.makeText(SignupActivity.this, "Password don't match",
                                                                    Toast.LENGTH_SHORT).show();
                   }
                }


            }
        });


    }



    private void setPrefs(String name , String uname , String pass){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(PREF_NAME, name);
        editor.putString(PREF_USERNAME, uname);
        editor.putString(PREF_PASSWORD, pass);
        editor.putBoolean(PREF_STATUS , false);
        editor.apply();
    }



}
