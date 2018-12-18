package com.naveed.myplan;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import model.ExcerciseBean;


public class ExerciseListFragment extends Fragment {

    private View mView;
    private MyDatabase db ;
    private ListView mListView;
    private Context ctx;
    private ArrayList<String>  listDesc , listID ;

    private ArrayList<ExcerciseBean> listTitle ;
    private ListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        ctx = mView.getContext();
        db = new MyDatabase(ctx);
        mListView = mView.findViewById(R.id.exerciseListView);

        listTitle = new ArrayList<>();
        listDesc = new ArrayList<>();
        listID = new ArrayList<>();

        Cursor data = db.getAllData(MyDatabase.TABLE_EXERCISE);
        if (data.getCount() == 0){
            MyBeansClass.alertDialog(ctx ,"Medicines Details" ,"No data found");
        }else{
            while (data.moveToNext()){

                ExcerciseBean excerciseBean=new ExcerciseBean();
                excerciseBean.setTitle(data.getString(1));
                excerciseBean.setDescription(data.getString(2));
                excerciseBean.setTimeId(data.getString(3));

                listTitle.add(excerciseBean);
            }

            ArrayList<String> tileArraylist=new ArrayList<>();
            for(int i=0;i<listTitle.size();i++){

                tileArraylist.add(listTitle.get(i).title);

            }

            if(tileArraylist!=null) {
                listAdapter = new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1, tileArraylist);
                mListView.setAdapter(listAdapter);

            }else{


            }


            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // Toast.makeText(ctx, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    MyBeansClass.alertDialog(ctx ,"Exercise Details" , "You have to for "+



                    listTitle.get(position)+" at "+listDesc.get(position));

                    Log.e("onItemClick: ", String.valueOf(position));
                }
            });
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                    PopupMenu popup = new PopupMenu(ctx, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.list_menu, popup.getMenu());
                    popup.show();


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            switch (id){
                                case R.id.action_update_record:
                                    Toast.makeText(ctx, String.valueOf(position)+": update", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.action_delete_record:

                                    ExcerciseBean excerciseBean=(ExcerciseBean)listTitle.get(position);
                                 //   Toast.makeText(ctx, String.valueOf(position)+": delete", Toast.LENGTH_SHORT).show();
                                    boolean abc = db.rowDelete(excerciseBean.timeId , MyDatabase.TABLE_EXERCISE);
                                    //     db.myDelete(MyDatabase.TABLE_EXPANCES ,String.valueOf( position));
                                    //  Toast.makeText(ctx, listTitle.get(position), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(ctx, String.valueOf(abc), Toast.LENGTH_SHORT).show();
                                    if(abc) {
                                        if (listAdapter != null) {

                                        }
//
                                    }
                                    else{

                                    }
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





}
