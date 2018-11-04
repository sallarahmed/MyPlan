package com.naveed.myplan;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    MyDatabase db;
    Button btnSinup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");
        db = new MyDatabase(this);
        checkUserLogedin();

        btnSinup = findViewById(R.id.btnSignup);
        btnSinup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData("salar" , "sallar" , "passw");

            }
        });






    }





    private void addData(String name , String uname , String pass){

        /*s_name = etName.getText().toString();
        s_surname = etSurname.getText().toString();
        s_marks = etMarks.getText().toString();*/

        boolean isInserted = db.insertData(name , uname , pass);
        if (isInserted){
            Toast.makeText(SignupActivity.this,
                    "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(SignupActivity.this,
                    "Data Insertion Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserLogedin() {
        boolean status = db.doesTableExist();
        if (!status) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }


}
