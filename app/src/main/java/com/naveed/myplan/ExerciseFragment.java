package com.naveed.myplan;

import android.annotation.TargetApi;
import android.app.AlarmManager;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;



public class ExerciseFragment extends Fragment {

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

    String exerciseType ="";
    String exerciseDiet = "";






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_exercise, container, false);
        context = mView.getContext();



        alarmTextView = mView.findViewById(R.id.tvInExercise);

        alarmTimePicker = mView.findViewById(R.id.alarmTimePickerExercise);

        btnStartAlarm = mView.findViewById(R.id.btnSetAlarmInExercise);
        btnStartAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


          //  showRadioButtonDialog();
            alertFormElements();



            }
        });

        btnStopAlarm = mView.findViewById(R.id.btnStopAlarmInExercise);
        btnStopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Stop Alarm", Toast.LENGTH_SHORT).show();
                int min = 1;
                int max = 9;

                Random r = new Random();
                int random_number = r.nextInt(max - min + 1) + min;
                Log.e("random number is ", String.valueOf(random_number));


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
         * res/layout/form_elements.xml
         */
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.form_elements,
                null, false);



        final RadioGroup genderRadioGroup = (RadioGroup) formElementsView
                .findViewById(R.id.genderRadioGroup);

        final EditText nameEditText = (EditText) formElementsView
                .findViewById(R.id.nameEditText);

        // the alert dialog
        new AlertDialog.Builder(getActivity()).setView(formElementsView)
                .setTitle("Select Exercise")
                .setPositiveButton("Set Alarm", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        /*
                         * Getting the value of selected RadioButton.
                         */
                        // get selected radio button from radioGroup
                        int selectedId = genderRadioGroup
                                .getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        RadioButton selectedRadioButton = (RadioButton) formElementsView
                                .findViewById(selectedId);
                        if (selectedRadioButton != null) {
                            exerciseType = selectedRadioButton.getText().toString();

                        }
                        /*
                         * Getting the value of an EditText.
                         */
                      //  toastString += "Name is: "  + "!\n";
                           exerciseDiet = "Your Diet is "+nameEditText.getText()+" Daily";

                  calendar.add(Calendar.SECOND, 3);

                final int hour;
                final int minute;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = alarmTimePicker.getHour();
                    minute = alarmTimePicker.getMinute();
                }else {
                    hour = alarmTimePicker.getCurrentHour();
                    minute = alarmTimePicker.getCurrentMinute();
                }

                Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);
                setAlarmText("You clicked a " + hour + " and " + minute);


                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                myIntent = new Intent(context, MyReceiver.class);
                myIntent.putExtra("extra", "yes");
                pending_intent = PendingIntent.getBroadcast(context, 0,
                        myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar
                        .getTimeInMillis(), pending_intent);


                // now you should change the set Alarm text so it says something nice


                setAlarmText("Alarm set to " + hour + ":" + minute);
                Toast.makeText(context, "You set the alarm",
                        Toast.LENGTH_SHORT).show();






                        dialog.cancel();
                    }

                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getActivity(), getActivity().getTitle(), Toast.LENGTH_SHORT).show();

                dialog.cancel();

            }
        }).show();
    }





}
