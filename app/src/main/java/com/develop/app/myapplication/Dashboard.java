package com.develop.app.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.develop.app.myapplication.Db.Work_order_list;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.develop.app.myapplication.API.APIURL.caselist;
import static com.develop.app.myapplication.API.APIURL.closedcaselist;

public class Dashboard extends AppCompatActivity {

    Button word_orderb;
    ImageView logout;
    String moduleName;
    String ini_sync = "0";
TextView helli;
String hello="";
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Bundle extras = getIntent().getExtras();
        moduleName = getIntent().getStringExtra("subModuleName");
        word_orderb = (Button) findViewById(R.id.word_order);
        logout = (ImageView) findViewById(R.id.img_logout);
        word_orderb.setText(moduleName);
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                helli=(TextView)findViewById(R.id.action);
                 hello=getIntent().getStringExtra("hello");
                helli.setText(hello);
                final PopupMenu popup = new PopupMenu(Dashboard.this, view);

                popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Logout");

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == 0) {

                            final Dialog dialog1 = new Dialog(Dashboard.this);
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
                                    Intent i = new Intent(Dashboard.this, Login.class);
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

        word_orderb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(Dashboard.this, WorkOrder.class);
                startActivity(in);
            }
        });
        new Initial_OPENSync().execute();
        new Initial_COMPLETEDSync().execute();


    }

    private class Initial_OPENSync extends AsyncTask {

        ProgressDialog pd = new ProgressDialog(Dashboard.this);

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

//                new Delete().from(Work_order_list.class).execute();

//                SQLiteDatabase db = ActiveAndroid.getDatabase();

//                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "workorderlist" + "'");

/*
                final List<Work_order_list> myListService = new Select()
                        .from(Work_order_list.class)
                        .execute();
*/

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                String url = caselist;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("Test", "onResponse: " + response);
                                try {

                                    JSONObject json = new JSONObject(response);

                                    JSONArray jsonArray1 = json.getJSONArray("workOrderdata");
                                    List<Work_order_list> work_order_lists = new ArrayList<>();
                                    Log.i("Test", "onResponse: " + jsonArray1);
                                    for (int i = 0; i < jsonArray1.length(); i++) {

                                        JSONObject coldList = jsonArray1.getJSONObject(i);

                                        Work_order_list coldmodel = new Work_order_list();

                                        coldmodel.setUnique_id("0");
                                        coldmodel.setWoNumber(coldList.getString("woNumber"));
                                        coldmodel.setWoStatus(coldList.getString("woStatus"));
                                        coldmodel.setCustName(coldList.getString("custName"));
                                        coldmodel.setRequestBy(coldList.getString("requestBy"));
                                        coldmodel.setRequestContact(coldList.getString("requestContact"));
                                        coldmodel.setProblemReported(coldList.getString("problemReported"));
                                        coldmodel.setProblemObserved(coldList.getString("problemObserved"));
                                        coldmodel.setActionTaken(coldList.getString("actionTaken"));
                                        coldmodel.setWorkOrderType(coldList.getString("workOrderType"));
                                        coldmodel.setEquipmentInfo(coldList.getString("equipmentInfo"));
                                        coldmodel.setApproveName(coldList.getString("approvedByName"));
                                        coldmodel.setApprovePopUpmessage(coldList.getString("approvePopUpmessage"));
//                                        coldmodel.save();
                                        Log.i("Test ", "onResponse: " + i + " ");
                                        work_order_lists.add(coldmodel);
                                    }

                                    Type wolTYpe= new TypeToken<List<Work_order_list>>() {}.getType();
                                    String data= new Gson().toJson(work_order_lists,wolTYpe);
                                    Log.i("Test", "onResponse: " + data);

                                    SharedPreferences.Editor initial_sync = getSharedPreferences("nh", MODE_PRIVATE).edit();
                                    initial_sync.putString("worklist", data);
                                    initial_sync.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
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
                    /*protected Map<String, String> getParams() {

                        Map<String, String> para = new HashMap<String, String>();

                        para.put("pageNumber", "1");
                        para.put("recordsPerPage", "5");
                        para.put("woStatus", "OPENED");
                        para.put("userId", "50");

                        return para;
                    }*/

                    public Map<String, String> getHeaders() {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new

                        DefaultRetryPolicy(8000, 3, 2));
                queue.add(stringRequest);


            }
            return null;
        }
    }

    private class Initial_COMPLETEDSync extends AsyncTask {

        ProgressDialog pd = new ProgressDialog(Dashboard.this);

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

//                new Delete().from(Work_order_list.class).execute();

//                SQLiteDatabase db = ActiveAndroid.getDatabase();

//                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "workorderlist" + "'");

/*
                final List<Work_order_list> myListService = new Select()
                        .from(Work_order_list.class)
                        .execute();
*/

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

//                String url = Config.BASE_URL + "initialSync";
                String url = closedcaselist;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("Test", "onResponse: " + response);
                                try {
                                    JSONObject json = new JSONObject(response);

                                    JSONArray jsonArray1 = json.getJSONArray("workOrderdata");
                                    List<Work_order_list> work_order_lists = new ArrayList<>();
                                    Log.i("Test", "onResponse: " + jsonArray1);
                                    for (int i = 0; i < jsonArray1.length(); i++) {

                                        JSONObject coldList = jsonArray1.getJSONObject(i);

                                        Work_order_list coldmodel = new Work_order_list();

                                        coldmodel.setUnique_id("0");
                                        coldmodel.setWoNumber(coldList.getString("woNumber"));
                                        coldmodel.setWoStatus(coldList.getString("woStatus"));
                                        coldmodel.setCustName(coldList.getString("custName"));
                                        coldmodel.setRequestBy(coldList.getString("requestBy"));
                                        coldmodel.setRequestContact(coldList.getString("requestContact"));
                                        coldmodel.setProblemReported(coldList.getString("problemReported"));
                                        coldmodel.setProblemObserved(coldList.getString("problemObserved"));
                                        coldmodel.setActionTaken(coldList.getString("actionTaken"));
                                        coldmodel.setWorkOrderType(coldList.getString("workOrderType"));
                                        coldmodel.setEquipmentInfo(coldList.getString("equipmentInfo"));
//                                        coldmodel.save();
                                        Log.i("Test ", "onResponse: " + i + " ");
                                        work_order_lists.add(coldmodel);
                                    }

                                    Type wolTYpe= new TypeToken<List<Work_order_list>>() {}.getType();
                                    String data= new Gson().toJson(work_order_lists,wolTYpe);
                                    Log.i("Test", "onResponse: " + data);

                                    SharedPreferences.Editor initial_sync = getSharedPreferences("nh", MODE_PRIVATE).edit();
                                    initial_sync.putString("completedworklist", data);
                                    initial_sync.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
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


                    public Map<String, String> getHeaders() {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new

                        DefaultRetryPolicy(8000, 3, 2));
                queue.add(stringRequest);


            }
            return null;
        }
    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
