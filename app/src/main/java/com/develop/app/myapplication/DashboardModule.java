package com.develop.app.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.develop.app.myapplication.API.APIURL;
import com.develop.app.myapplication.Db.Work_order_list;
import com.develop.app.myapplication.common.KeyCodes;
import com.develop.app.myapplication.common.Validation;
import com.develop.app.myapplication.model.Module;
import com.develop.app.myapplication.model.SubModule;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardModule extends AppCompatActivity {

    //    Button word_orderb;
    String id;
    ImageView logout;
    TextView helli;
    String moduleName;
    String ini_sync = "0";
    String hello="0";
    List<Module> modules = new ArrayList<>();
    LinearLayout linearLayout;
    String finalresponse;
    KeyCodes keyCodes = KeyCodes.getInstance();
    Bundle extras;
    SharedPreferences sharedPreferences;
Validation validation=Validation.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardmodule);

        if (getIntent().getExtras()!= null)
        {
            String hello1;
            for (String key : getIntent().getExtras().keySet())
            {
                if (   key.equals("title"))
                    Log.i("mykey",getIntent().getExtras().getString(key));

                if (key.equals("message"))
                    Log.i("mybody",getIntent().getExtras().getString(key));
            }
        }

        helli=(TextView)findViewById(R.id.action);
        helli.setText(hello);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        sharedPreferences = getSharedPreferences(Login.My_PREF, MODE_PRIVATE);
        sharedPreferences.getString(keyCodes.USERNAME, "");
        Type listType = new TypeToken<List<Module>>()
        {
        }.getType();
            moduleName = sharedPreferences.getString(keyCodes.SUBMODULENAME, "");
            modules = new Gson().fromJson(moduleName, listType);
            Log.i("Test", "onCreate: " + moduleName);
            logout = (ImageView) findViewById(R.id.img_logout);
            if (sharedPreferences.getString(keyCodes.REFRESHTOKEN, "").equalsIgnoreCase(""))
            {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("refreshToken", FirebaseInstanceId.getInstance().getToken());
                edit.apply();
            }
            id = getIntent().getStringExtra(keyCodes.USERID);
            firebaseTokenIDUpdate();
            Button[] btn = new Button[modules.size()];
            for (int i = 0; i < modules.size(); i++) {
                final Module module = modules.get(i);
                btn[i] = new Button(this);
                btn[i].setId(i);
                btn[i].setText(module.getModuleName());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(30, 0, 30, 0);
                btn[i].setLayoutParams(layoutParams);
                btn[i].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.button_design));
                btn[i].setBackground(ContextCompat.getDrawable(this, R.drawable.button_design));
                ((Button) btn[i]).setTextColor(Color.parseColor("#ffffff"));
                btn[i].setTypeface(btn[i].getTypeface(), Typeface.BOLD);
                linearLayout.addView(btn[i]);
                btn[i].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        validation.clickAnimation(v);
                        if (module.getSubModules() != null && module.getSubModules().size() != 0) {
                            Type listType = new TypeToken<List<SubModule>>() {
                            }.getType();
                            Intent i = new Intent(DashboardModule.this, DashboardSubmodule.class);
                            i.putExtra(keyCodes.SUBMODULENAME, new Gson().toJson(module.getSubModules(), listType));
                            startActivity(i);
                        } else {
//                        new Initial_OPENSync().execute(module.getModulecountService());
//                        new Initial_COMPLETEDSync().execute(module.getModulelistService());
//                        getModule(module.getModulelistService());
                            Intent intent = new Intent(DashboardModule.this, WorkOrderList.class);
                            intent.putExtra(keyCodes.LOADINGURL, module.getModulelistService());
                            intent.putExtra(keyCodes.COUNTURL, module.getModulecountService());
                            startActivity(intent);

                        }
                    }
                });
            }
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final PopupMenu popup = new PopupMenu(DashboardModule.this, view);
                    popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Logout");
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item)
                        {
                            if (item.getItemId() == 0) {

                                final Dialog dialog1 = new Dialog(DashboardModule.this);
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
                                        Intent i = new Intent(DashboardModule.this, Login.class);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.remove("userName").apply();
                                        editor.remove("password").apply();
                                        editor.remove("isLoginKey").apply();
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
        word_orderb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(DashboardModule.this, WorkOrder.class);
                startActivity(in);
            }
        });
*/

