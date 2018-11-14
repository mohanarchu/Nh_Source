package com.develop.app.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.develop.app.myapplication.API.APIURL;
import com.develop.app.myapplication.Db.Work_order_list;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkOrder extends AppCompatActivity {

    Button open_call, closed_call;
    ScheduledExecutorService scheduleTaskExecutor;
    private boolean isInFront;
    SharedPreferences login;
    String User_name = "", User_Id = "", logout = "0", sync_fail = "0", response = "", ini_sync = "0";
    ImageView logoutbtn;
    List<Work_order_list> new_call_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order);
        logoutbtn = (ImageView) findViewById(R.id.img_logout);
        open_call = (Button) findViewById(R.id.openb);
        closed_call = (Button) findViewById(R.id.closeb);

        open_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor edits = getSharedPreferences("nh", MODE_PRIVATE).edit();
                edits.putString("status", "open");
                edits.commit();

                Intent intent = new Intent(WorkOrder.this, WorkOrderList.class);
                intent.putExtra("status", "open");
                startActivity(intent);

            }
        });

        closed_call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edits = getSharedPreferences("nh", MODE_PRIVATE).edit();
                edits.putString("status", "close");
                edits.commit();

                Intent intent = new Intent(WorkOrder.this, WorkOrderList.class);
                intent.putExtra("status", "close");
                startActivity(intent);


            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final PopupMenu popup = new PopupMenu(WorkOrder.this, view);

                popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Logout");

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == 0) {

                            final Dialog dialog1 = new Dialog(WorkOrder.this);
                            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog1.setContentView(R.layout.dialog_cancelrequest);

                            Button okbtn = (Button) dialog1.findViewById(R.id.ok);
                            Button cnclbtn = (Button) dialog1.findViewById(R.id.cancel);
                            final TextView rply = (TextView) dialog1
                                    .findViewById(R.id.cancelRequestText);
                            // if button is clicked, close the custom dialog
                            rply.setText("Are you sure to logout?");
                            okbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    // TODO Auto-generated method stub
                                    dialog1.dismiss();
                                    Intent i = new Intent(WorkOrder.this, Login.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);

                                }

                            });

                            cnclbtn.setOnClickListener(new View.OnClickListener()

                            {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    dialog1.dismiss();
                                }
                            });

                            dialog1.show();

                        }

                        return true;
                    }

                });

                popup.show();


            }
        });

