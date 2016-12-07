package com.example.soumendutta.mappsepnavigation.adapter;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.soumendutta.mappsepnavigation.R;
import com.example.soumendutta.mappsepnavigation.database.Config;
import com.example.soumendutta.mappsepnavigation.database.Longtap;
import com.example.soumendutta.mappsepnavigation.database.MyDatabase;

import java.util.ArrayList;


public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder>
{
    Context context;
    ArrayList<Longtap> arrayList;
    MyDatabase database;
    String toggle_status;
    private AdapterCallback mAdapterCallback;


    public AlarmListAdapter(Context context, ArrayList<Longtap> arrayList)
    {
        this.arrayList = arrayList;
        this.context = context;
        database = new MyDatabase(context);
        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @Override
    public AlarmListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.alarm_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AlarmListAdapter.ViewHolder viewHolder, final int position)
    {

        final Longtap values = arrayList.get(position);

        viewHolder.name.setText(values.getName());
        viewHolder.radius.setText(values.getRedious()+ " "+values.getUnits());
        viewHolder.description.setText(values.getDec());
        viewHolder.startDate.setText(values.getFdate());
        viewHolder.endDate.setText(values.getTdate());
        viewHolder.toggleStatus.setText(values.getTogglebutton());

        if (values.getPlayPauseStatus().equals("play"))
        {
            viewHolder.img_play.setVisibility(View.VISIBLE);
            viewHolder.img_pause.setVisibility(View.GONE);
        }
        else {
            viewHolder.img_play.setVisibility(View.GONE);
            viewHolder.img_pause.setVisibility(View.VISIBLE);
        }



        viewHolder.img_play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                viewHolder.img_play.setVisibility(View.GONE);
                viewHolder.img_pause.setVisibility(View.VISIBLE);

                Log.e("Clicked id: ", ""+(position+1));
                database.updateStatus(values.getLat(), "pause");
            }
        });

        viewHolder.img_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.img_play.setVisibility(View.VISIBLE);
                viewHolder.img_pause.setVisibility(View.GONE);

                Log.e("Clicked id: ", ""+(position+1));
                database.updateStatus(values.getLat(), "play");
            }
        });

        viewHolder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_status = values.getTogglebutton();
                showDialog(context, values, position);
            }
        });

        viewHolder.img_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                arrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, arrayList.size());
                database.deleteRow(values.getLat());

                SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
                String count = "SELECT count(*) FROM taplisttable";
                Cursor mCursor = sqLiteDatabase.rawQuery(count, null);
                mCursor.moveToFirst();
                int icount = mCursor.getInt(0);

                if (icount <= 0) {
                    mAdapterCallback.onMethodCallback();
                   // AlarmListActivity.remindEmptyLayout.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    public static interface AdapterCallback {
        void onMethodCallback();
    }

    public void showDialog(final Context context, final Longtap values, final int position) {
        final Dialog journeyDialog = new Dialog(context);
        journeyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        journeyDialog.setContentView(R.layout.journey_dialog);
        journeyDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        journeyDialog.show();


        final EditText journeyName = (EditText)journeyDialog.findViewById(R.id.journeyName);
        final EditText journeyDescription = (EditText)journeyDialog.findViewById(R.id.journeyDescription);
        final EditText journeyRadius = (EditText)journeyDialog.findViewById(R.id.journeyRadius);

        final TextView radiusUnits = (TextView) journeyDialog.findViewById(R.id.units);
        ToggleButton toggleButton = (ToggleButton) journeyDialog.findViewById(R.id.toggleButton);
        final EditText startDate = (EditText)journeyDialog.findViewById(R.id.fromDate);
        final EditText endDate = (EditText)journeyDialog.findViewById(R.id.toDate);

        Button cancel = (Button)journeyDialog.findViewById(R.id.btn_cancel);
        Button done = (Button)journeyDialog.findViewById(R.id.btn_done);

        radiusUnits.setText(Config.units);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if (isChecked)
                    toggle_status = "Coming In";
                else
                    toggle_status = "Going Out";

            }
        });

        journeyName.setText(values.getName());
        journeyDescription.setText(values.getDec());

        journeyRadius.setText(values.getRedious().replace("Miles", "").replace("Kilometer", "").replace("Meter", ""));

        if(values.getTogglebutton().equals("Going Out"))
        {
            toggleButton.setChecked(false);
        }
        else {
            toggleButton.setChecked(true);
        }

        startDate.setText(values.getFdate());
        endDate.setText(values.getTdate());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                journeyDialog.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                journeyDialog.dismiss();
                String journey_name = journeyName.getText().toString();
                String journey_description = journeyDescription.getText().toString();
                String journey_radius = journeyRadius.getText().toString();
                String start_date = startDate.getText().toString();
                String end_date = endDate.getText().toString();


                if(journey_name.isEmpty())
                {
                    journeyName.setError("Please enter the journey name");
                    return;
                }
                if(journey_description.isEmpty()) {
                    journeyDescription.setError("Please enter the journey description");
                    return;
                }
                if(journey_radius.isEmpty()) {
                    journeyRadius.setError("Please enter the journey radius");
                    return;
                }

                if(start_date.isEmpty()) {
                    startDate.setError("Please enter the start date");
                    return;
                }

                if(end_date.isEmpty()) {
                    endDate.setError("Please enter the end date");
                    return;
                }

                Longtap longtap = new Longtap(journey_radius, journey_description, journey_name,
                        start_date, end_date, toggle_status, values.getLat(), values.getLog(), values.getPlayPauseStatus(), Config.units);

                boolean update = database.updateValues(values.getLat(), longtap);

                if(update) {
                    Toast.makeText(context, "Data has been updated successfully", Toast.LENGTH_LONG).show();

                    Log.e("Position: ", ""+position);

                    arrayList.set(position, longtap);
                    notifyItemChanged(position);
                }
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder
     {
        TextView name, radius, description, startDate, endDate, toggleStatus;
        ImageView img_pause, img_play, img_edit, img_done;

        public ViewHolder(View itemView)
        {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.marker_name);
            description = (TextView)itemView.findViewById(R.id.marker_description);
            radius = (TextView)itemView.findViewById(R.id.radius);
            startDate = (TextView)itemView.findViewById(R.id.startDate);
            endDate = (TextView)itemView.findViewById(R.id.endDate);
            toggleStatus = (TextView)itemView.findViewById(R.id.journeyType);

            img_pause = (ImageView)itemView.findViewById(R.id.img_pause);
            img_play = (ImageView)itemView.findViewById(R.id.img_play);
            img_edit = (ImageView)itemView.findViewById(R.id.img_edit);
            img_done = (ImageView)itemView.findViewById(R.id.img_done);
        }
    }
}
