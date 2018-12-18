package com.naveed.myplan;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;


public class MeetingsFragment extends Fragment {

    Context context;
    View mView;
    Button btnStartAlarm , btnStopAlarm , btnChk;
    AlarmManager alarmManager;
    private PendingIntent pending_intent;

    private TimePicker alarmTimePicker;
    private TextView alarmTextView;
    private MyReceiver alarm;
    Calendar calendar;
    Intent myIntent;
    MyDatabase myDb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!HomeActivity.inside) {
            HomeActivity.inside = true;
        }
        mView = inflater.inflate(R.layout.fragment_meetings, container, false);
        context = mView.getContext();

        alarmTextView = mView.findViewById(R.id.tvInMeetings);
        myDb = new MyDatabase(context);

        alarmTimePicker = mView.findViewById(R.id.alarmTimePickerMeetings);
        //   alarmTextView = mView.findViewById(R.id.alarmText);




        btnStartAlarm = mView.findViewById(R.id.btnSetAlarmInMeetings);
        btnStartAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertFormElements();




            }
        });

        btnStopAlarm = mView.findViewById(R.id.btnStopAlarmInMeetings);
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
                myIntent.putExtra("id", "");
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


    private void alertFormElements() {

        /*
         * Inflate the XML view. activity_main is in
         * res/layout/form_elements_exercise.xmlrcise.xml
         */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.form_elements_meetings,
                null, false);


        final EditText etMeetingWith = formElementsView
                .findViewById(R.id.formEtWhomMeetings);

        final EditText etMeetingTitle = formElementsView
                .findViewById(R.id.formEtTitleMeetings);

        final EditText etMeetingDesc = formElementsView
                .findViewById(R.id.formEtDescMeetings);



        // the alert dialog
        new AlertDialog.Builder(getActivity()).setView(formElementsView)
                .setTitle("Enter Details")
                .setPositiveButton("Set Meeting", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        String meetingTitle = etMeetingTitle.getText().toString();
                        String meetingDesc = etMeetingDesc.getText().toString() +
                                " meeting with "+etMeetingWith.getText().toString();

                       // Toast.makeText(context, with+" : "+t+" : "+d, Toast.LENGTH_SHORT).show();

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
                        meetingDesc = meetingDesc + hour + " : "+minute;

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


                        // now you should change the set Alarm text so it says something nice

                        // added to database
                        addData(meetingTitle , meetingDesc , String.valueOf(_id));

                        setAlarmText("Alarm set to " + hour + ":" + minute);
                        Toast.makeText(context, "You set the alarm",
                                Toast.LENGTH_SHORT).show();

                        dialog.cancel();
                    }

                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

      //          Toast.makeText(context, getActivity().getTitle(), Toast.LENGTH_SHORT).show();

                dialog.cancel();

            }
        }).show();
    }

    public void addData(String name, String title, String desc){

        Log.e( "addData Method : ",name+"  "+title+"  "+desc);

        boolean insertData = myDb.insertData(name , title , desc , MyDatabase.TABLE_MEETINGS);
        if (insertData)
            Toast.makeText(context, "Data inserted Successfully", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

    }


}