/*
        login = getSharedPreferences("Logged_in_details", MODE_PRIVATE);
        User_name = login.getString("UserName", "");
        User_Id = login.getString("_id", "");



        open = (Button) findViewById(R.id.openb);
        close = (Button) findViewById(R.id.closeb);

        SharedPreferences initial_sync = getSharedPreferences("initial_sync", MODE_PRIVATE);

        if (isNetworkConnected()) {

            new_call_list = new ArrayList<>();

            new_call_list = new Select()
                    .from(Work_order_list.class)
                    .where("update_status = ?", "new")
                    .execute();

            if (new_call_list.size() != 0) {

                ini_sync = "1";

                new Insert_Data_Server().execute();

            } else {

                ini_sync = "0";

                new Initial_Sync().execute();

            }

        } else {


        }

        //  }

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor edits = getSharedPreferences("service", MODE_PRIVATE).edit();
                edits.putString("status", "open");
                edits.commit();

                Intent intent = new Intent(WorkOrder.this, WorkOrderList.class);
                intent.putExtra("status", "close");
                startActivity(intent);

            }
        });

        closed_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences.Editor edits = getSharedPreferences("service", MODE_PRIVATE).edit();
                edits.putString("status", "close");
                edits.commit();

                Intent intent = new Intent(WorkOrder.this, WorkOrderList.class);
                intent.putExtra("status", "close");
                startActivity(intent);


            }
        });


 close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(WorkOrder.this, WorkOrderList.class);
                startActivity(in);
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(WorkOrder.this, WorkOrderList.class);
                startActivity(in);
            }
        });


        logoutbtn = (ImageView) findViewById(R.id.img_logout);

        logoutbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final PopupMenu popup = new PopupMenu(WorkOrder.this, v);


                        popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Logout");

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {

                                if (item.getItemId() == 0) {


                                    final Dialog dialog1 = new Dialog(WorkOrder.this);
                                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog1.setContentView(R.layout.dialog_cancelrequest);

                                    Button okbtn = (Button) dialog1.findViewById(R.id.ok);
                                    Button cnclbtn = (Button) dialog1.findViewById(R.id.cancel);
                                    final TextView rply = (TextView) dialog1
                                            .findViewById(R.id.cancelRequestText);
                                    // if button is clicked, close the custom dialog
                                    rply.setText("Are you sure to logout?");
                                    okbtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View arg0) {

                                            if (isNetworkConnected()) {

                                                new_call_list = new ArrayList<>();

                                                new_call_list = new Select()
                                                        .from(Work_order_list.class)
                                                        .where("update_status = ?", "new")
                                                        .execute();

                                                if (new_call_list.size() != 0) {

                                                    logout = "1";

                                                    new Insert_Data_Server().execute();

                                                } else {

                                                    if(scheduleTaskExecutor!=null) {
                                                        scheduleTaskExecutor.shutdown();
                                                    }

                                                    try {

                                                        new Delete().from(Work_order_list.class).execute();

                                                        SQLiteDatabase db = ActiveAndroid.getDatabase();

                                                        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "Engservicecall" + "'");

                                                    } catch (Exception e) {

                                                    }


                                                    Toast.makeText(WorkOrder.this, "Data sync completed", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(WorkOrder.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

                                                    SharedPreferences.Editor loggedin = getSharedPreferences("login_session", MODE_PRIVATE).edit();
                                                    loggedin.putString("val", "0");
                                                    loggedin.apply();

                                                    Intent i = new Intent(WorkOrder.this, Login.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);

                                                }
                                            } else {

                                                Toast.makeText(WorkOrder.this, "You must need internet connection to Logout", Toast.LENGTH_SHORT).show();

                                            }
                                            dialog1.dismiss();


                                        }

                                    });

                                    cnclbtn.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            dialog1.dismiss();
                                        }
                                    });

                                    dialog1.show();

                                }

                                return true;
                            }

                        });

                        popup.show();


                    }
                }
        );

        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        //Schedule a task to run every time (or however long you want)
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Do stuff here!
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (isNetworkConnected()) {

                            new_call_list = new ArrayList<>();

                            new_call_list = new Select()
                                    .from(Work_order_list.class)
                                    .where("update_status = ?", "new")
                                    .execute();

                            if (new_call_list.size() != 0) {

                                new Insert_Data_Server().execute();

                            }

                            if (!isInFront) {

                                Toast.makeText(getApplicationContext(), "Inserting data to server....", Toast.LENGTH_SHORT).show();

                            }
                        } else {

                            Toast.makeText(WorkOrder.this, "Need internet to sync", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        }, 1, 5, TimeUnit.MINUTES);

    }

    @Override
    public void onResume() {
        super.onResume();
        isInFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isInFront = false;
    }


    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private class Initial_Sync extends AsyncTask {

        ProgressDialog pd = new ProgressDialog(WorkOrder.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("Syncing with Server...");
            pd.setCancelable(false);
            if (!pd.isShowing()) {
                pd.show();
            }
        }

        @Override
        protected void onPostExecute(Object o) {

            if ((pd != null) && pd.isShowing()) {
                pd.dismiss();
            }

            SharedPreferences.Editor initial_sync = getSharedPreferences("initial_sync", MODE_PRIVATE).edit();
            initial_sync.putString("val", "0");
            initial_sync.commit();

            ini_sync = "0";

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {

                new Delete().from(Work_order_list.class).execute();

                SQLiteDatabase db = ActiveAndroid.getDatabase();

                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "Engservicecall" + "'");

                final List<Work_order_list> myListService = new Select()
                        .from(Work_order_list.class)
                        .execute();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                String url = APIURL.url + "initialSync";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    JSONObject json = new JSONObject(response);

                                    JSONArray jsonArray1 = json.getJSONArray("serviceList");

                                    for (int i = 0; i < jsonArray1.length(); i++) {

                                        JSONObject coldList = jsonArray1.getJSONObject(i);

                                        Work_order_list coldmodel = new Work_order_list();

                                        coldmodel.setUnique_id("0");
*/
/*
                                        coldmodel.setRequest_id(coldList.getString("request_id"));
                                        coldmodel.setCustomer_name(coldList.getString("woNumber"));
                                        coldmodel.setContact_no(coldList.getString("woStatus"));
                                        coldmodel.setEid(coldList.getString("custName"));
                                        coldmodel.setEquip_name(coldList.getString("requestBy"));
                                        coldmodel.setEquip_sl_no(coldList.getString("requestContact"));
                                        coldmodel.setContract_type(coldList.getString("contract_type"));
                                        coldmodel.setSr_type(coldList.getString("sr_type"));
                                        coldmodel.setEng_name(coldList.getString("eng_name"));
                                        coldmodel.setStatus(coldList.getString("status"));
                                        coldmodel.setProblem(coldList.getString("problem"));
                                        coldmodel.setCreated_dt(coldList.getString("created_dt"));
                                        coldmodel.setGroupID(coldList.getString("groupID"));
                                        coldmodel.setSr_no(coldList.getString("sr_no"));
                                        coldmodel.setSr_activity_dt("");
                                        coldmodel.setAck_datetime("");
                                        coldmodel.setTicket_no(coldList.getString("ticket_no"));
                                        coldmodel.setRemarks(coldList.getString("remarks"));
                                        coldmodel.setSync_mobile_status("");
                                        coldmodel.setStatus_ack(coldList.getString("status"));
                                        coldmodel.setType_updated("no");
*//*

                                        coldmodel.setUpdate_status("old");
                                        coldmodel.save();
                                    }

                                } catch (Exception e) {

                                }
                            }
                        }, new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Connection Error!..", Toast.LENGTH_SHORT).show();

                    }
                })

                {
                    protected Map<String, String> getParams() {

                        Map<String, String> para = new HashMap<String, String>();

                        para.put("userid", login.getString("_id", ""));
                        para.put("groupid", login.getString("GroupId", ""));

                        return para;
                    }
                };

                stringRequest.setRetryPolicy(new

                        DefaultRetryPolicy(8000, 3, 2));
                queue.add(stringRequest);


            }
            return null;
        }
    }

    private class Insert_Data_Server extends AsyncTask {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(WorkOrder.this);
            pd.setMessage("Inserting data to server....");
            pd.setCancelable(false);

            if (!pd.isShowing()) {
                pd.show();
            }

        }

        @Override
        protected void onPostExecute(Object o) {

            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }

            if (logout.contains("1") && sync_fail.contains("1")) {

                scheduleTaskExecutor.shutdown();

                try {

                    new Delete().from(Work_order_list.class).execute();

                    SQLiteDatabase db = ActiveAndroid.getDatabase();

                    db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "Engservicecall" + "'");

                } catch (Exception e) {

                }

                Toast.makeText(WorkOrder.this, "Data sync completed", Toast.LENGTH_SHORT).show();
                Toast.makeText(WorkOrder.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor loggedin = getSharedPreferences("login_session", MODE_PRIVATE).edit();
                loggedin.putString("val", "0");
                loggedin.apply();

                Intent i = new Intent(WorkOrder.this, Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

                logout = "0";

            } else {

                if (sync_fail.contains("0")) {

                    Toast.makeText(WorkOrder.this, "Data sync not completed.Please try again", Toast.LENGTH_SHORT).show();

                }
            }

            if (ini_sync.contains("1") && sync_fail.contains("1")) {

                if (isNetworkConnected()) {

                    new Initial_Sync().execute();

                }

            }

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            for (int i = 0; i < new_call_list.size(); i++) {

                String type = "";
                try {
                    type = new_call_list.get(i).getUpdate_status();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!(type.equals(""))) {

                    if (type.equals("yes")) {

                        if (isNetworkConnected()) {

*/
/*                            tryLogin(new_call_list.get(i).getRequest_id(), new_call_list.get(i).getStatus_ack(), new_call_list.get(i).getExpected_datetime(), new_call_list.get(i).getAck_datetime(),
                                    new_call_list.get(i).getRes_datetime(), "", "", new_call_list.get(i).getReason(), new_call_list.get(i).getEng_name(),
                                    new_call_list.get(i).getTicket_no(), new_call_list.get(i).getService_reportno(), new_call_list.get(i).getActivity_date_time(), new_call_list.get(i).getArrival_time(), new_call_list.get(i).getRes_datetime(),
                                    new_call_list.get(i).getRemarks());*//*

                        }

                    }
                }
            }


            return null;
        }

        private void tryLogin(String requestId, String status, String expectedDateTime, String ackDateTime, String resdateTime, String respDateTime, String closeDateTime,
                              String rescheduledReason, String engineerName, String ticketNo, String serviceReportNo, String activityDateTime,
                              String arrivalTime, String departureTime, String remarks) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
            HttpURLConnection connection;
            OutputStreamWriter request = null;

            URL url = null;
            String parameters = "requestId=" + requestId + "&status=" + status + "&expectedDateTime=" + expectedDateTime +
                    "&ackDateTime=" + ackDateTime + "&resdateTime=" + resdateTime + "&respDateTime=" + respDateTime + "&closeDateTime=" + closeDateTime +
                    "&rescheduledReason=" + rescheduledReason + "&engineerName=" + engineerName + "&ticketNo=" + ticketNo + "&serviceReportNo=" + serviceReportNo +
                    "&activityDateTime=" + activityDateTime + "&arrivalTime=" + arrivalTime + "&departureTime=" + departureTime + "&remarks=" + remarks;

            try {

                url = new URL(APIURL.url+ "updateSrRequestDataToServer");

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("POST");

                request = new OutputStreamWriter(connection.getOutputStream());
                request.write(parameters);
                request.flush();
                request.close();
                String line = "";
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                response = sb.toString();

                try {

                    JSONObject res = new JSONObject(response);

                    if (res.getString("result").contains("sucesss")) {

                        Log.d("service_call--", res.toString());

*/
/*                        new Update(EngServiceCallList.class)
                                .set("update_status = ?", "old")
                                .where("request_id = ?", requestId)
                                .execute();*//*

                        sync_fail = "1";

                    } else {

                        sync_fail = "0";

                        // Toast.makeText(Coldcall_Sales.this, "Sync Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }

                isr.close();
                reader.close();


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
*/


    }
}
