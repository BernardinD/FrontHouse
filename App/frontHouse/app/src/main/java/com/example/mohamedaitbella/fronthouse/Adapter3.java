package com.example.mohamedaitbella.fronthouse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

public class Adapter3 extends RecyclerView.Adapter<Adapter3.ViewHolder> {

    Shift[] list;
    String state;
    Context context;

    Adapter3(Shift[] list, String state, Context context){

        this.list = list;
        this.state = state;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell3, parent, false);
        Adapter3.ViewHolder holder = new Adapter3.ViewHolder(view);
        Log.d("Adapter3", "returning viewholder");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.state.setText(state);
        viewHolder.employee.setText(list[i].EmpFirstName + " " + list[i].EmpLastName);

        String shifts[] = {"", ""};
        try{
            Gson gson = new Gson();
            Home.Time(new JSONObject(gson.toJson(list[i])), 0);
        }catch (Exception e){
            Log.d("Adapter3", e.getMessage());
        }
        viewHolder.shift.setText(state.equals("AM")? shifts[0] : shifts[1]);


    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView state, employee, shift, job;
        Button click;

        public ViewHolder(@NonNull View view) {
            super(view);
            state = view.findViewById(R.id.State);
            employee = view.findViewById(R.id.MyShift);
            shift = view.findViewById(R.id.Time);
            job = view.findViewById(R.id.Title);
            click = view.findViewById(R.id.request2);


            //click.setText((list.JobStatus == 0)? "SWAP":"PICK-UP");
            click.setOnClickListener(new View.OnClickListener() {
                @Override
                // Should open swap/pickup dialog box, with cancel button
                // Button determined by employee shift status
                // Send to firebase on completion (for pickup)
                public void onClick(View view) {

                    int cases = 1;//list[i].JobStatus

                    // Do buttons. Link:
                    // https://stackoverflow.com/questions/17622622/how-to-pass-data-from-a-fragment-to-a-dialogfragment

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    AlertDialog dialog;
                    Bundle args = new Bundle();
                    switch (cases){

                        // Swap, job not currently on request
                        case 0:
                            builder.setMessage("Are you sure you would like to SWAP? Employee must have already agreed to SWAP.");

                            // If yes, send API call for swap
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferences share = context.getSharedPreferences(Home.pref,0);
                                    int send = list[getAdapterPosition()].ScheduleID,
                                            id = list[getAdapterPosition()].EmployeeID;

                                    APICall apicall = new APICall();
                                    String url = "http://knightfinder.com/WEBAPI/Swap.aspx",
                                            payload = "{\"EmployeeID\": \""+share.getInt("EmployeeID", -1)+"\"," +
                                                    "\"EmployeeID\": \"" +id+ ", \"ScheduleID\": \""+send+"\"}";
                                    apicall.execute(url, payload);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.cancel();
                                }
                            });
                            dialog = builder.create();
                            break;

                        // Pickup Shift
                        default:
                            builder.setMessage("Are you sure you would like to PICK-UP this shift?");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int send = list[getAdapterPosition()].ScheduleID;
                                    // ShiftID still needed for firebase request

                                    // Firebase request(new function needed): sendRequest(Employee1ID, Employee2ID, ScheduleID).
                                    // Sends to constant url and Manager's app takes care of rest
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.cancel();
                                }
                            });
                            dialog = builder.create();
                            break;

                    }
                }
            });
        }
    }
}
