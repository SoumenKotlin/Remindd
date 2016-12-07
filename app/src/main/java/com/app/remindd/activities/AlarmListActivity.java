package com.app.remindd.activities;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.remindd.R;
import com.app.remindd.adapter.AlarmListAdapter;
import com.app.remindd.database.Config;
import com.app.remindd.database.Longtap;
import com.app.remindd.database.MyDatabase;

import java.util.ArrayList;

/**
 * Created by soumendutta on 17/10/16.
 */

public class AlarmListActivity extends AppCompatActivity implements AlarmListAdapter.AdapterCallback{

    Activity mActivity;
    ArrayList<Longtap> arrayList;
    LinearLayout remindEmptyLayout;
    Button addLocation;
    RecyclerView recyclerView;

    ImageView img_map, img_customLayout, img_locationsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsetradius);

        mActivity = this;
        arrayList=new ArrayList<>();

        remindEmptyLayout = (LinearLayout)findViewById(R.id.remindersEmptyLayout);
        addLocation = (Button)findViewById(R.id.addLocation);

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_map = (ImageView)findViewById(R.id.img_map);
        img_customLayout = (ImageView)findViewById(R.id.img_customLayout);
        img_locationsList = (ImageView)findViewById(R.id.img_locationsList);

        img_map.setImageResource(R.drawable.ic_icon_map_black);
        img_customLayout.setImageResource(R.drawable.ic_icon_location_black);
        img_locationsList.setImageResource(R.drawable.ic_icon_alarmlist);

        img_map.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                img_map.setImageResource(R.drawable.ic_icon_map);
                img_customLayout.setImageResource(R.drawable.ic_icon_location_black);
                img_locationsList.setImageResource(R.drawable.ic_icon_alarmlist_black);

                Config.icon_status = "map";

                finish();
            }
        });


        img_customLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                img_map.setImageResource(R.drawable.ic_icon_map_black);
                img_customLayout.setImageResource(R.drawable.ic_icon_location);
                img_locationsList.setImageResource(R.drawable.ic_icon_alarmlist_black);

                Config.icon_status = "custom";

                finish();
            }
        });


        recyclerView  = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        MyDatabase db  = new MyDatabase(mActivity);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        String count = "SELECT count(*) FROM taplisttable";
        Cursor mcursor = sqLiteDatabase.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if(icount>0)
        {
            remindEmptyLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else {
            remindEmptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


        ArrayList<Longtap> values = db.getLoginData();


        for(Longtap longtap : values)
        {

            String name = longtap.getName();
            String des = longtap.getDec();
            String radius = longtap.getRedious();
            String fDate = longtap.getFdate();
            String tDate = longtap.getTdate();
            String toggle = longtap.getTogglebutton();
            String lat = longtap.getLat();
            String log = longtap.getLat();
            String status = longtap.getPlayPauseStatus();
            String units = longtap.getUnits();

            Longtap data = new Longtap(radius, name, des, fDate, tDate, toggle, lat, log, status, units);
            arrayList.add(data);
        }

        AlarmListAdapter alarmListAdapter = new AlarmListAdapter(mActivity, arrayList);
        recyclerView.setAdapter(alarmListAdapter);
    }

    @Override
    public void onMethodCallback() {

        remindEmptyLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

}
