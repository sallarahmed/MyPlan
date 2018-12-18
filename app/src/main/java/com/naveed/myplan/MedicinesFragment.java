package com.naveed.myplan;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;


public class MedicinesFragment extends Fragment {

    Context context;
    View mView;
    Button btnStartAlarm , btnStopAlarm ;
    AlarmManager alarmManager;
    private PendingIntent pending_intent;

    private TimePicker alarmTimePicker;
    private TextView alarmTextView;
    private MyReceiver alarm;
    Calendar calendar;
    Intent myIntent;
    MyDatabase myDb;
    String medicineType ="";
    String medicineName = "";






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myDb = new MyDatabase(getActivity());
        if (!HomeActivity.inside) {
            HomeActivity.inside = true;
        }
        mView = inflater.inflate(R.layout.fragment_medicines, container, false);
        context = mView.getContext();



        alarmTextView = mView.findViewById(R.id.tvInMedicines);

        alarmTimePicker = mView.findViewById(R.id.alarmTimePickerMedicines);

        btnStartAlarm = mView.findViewById(R.id.btnSetAlarmInMedicines);
        btnStartAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFormElements();

            }
        });

        btnStopAlarm = mView.findViewById(R.id.btnStopAlarmInMedicines);
        btnStopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Stop Alarm", Toast.LENGTH_SHORT).show();


                myIntent.putExtra("extra", "no");
                context.sendBroadcast(myIntent);

                alarmManager.cancel(pending_intent);
                setAlarmText("Alarm canceled");
            }
        });

        // Get the alarm manager service
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        // set the alarm to the time that you picked
        calendar = Calendar.getInstance();


        return mView;

    }


    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }


    /*
     * Show AlertDialog with some form elements.
     */
    public void alertFormElements() {

        /*
         * Inflate the XML view. activity_main is in
         * res/layout/form_elements_exercise.xmlrcise.xml
         */
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.layout_dialog_edittext,
                null, false);



        final RadioGroup medicinesRadioGroup = formElementsView
                .findViewById(R.id.MedicinesRadioGroup);

        final EditText nameEditText = formElementsView
                .findViewById(R.id.dialogEtMedicine);

        // the alert dialog
        new AlertDialog.Builder(getActivity()).setView(formElementsView)
                .setTitle("Select Medicine")
                .setPositiveButton("Set Alarm", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        /*
                         * Getting the value of selected RadioButton.
                         */
                        // get selected radio button from radioGroup
                        int selectedId = medicinesRadioGroup
                                .getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        RadioButton selectedRadioButton = formElementsView
                                .findViewById(selectedId);
                        if (selectedRadioButton != null) {
                            medicineType = selectedRadioButton.getText().toString();

                        }
                        /*
                         * Getting the value of an EditText.
                         */
                        //  toastString += "Name is: "  + "!\n";
                        medicineName = nameEditText.getText().toString();
                       // Toast.makeText(context, medicineName+" : "+medicineType, Toast.LENGTH_SHORT).show();
                        Log.e( "addData Method ",medicineName+"  "+medicineType);




                        calendar.add(Calendar.SECOND, 3);

                        final int hour;
                        final int minute;
                        final int _id = (int) System.currentTimeMillis();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hour = alarmTimePicker.getHour();
                            minute = alarmTimePicker.getMinute();
                        }else {
                            hour = alarmTimePicker.getCurrentHour();
                            minute = alarmTimePicker.getCurrentMinute();
                        }
                        medicineType =  medicineType +" at:"+hour + " : "+minute;

                        Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);
                        setAlarmText("You clicked a " + hour + " and " + minute);


                        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                        myIntent = new Intent(context, MyReceiver.class);
                        myIntent.putExtra("extra", "yes");
                        myIntent.putExtra("id" , _id);
                        pending_intent = PendingIntent.getBroadcast(context, _id,
                                myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar
                                .getTimeInMillis(), pending_intent);

                        addData(medicineName , medicineType , String.valueOf(_id));
                        // now you should change the set Alarm text so it says something nice


                        setAlarmText("Alarm set to " + hour + ":" + minute);
                        Toast.makeText(context, "You set the alarm",
                                Toast.LENGTH_SHORT).show();


                        dialog.cancel();
                    }

                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        }).show();
    }


    public void addData(String title , String desc , String timeId){

        Log.e( "addData Method : ",title+"  "+desc+"  "+timeId );

        boolean insertData = myDb.insertData(title , desc , timeId , MyDatabase.TABLE_MEDICINE);
        if (insertData)
            MyBeansClass.alertDialog(context ,"Medicines Details" ,"Data Added");
        else
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

    }


}
