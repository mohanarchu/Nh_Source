package com.develop.app.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkOrderList extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;
    RecyclerView wrk_odr_list;
    ImageView back, logout;
    Order_list_Adapter ol;
    ArrayList<OrderList> order_lists = new ArrayList<>();
    private static String TAG = "Test";
    String loadingurl = "";
    String counturl = "";
    KeyCodes keyCodes = KeyCodes.getInstance();
    int totalCount;
    TextView my1, my2;
    String userGroup, uusermodel, model = "1";
    Bundle bundle;
    Work_order_list coldmodel;
    JSONObject coldList;
    SharedPreferences notification;
    SharedPreferences sharedPreferences;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_list);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.nodata);
        sharedPreferences = getSharedPreferences(Login.My_PREF, MODE_PRIVATE);
        notification = getSharedPreferences("Notification", MODE_PRIVATE);
        userGroup = notification.getString("user_type", "");
        model = notification.getString("userGroup", "");

        uusermodel = notification.getString("hello", "");
        my1 = (TextView) findViewById(R.id.my1);
        my2 = (TextView) findViewById(R.id.my2);
        my2.setText(model);
        my1.setText(userGroup);
        wrk_odr_list = (RecyclerView) findViewById(R.id.wrk_odr_list);
        wrk_odr_list.setLayoutManager(new LinearLayoutManager(WorkOrderList.this,
                LinearLayoutManager.VERTICAL, false));
        back = (ImageView) findViewById(R.id.back);
        firebaseTokenIDUpdate();
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            String someData = extras.getString("dataUrl");
            String someData2 = extras.getString("navigateUrl");

            Notify(someData2);
        // Log.i("TestTitle", someData);
      //   Log.i("TestTitle1",someData2);
        }

        if (model.contains("0"))
        {
           Notify(userGroup);
        }
        else
        {
            loadingurl = getIntent().getExtras().getString(keyCodes.LOADINGURL);
            counturl = getIntent().getExtras().getString(keyCodes.COUNTURL);
            wrk_odr_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (!recyclerView.canScrollVertically(1)) {
                        if (totalCount > ol.orderlist.size()) {
                            LoadData(WorkOrderList.this, loadingurl);
                        }
                    }
                }
            });
        }
        getCountData(this, counturl);
        getListData(this, loadingurl);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = notification.edit();
                editor.remove("user_type");
                editor.remove("model");
                editor.remove("userGroup");
                editor.apply();
                finish();
            }
        });
        logout = (ImageView) findViewById(R.id.img_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(WorkOrderList.this, view);
                popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Logout");

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == 0) {
                            final Dialog dialog1 = new Dialog(WorkOrderList.this);
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
                                    Intent i = new Intent(WorkOrderList.this, Login.class);
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
    }

    @Override
    public void onBackPressed()
    {

        SharedPreferences.Editor editor = notification.edit();
        editor.remove("user_type");
        editor.remove("model");
        editor.remove("userGroup");
        editor.apply();
        //  startActivity(new Intent(WorkOrderList.this,DashboardSubmodule.class));


        finish();
    }

    public class Order_list_Adapter extends RecyclerView.Adapter<Order_list_Adapter.ViewHolder> {
        Context context;
        List<Work_order_list> orderlist;
        String loadingurl;

        public Order_list_Adapter(Context context, List<Work_order_list> orderlist, String loadingurl) {
            this.context = context;
            this.orderlist = orderlist;
            this.loadingurl = loadingurl;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.order_design, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.name.setText(orderlist.get(position).getCustName());
            holder.contact_no.setText(orderlist.get(position).getRequestContact());
            holder.id.setText(orderlist.get(position).getWoNumber());
            holder.requested_name.setText(orderlist.get(position).getRequestBy());
            holder.status.setText(orderlist.get(position).getWoStatus());
            holder.status_code.setText(orderlist.get(position).getWorkOrderType());
            holder.equipmentInfo.setText(orderlist.get(position).getEquipmentInfo());
            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(context, WorkOrderView.class);
                    in.putExtra("workdet", new Gson().toJson(orderlist.get(position)));
                    Log.i("mohan", new Gson().toJson(orderlist.get(position)));
                    in.putExtra("loadingurl", loadingurl);
                    startActivity(in);
                }
            });
        }

        @Override
        public int getItemCount()
        {
            if (orderlist.size() != 0)
            {
                return orderlist.size();
            }
            else
            {
                coordinatorLayout.setVisibility(View.VISIBLE);
                wrk_odr_list.setVisibility(View.GONE);
            }
             return order_lists.size();

        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView id, requested_name, status_code, name, contact_no, status;
            ImageView settings;
            LinearLayout main;
            TextView equipmentInfo;

            public ViewHolder(View itemView) {
                super(itemView);

                id = (TextView) itemView.findViewById(R.id.id);
                requested_name = (TextView) itemView.findViewById(R.id.requested_name);
                status_code = (TextView) itemView.findViewById(R.id.status_code);
                contact_no = (TextView) itemView.findViewById(R.id.contact_no);
                status = (TextView) itemView.findViewById(R.id.status);
                name = (TextView) itemView.findViewById(R.id.name);
                equipmentInfo = (TextView) itemView.findViewById(R.id.equipmentInfo);
                main = (LinearLayout) itemView.findViewById(R.id.main);
                settings = (ImageView) itemView.findViewById(R.id.settings);
            }
        }
    }

    private void getListData(final Context context, final String loadingurl)
    {
        if (isNetworkConnected()) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = APIURL.baseurl + loadingurl;
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
                                    Work_order_list coldmodel = new Gson().fromJson(coldList.toString(),
                                            Work_order_list.class);
                                    work_order_lists.add(coldmodel);
                                    Log.i("Test", "mytest:workorder " + coldList);
                                }
                                ol = new Order_list_Adapter(WorkOrderList.this, work_order_lists, loadingurl);
                                wrk_odr_list.setAdapter(ol);

