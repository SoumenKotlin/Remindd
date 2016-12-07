package com.app.remindd.activities;

/**
 * Created by soumendutta on 7/10/16.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.remindd.R;
import com.app.remindd.database.Config;
import com.app.remindd.database.MyDatabase;


public class SettingsActivity extends AppCompatActivity
{
    BottomSheetDialog bottomSheetDialog;

    ListView bottomlist;
    ImageView imageView;
    ListView listView;

    String[] items = {"Select radius unit"+"\n"+ Config.units," Help"};
    MyDatabase database;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settinglist_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(Color.WHITE);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        database = new MyDatabase(this);

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView = (ListView)findViewById(R.id.listViewfragment);
        listView.setAdapter(itemsAdapter);

        final View bottomSheet = LayoutInflater.from(this).inflate(R.layout.radius_bottom_sheet, null, false);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheet);
        bottomlist = (ListView) bottomSheet.findViewById(R.id.bottomlist);
        imageView = (ImageView)findViewById(R.id.imagehelp);

        String[] listitem={"Meter","Kilometer","Miles"};
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listitem);

        bottomlist.setAdapter(listAdapter);

        bottomlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //bottomSheetDialog.hide();
                bottomSheetDialog.dismiss();
                String items1=parent.getItemAtPosition(position).toString();
                items[0]= "Select radius unit"+"\n"+items1;
                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(SettingsActivity.this, android.R.layout.simple_list_item_1, items);
                listView.setAdapter(itemsAdapter);
                Config.units=items1;
                boolean update = database.updateColumn(items1);
                if(update) {
                    Log.e("database", "update successfully");
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                if(position==0)

                bottomSheetDialog.show();
                else
                {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageView.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                        }
                    });

                    listView.setVisibility(View.GONE);
                }

            }
        });

       /* bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialog) {

            }
        });

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {

            }
        });*/


    }


}
