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
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.develop.app.myapplication.API.APIURL;
import com.develop.app.myapplication.Db.Work_order_list;
import com.develop.app.myapplication.common.KeyCodes;
import com.develop.app.myapplication.common.Validation;
import com.develop.app.myapplication.model.CaseStatusResponse;
import com.develop.app.myapplication.model.SubModule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class DashboardSubmodule extends AppCompatActivity {

    //    Button word_orderb;
    ImageView logout;
    String subModuleName;
    String ini_sync = "0";
    List<SubModule> subModules = new ArrayList<>();
    LinearLayout linearLayout;
    String finalresponse;
    SharedPreferences saved_values;
    KeyCodes keyCodes = KeyCodes.getInstance();
   Validation validation=Validation.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardmodule);
    saved_values   = getSharedPreferences(Login.My_PREF, MODE_PRIVATE);
       saved_values.getString(keyCodes.USERNAME, Login.My_PREF);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        Type listType = new TypeToken<List<SubModule>>() {
        }.getType();
        subModuleName = getIntent().getStringExtra(keyCodes.SUBMODULENAME);
        subModules = new Gson().fromJson(subModuleName, listType);
        logout = (ImageView) findViewById(R.id.img_logout);
        Button[] btn = new Button[subModules.size()];
        for (int i = 0; i < subModules.size(); i++)
        {
            final SubModule module = subModules.get(i);
            btn[i] = new Button(this);
            btn[i].setId(i);
            btn[i].setText(module.getSubmoduleName());

            /*TranslateAnimation animate = new TranslateAnimation(0,btn[i].getWidth(),0,0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            btn[i].startAnimation(animate);
            btn[i].setVisibility(View.GONE);
            */

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 20, 30, 20);

            float paddingDp = 10f;
            int paddingPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingDp, this.getResources().getDisplayMetrics());
            btn[i].setLayoutParams(layoutParams);
            btn[i].setPadding(0, 20, 0, 20);
            btn[i].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.button_design));
            btn[i].setBackground(ContextCompat.getDrawable(this, R.drawable.button_design));

            ((Button) btn[i]).setTextColor(Color.parseColor("#ffffff"));
            btn[i].setTypeface(btn[i].getTypeface(), Typeface.BOLD);


            linearLayout.addView(btn[i]);
            btn[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    /*
                    if(module.getSubmoduleName()!=null && module.getSubmoduleName().size()!=0){

                    }else{
*/
//                        new Initial_OPENSync().execute(module.getModulecountService());
//                    }
//                    Toast.makeText(DashboardSubmodule.this, module.getSubmoduleName(), Toast.LENGTH_SHORT).show();
//                    new Initial_COMPLETEDSync(DashboardSubmodule.this).execute(module.getSubModulelistService());
//                    getSubModule(module.getSubModulelistService(),DashboardSubmodule.this);

                    validation.clickAnimation(v);
                    Intent intent = new Intent(DashboardSubmodule.this, WorkOrderList.class);
                    intent.putExtra(keyCodes.LOADINGURL, module.getSubModulelistService());
                    intent.putExtra(keyCodes.COUNTURL, module.getSubModulecountService());
                    startActivity(intent);

                }
            });
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final PopupMenu popup = new PopupMenu(DashboardSubmodule.this, view);

                popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Logout");

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == 0) {

                            final Dialog dialog1 = new Dialog(DashboardSubmodule.this);
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
                                    Intent i = new Intent(DashboardSubmodule.this, Login.class);
                                    SharedPreferences.Editor editor = saved_values.edit();
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

        ProgressDialog pd = new ProgressDialog(DashboardSubmodule.this);

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

            SharedPreferences initial_sync = getSharedPreferences("nh", MODE_PRIVATE);
            String data = initial_sync.getString("worklist", "");

/*
            SharedPreferences.Editor initial_sync = getSharedPreferences("initial_sync", MODE_PRIVATE).edit();
            initial_sync.putString("val", "0");
            initial_sync.commit();
*/

            ini_sync = "0";

            List<CaseStatusResponse> caseStatusResponse = new ArrayList<>();


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
                                Log.i("Test", "onResponse:Service1 " + response);

                                try {

                                    JSONObject json = new JSONObject(response);

                                    JSONArray jsonArray1 = json.getJSONArray("workOrderdata");
                                    List<Work_order_list> work_order_lists = new ArrayList<>();

                                    SharedPreferences.Editor initial_sync = getSharedPreferences("nh", MODE_PRIVATE).edit();
                                    initial_sync.putString("worklist", jsonArray1.toString());
                                    initial_sync.commit();

                                    Log.i("Test", "onResponse:Service2 " + jsonArray1);
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
                                        work_order_lists.add(coldmodel);
                                    }

                                    Type wolTYpe = new TypeToken<List<Work_order_list>>() {
                                    }.getType();
                                    String data = new Gson().toJson(work_order_lists, wolTYpe);
                                    Log.i("Test", "onResponse:Service3 " + data);


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

        ProgressDialog pd = new ProgressDialog(DashboardSubmodule.this);
        Context context;

        public Initial_COMPLETEDSync(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            ProgressDialog pd = new ProgressDialog(DashboardSubmodule.this);
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

            Log.i("Test", "onPostExecute:---> " + finalresponse);
            SharedPreferences.Editor initial_sync = getSharedPreferences("nh", MODE_PRIVATE).edit();
            initial_sync.putString("worklist", finalresponse);
            initial_sync.commit();

            Intent intent = new Intent(DashboardSubmodule.this, WorkOrderList.class);
            startActivity(intent);


        }

        @Override
        protected String doInBackground(String[] objects) {

            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = APIURL.baseurl + objects[0];
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
                                        work_order_lists.add(coldmodel);
                                    }

                                    Type wolTYpe = new TypeToken<List<Work_order_list>>() {
                                    }.getType();
                                    String data = new Gson().toJson(work_order_lists, wolTYpe);
                                    Log.i("Test", "onResponse: Result " + data);

                                    finalresponse = data;
                                    SharedPreferences.Editor initial_sync = context.getSharedPreferences("nh", MODE_PRIVATE).edit();
                                    initial_sync.putString("worklist", finalresponse);
                                    initial_sync.commit();

                                    /*                                    SharedPreferences.Editor initial_sync = getSharedPreferences("nh", MODE_PRIVATE).edit();
                                    initial_sync.putString("worklist", data);
                                    initial_sync.commit();*/

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

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