/*
                                Type wolTYpe = new TypeToken<List<Work_order_list>>() {
                                }.getType();
                                String data = new Gson().toJson(work_order_lists, wolTYpe);
                                Log.i("Test", "onResponse: Result " + data);
*/


     /*                           finalresponse = data;
                                SharedPreferences.Editor initial_sync = context.getSharedPreferences("nh", MODE_PRIVATE).edit();
                                initial_sync.putString("worklist", finalresponse);
                                initial_sync.commit();

                                Intent intent = new Intent(DashboardSubmodule.this, WorkOrderList.class);
                                startActivity(intent);*/

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

                //    Toast.makeText(getApplicationContext(), "Connection Error!..", Toast.LENGTH_SHORT).show();

                }
            })

            {
                public Map<String, String> getHeaders()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new

                    DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);


        }
    }

    private void LoadData(final Context context, String mLoadingurl) {
        if (isNetworkConnected()) {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String commonUrl = mLoadingurl;
            String[] data = commonUrl.split("pageNumber=");
            String data1 = data[0];

            String numberdata = data[1];
            StringBuilder str = new StringBuilder("");
            int endURL = 0;
            for (int i = 0; i < numberdata.toCharArray().length; i++) {
                char ch = numberdata.charAt(i);
                if (Character.isDigit(ch)) {
                    str.append(ch);
                } else {
                    endURL = i;
                    break;
                }
            }

            /*for (char ch: numberdata.toCharArray()) {
                if(Character.isDigit(ch)){
                    str.append(ch);
                }else{
                    break;
                }
            }*/

            int addedCount = Integer.parseInt(str.toString()) + 1;

            String finalloadingurl = APIURL.baseurl + data1 + "pageNumber=" + addedCount + numberdata.substring(endURL);
            Log.i(TAG, "LoadData: " + finalloadingurl);
            loadingurl = data1 + "pageNumber=" + addedCount + numberdata.substring(endURL);
            String url = finalloadingurl;
            Log.i("Test", "onResponse: " + finalloadingurl);

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
                                    Work_order_list coldmodel = new Gson().fromJson
                                            (coldList.toString(), Work_order_list.class);

                                    work_order_lists.add(coldmodel);

                                }
                                int count = ol.orderlist.size();
                                ol.orderlist.addAll(work_order_lists);


                                ol.notifyItemInserted(count);
//                                 ol = new Order_list_Adapter(WorkOrderList.this, work_order_lists,loadingurl);
//                                wrk_odr_list.setAdapter(ol);

/*
                                Type wolTYpe = new TypeToken<List<Work_order_list>>() {
                                }.getType();
                                String data = new Gson().toJson(work_order_lists, wolTYpe);
                                Log.i("Test", "onResponse: Result " + data);
*/


     /*                           finalresponse = data;
                                SharedPreferences.Editor initial_sync = context.getSharedPreferences("nh", MODE_PRIVATE).edit();
                                initial_sync.putString("worklist", finalresponse);
                                initial_sync.commit();

                                Intent intent = new Intent(DashboardSubmodule.this, WorkOrderList.class);
                                startActivity(intent);*/

                                    /*                                    SharedPreferences.Editor initial_sync = getSharedPreferences("nh", MODE_PRIVATE).edit();
                                    initial_sync.putString("worklist", data);
                                    initial_sync.commit();*/

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {

                  //  Toast.makeText(getApplicationContext(), "Connection Error!..", Toast.LENGTH_SHORT).show();

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
    }

    private void getCountData(final Context context, final String counturl) {
        if (isNetworkConnected())
        {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = APIURL.baseurl + counturl;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Test", "onResponse: Count" + response);
                            try {
                                JSONObject countobj = new JSONObject(response);
//                                {"count":67,"success":true}
                                if (countobj.getBoolean("success")) {
                                    totalCount = countobj.getInt("count");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                //    Toast.makeText(getApplicationContext(), "Connection Error!..", Toast.LENGTH_SHORT).show();
                }
            })

            {
                public Map<String, String> getHeaders() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);


        }
    }

    private boolean isNetworkConnected()
    {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loadingurl != null && !loadingurl.equalsIgnoreCase("")) {
            getListData(WorkOrderList.this, loadingurl);

        }

    }

    public void Notify(final String userGroup)
    {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = APIURL.baseurl + userGroup;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Test", "onResponse: " + response);
                        try
                        {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray1 = json.getJSONArray("workOrderdata");
                            List<Work_order_list> work_order_lists = new ArrayList<>();
                            Log.i("Test", "onResponse: " + jsonArray1);
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                coldList = jsonArray1.getJSONObject(i);
                                coldmodel = new Gson().fromJson(coldList.toString(),
                                        Work_order_list.class);
                                work_order_lists.add(coldmodel);
                                Intent in = new Intent(WorkOrderList.this, WorkOrderView.class);
                                in.putExtra("workdet",coldList.toString());
                                Log.i("mohan", "myTest" + coldList.toString());
                                //AckView(WorkOrderList.this , coldmodel.getWoNumber());
                                LoadData(WorkOrderList.this,userGroup);
                                startActivity(in);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {

            //    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

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

    private void firebaseTokenIDUpdate()
    {

      if (isNetworkConnected()) {
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
    }
}
