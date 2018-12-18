package com.naveed.myplan;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ExpancesFragment extends Fragment {

    private View mView;
    private MyDatabase db;
    private ListView mListView;
    private Context ctx;

    private ArrayList<String> listTitle, listDesc, listId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_expances, container, false);
        ctx = mView.getContext();
        db = new MyDatabase(getActivity());
        mListView = mView.findViewById(R.id.expancesListView);


        listTitle = new ArrayList<>();
        listDesc = new ArrayList<>();
        listId = new ArrayList<>();

        ListAdapter listAdapter = null;
        Cursor data = db.getAllData(MyDatabase.TABLE_EXPANCES);
        if (data.getCount() == 0) {
            MyBeansClass.alertDialog(ctx, "Expanses Details", "No data found");
        } else {
            while (data.moveToNext()) {
                listTitle.add(data.getString(1));
                listDesc.add(data.getString(2));
                listId.add(data.getString(3));

                listAdapter = new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1, listTitle);

            }
            mListView.setAdapter(listAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // Toast.makeText(ctx, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    MyBeansClass.alertDialog(ctx, "Expanses Details",
                            listTitle.get(position) + " cost " + listDesc.get(position) + "\n"
                                    + "Expenditure of last Month: 0");

                    Log.e("onItemClick: ", String.valueOf(position));
                }
            });
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    final PopupMenu popup = new PopupMenu(ctx, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.list_menu, popup.getMenu());
                    popup.show();


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            switch (id) {

                                case R.id.action_update_record:
                                    // Toast.makeText(ctx, String.valueOf(position)+": update", Toast.LENGTH_SHORT).show();
                                    final int ide = (int) System.currentTimeMillis();
                                  //  MyBeansClass.alertDialog(ctx, "ID Details", String.valueOf(currentTime));
                                    alertExpancesFormElementsUpdate(Integer.toBinaryString(position + 1));


                                    break;
                                case R.id.action_delete_record:

                                   // boolean abc = db.rowDelete(position + 1, MyDatabase.TABLE_EXPANCES);
                                    //     db.myDelete(MyDatabase.TABLE_EXPANCES ,String.valueOf( position));
                                    //  Toast.makeText(ctx, listTitle.get(position), Toast.LENGTH_SHORT).show();
                                   // Toast.makeText(ctx, String.valueOf(abc), Toast.LENGTH_SHORT).show();
                                    //     Toast.makeText(ctx, String.valueOf(position+1)+": delete", Toast.LENGTH_SHORT).show();

                                    //   db.deleteRow(String.valueOf(position) , MyDatabase.TABLE_EXPANCES);
                                    break;
                            }

                            return true;
                        }
                    });

                    return true;
                }
            });

        }


        return mView;
    }

    private void alertExpancesFormElementsUpdate(final String poss) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.form_elements_expances,
                null, false);


        final EditText etName = formElementsView
                .findViewById(R.id.formEtNameExpances);


        final EditText etPrice = formElementsView
                .findViewById(R.id.formEtPriceExpances);


        // the alert dialog
        new android.app.AlertDialog.Builder(ctx).setView(formElementsView)
                .setTitle("Enter Details")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        String name = etName.getText().toString();
                        String qty = etPrice.getText().toString();
                        Date currentTime = Calendar.getInstance().getTime();

                        updateData(poss, name, qty, currentTime.toString(), MyDatabase.TABLE_EXPANCES);
                        //  Toast.makeText(HomeActivity.this, n+" : "+q, Toast.LENGTH_SHORT).show();
/*
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.content_home, callFragment(5)).commit();*/
                        dialog.cancel();
                    }

                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        }).show();
    }

    public void updateData(String id, String title, String desc, String timeId, String table) {

        Log.e("update Method : ", title + "  " + desc + "  " + timeId);

        boolean updateData = db.updateTable(id, title, desc, timeId, table);
        if (updateData)
            Toast.makeText(ctx, "Data updated Successfully", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(ctx, "Something went wrong", Toast.LENGTH_SHORT).show();

    }
}