//        new Initial_OPENSync().execute();
//        new Initial_COMPLETEDSync().execute();


    }

    private class Initial_OPENSync extends AsyncTask<String, String, String> {

        ProgressDialog pd = new ProgressDialog(DashboardModule.this);

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
        protected void onPostExecute(String o) {

            if ((pd != null) && pd.isShowing()) {
                pd.dismiss();
            }
/*
            SharedPreferences.Editor initial_sync = getSharedPreferences("initial_sync", MODE_PRIVATE).edit();
            initial_sync.putString("val", "0");
            initial_sync.commit();

            ini_sync = "0";
*/
            SharedPreferences.Editor initial_sync = getSharedPreferences("nh", MODE_PRIVATE).edit();
            initial_sync.putString("worklist", finalresponse);
            initial_sync.commit();

            Intent intent = new Intent(DashboardModule.this, WorkOrderList.class);
            startActivity(intent);

        }

        @Override
        protected String doInBackground(String[] objects) {

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
                String url = APIURL.baseurl + objects[0];
                Log.i("Test", "doInBackground: " + url);
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

                                        coldmodel.setUnique_id(id);
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

                                    Type wolTYpe = new TypeToken<List<Work_order_list>>() {
                                    }.getType();
                                    String data = new Gson().toJson(work_order_lists, wolTYpe);
                                    Log.i("Test", "onResponse: " + data);
                                    finalresponse = data;

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

    private class Initial_COMPLETEDSync extends AsyncTask<String, String, String> {

        ProgressDialog pd = new ProgressDialog(DashboardModule.this);

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
        protected void onPostExecute(String o) {

            if ((pd != null) && pd.isShowing()) {
                pd.dismiss();
            }

/*
            SharedPreferences.Editor initial_sync = getSharedPreferences("initial_sync", MODE_PRIVATE).edit();
            initial_sync.putString("val", "0");
            initial_sync.commit();


            ini_sync = "0";
            Toast.makeText(DashboardModule.this, "Completed", Toast.LENGTH_SHORT).show();
*/


            Intent intent = new Intent(DashboardModule.this, WorkOrderList.class);
            startActivity(intent);

        }

        @Override
        protected String doInBackground(String[] objects)
        {
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
//                String url =   closedcaselist;
                String url = APIURL.baseurl + objects[0];
                Log.i("Test", "doInBackground: " + url);
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

                                    Type wolTYpe = new TypeToken<List<Work_order_list>>() {
                                    }.getType();
                                    String data = new Gson().toJson(work_order_lists, wolTYpe);
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

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

   /* private void getModule(String geturl){
        if (isNetworkConnected()) {

//                new Delete().from(Work_order_list.class).execute();

//                SQLiteDatabase db = ActiveAndroid.getDatabase();

//                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "workorderlist" + "'");

*//*
                final List<Work_order_list> myListService = new Select()
                        .from(Work_order_list.class)
                        .execute();
*//*

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

//                String url = Config.BASE_URL + "initialSync";
//                String url =   closedcaselist;
            String url = APIURL.baseurl + geturl;
            Log.i("Test", "doInBackground: " + url);
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
                                Type wolTYpe = new TypeToken<List<Work_order_list>>() {
                                }.getType();
                                String data = new Gson().toJson(work_order_lists, wolTYpe);
                                Log.i("Test", "onResponse: " + data);

                                SharedPreferences.Editor initial_sync = getSharedPreferences("nh", MODE_PRIVATE).edit();
                                initial_sync.putString("worklist", data);
                                initial_sync.commit();

                                Intent intent = new Intent(DashboardModule.this, WorkOrderList.class);
                                startActivity(intent);
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
                    *//*protected Map<String, String> getParams() {

                        Map<String, String> para = new HashMap<String, String>();

                        para.put("pageNumber", "1");
                        para.put("recordsPerPage", "5");
                        para.put("woStatus", "OPENED");
                        para.put("userId", "50");

                        return para;
                    }*//*

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

    }*/
    private void firebaseTokenIDUpdate()
    {
        if(isNetworkConnected()) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = APIURL.baseurl + "updateMobileDeviceId.mobile";
            Log.i("Test", "FB :: " + url);

            Map<String, String> para = new HashMap<String, String>();

            para.put(keyCodes.USERID, sharedPreferences.getString(keyCodes.USERID, ""));
            para.put(keyCodes.DEVICEID, sharedPreferences.getString(keyCodes.REFRESHTOKEN, ""));
            Log.i("Test", "FB : " + para);
            JSONObject objRegData = new JSONObject(para);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, url, objRegData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Test", "Firebase onResponse: " + response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };

            jsObjRequest.setRetryPolicy(new
                    DefaultRetryPolicy(8000, 3, 2));
            queue.add(jsObjRequest);
        }




            /*StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Test", "onResponse: approve Work " + response);
                            LoginResponse loginResponse = new Gson().fromJson(response.toString(), LoginResponse.class);
                            try {
                                if (loginResponse.getSuccess()) {
                                *//*    Intent i = new Intent(getApplicationContext(), WorkOrderList.class);
                                    i.putExtra("loadingurl", loadingurl);
                                    ((Activity) context).startActivity(i);
                                *//*
                                    ((Activity) context).finish();
                                } else {
                                    TastyToast.makeText(getApplicationContext(), loginResponse.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                                }
                            } catch (Exception e) {
                                TastyToast.makeText(getApplicationContext(), "Unknown Error", TastyToast.LENGTH_LONG, TastyToast.ERROR);
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
                @Override
                protected Map<String, String> getParams() {


                    Map<String, String> para = new HashMap<String, String>();

                    para.put("approvedByUserName", sharedPreferences.getString("userId", ""));
                    para.put("woNumber", work_order_list.getWoNumber());
                    para.put("status", work_order_list.getActionStatus());
                    para.put("approvedDateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS").format(new Date()));
                    para.put("customerRemarks", comments.getText().toString());
                    Log.i("Test", "getParams: " + para);
                    return para;
                }

                @Override
                public Map<String, String> getHeaders() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new

                    DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);
        */
        }
    }




