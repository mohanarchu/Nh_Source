package com.develop.app.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.develop.app.myapplication.API.APIURL;
import com.develop.app.myapplication.Db.Work_order_list;
import com.develop.app.myapplication.common.KeyCodes;
import com.develop.app.myapplication.common.Validation;
import com.develop.app.myapplication.model.LoginResponse;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class WorkOrderView extends AppCompatActivity {
     TextView otp;
     EditText get_otp;
     String message;
     TextView  verify;
     LinearLayout for_otp,for_approve;
    ImageView logout;
    TextView wonumber;
    TextView status;
    TextView cust_name;
    TextView req_by;
    TextView prob_reported;
    TextView prob_absorverd;
    TextView action_taken;
    TextView approved_by;
    TextView closeddate;
    EditText comments;
    Bitmap sign = null;
    String loadingurl;
    ImageView signView;
    FrameLayout signViewTemp;
  RequestQueue queue;
    LinearLayout editLL, backLL;
  AlertDialog alertDialog;
    TextView mDepartment, mEquiment, mCeid, mOpen, mAcknowledged;
    SharedPreferences sharedPreferences,mypref;
    String userGroup,model;
    Validation validation = Validation.getInstance();
    KeyCodes keyCodes = KeyCodes.getInstance();
  ScrollView scrollView;
  String userId,data;
  TextView myotp;
  ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_order_view);
        sharedPreferences = getSharedPreferences("nh", MODE_PRIVATE);
        mypref=getSharedPreferences("Notification", MODE_PRIVATE);


       firebaseTokenIDUpdate();

         userId=sharedPreferences.getString(keyCodes.USERID,"");
        otp=(TextView)findViewById(R.id.otp);
        get_otp=(EditText)findViewById(R.id.otp_text);
        verify=(TextView)findViewById(R.id.verify);
        for_otp=(LinearLayout)findViewById(R.id.otp_view);
        myotp=(TextView)findViewById(R.id.generated);
        for_approve=(LinearLayout)findViewById(R.id.hide);
        wonumber = (TextView) findViewById(R.id.wonumber);
        status = (TextView) findViewById(R.id.status);
        cust_name = (TextView) findViewById(R.id.cust_name);
        req_by = (TextView) findViewById(R.id.req_by);
        prob_reported = (TextView) findViewById(R.id.prob_reported);
        prob_absorverd = (TextView) findViewById(R.id.prob_absorverd);
        action_taken = (TextView) findViewById(R.id.action_taken);
        approved_by = (TextView) findViewById(R.id.approved_by);
        closeddate = (TextView) findViewById(R.id.edit_closeddate);
        comments = (EditText) findViewById(R.id.comments);
        editLL = (LinearLayout) findViewById(R.id.edit_layout);
        backLL = (LinearLayout) findViewById(R.id.back_layout);
        mCeid = (TextView) findViewById(R.id.edit_cedi);
        mDepartment = (TextView) findViewById(R.id.edit_dept);
        scrollView =(ScrollView)findViewById(R.id.scroling);
        mEquiment = (TextView) findViewById(R.id.edit_equip);
        mAcknowledged = (TextView) findViewById(R.id.edit_acknowledged_date);
        mOpen = (TextView) findViewById(R.id.edit_openeddate);
        signViewTemp = (FrameLayout) findViewById(R.id.sign_view);
        signView = (ImageView) findViewById(R.id.sign_image);
        for_otp.setVisibility(View.VISIBLE);
      for_approve.setVisibility(View.INVISIBLE);
       back=(ImageView)findViewById(R.id.back1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        signViewTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(WorkOrderView.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setContentView(R.layout.signpad);
                dialog.show();
                final SignaturePad signatureView = (SignaturePad) dialog.findViewById(R.id.signature_pad);
                if (sign != null) {
                    Matrix matrix = new Matrix();
               matrix.preRotate(270);
                    Bitmap bitmapOrg = sign;
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, bitmapOrg.getWidth(),
                            bitmapOrg.getHeight(), matrix, true);
                    signatureView.setSignatureBitmap(rotatedBitmap);
                }

                Button clear = (Button) dialog.findViewById(R.id.clear_button);
                Button done = (Button) dialog.findViewById(R.id.save_button);
                                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        signatureView.clear();
                    }
                });
                done.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        Bitmap bitmapOrg = signatureView.getSignatureBitmap();
                      Bitmap rotatedBitmaps = Bitmap.createBitmap(bitmapOrg, 0, 0, bitmapOrg.getWidth(),
                                bitmapOrg.getHeight(), matrix, true);
                       sign = rotatedBitmaps;
                        signView.setImageBitmap(rotatedBitmaps);
                        dialog.dismiss();
                    }
                });
            }
        });
        data = getIntent().getExtras().getString("workdet");
        final Work_order_list work_order_list = new Gson().fromJson(data, Work_order_list.class);
        Log.i("jsonfile" , new Gson().toJson(work_order_list));
        Log.i("Test", "onCreate: " + data);
        wonumber.setText(work_order_list.getWoNumber());
        status.setText(work_order_list.getWoStatus());
        cust_name.setText(work_order_list.getCustName());
        req_by.setText(work_order_list.getRequestBy());
        prob_reported.setText(work_order_list.getProblemReported());
        prob_absorverd.setText(work_order_list.getProblemObserved());
        action_taken.setText(work_order_list.getActionTaken());
        approved_by.setText(work_order_list.getApproveName());
        mAcknowledged.setText(work_order_list.getAckDateTime());
        mOpen.setText(work_order_list.getOpenedDateTime());
        mEquiment.setText(work_order_list.getEquipmentModel());
        mCeid.setText(work_order_list.getEquipmentCeid());
        mDepartment.setText(work_order_list.getEquipmentDepartment());
        closeddate.setText(work_order_list.getCompletedDateTime());
        myotp.setText(work_order_list.getGenerateOtpButton());
        myotp.setVisibility(View.GONE);

        if (work_order_list.getWoStatus().equalsIgnoreCase("COMPLETED")
                ||
                work_order_list.getWoStatus().equalsIgnoreCase("COMPLETE")
                ||
                work_order_list.getWoStatus().equalsIgnoreCase("CLOSED")
                ||
                work_order_list.getWoStatus().equalsIgnoreCase("CLOSE"))
        {


            editLL.setVisibility(View.VISIBLE);
            backLL.setVisibility(View.GONE);
        }
        else
            {
            editLL.setVisibility(View.GONE);
            backLL.setVisibility(View.VISIBLE);
        }
        if(work_order_list.getWoStatus().equalsIgnoreCase("OPENED") ||
                work_order_list.getWoStatus().equalsIgnoreCase("OPEN"))
        {
            AckView(this, work_order_list);
            showToast("Acknowledged successfully");

        }
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
      if (myotp.getText().toString().equals("true"))
      {
          otp.setText("Generate OTP");
          for_otp.setVisibility(View.GONE);
      }
      else
      {
          otp.setText("Resend OTP");
      }

    findViewById(R.id.otp).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (otp.getText().toString().equals("Generate OTP"))
            {

                final AlertDialog.Builder builder = new AlertDialog.Builder(WorkOrderView.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertdialogue, null);
                builder.setView(dialogView);
                TextView editText = (TextView) dialogView.findViewById(R.id.dialgue);
                editText.setText(work_order_list.getApprovePopUpmessage());
                TextView ok = (TextView) dialogView.findViewById(R.id.otp_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        alertDialog.dismiss();
                        final String URL = APIURL.baseurl + "generateCustomerApprovalCode.mobile";
                        queue = Volley.newRequestQueue(getApplicationContext());
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("woNumber", wonumber.getText().toString());
                        params.put("woApprovedByName", cust_name.getText().toString());
                        params.put("woApprovedByContactNumber", work_order_list.getApprovedByContactNumber());
                        params.put("woApprovedByContactEmailId", "");
                        params.put(keyCodes.USERID, sharedPreferences.getString(keyCodes.USERID, ""));
                        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        LoginResponse loginResponse = new Gson().fromJson(response.toString(), LoginResponse.class);
                                        try {
                                            if (loginResponse.getSuccess()) {
                                                showToast(loginResponse.getMessage());
                                                for_otp.setVisibility(View.VISIBLE);
                                                otp.setText("Resend OTP");

                                            } else {
                                                TastyToast.makeText(getApplicationContext(), loginResponse.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //Process os success response

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("Error: ", error.getMessage());
                                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        queue.add(request_json);
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }

            if (otp.getText().toString().equals("Resend OTP")) {
                for_otp.setVisibility(View.VISIBLE);
                final AlertDialog.Builder builder = new AlertDialog.Builder(WorkOrderView.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertdialogue, null);
                builder.setView(dialogView);
                TextView editText = (TextView) dialogView.findViewById(R.id.dialgue);
                editText.setText(work_order_list.getResendPopUpmessage());
                TextView ok = (TextView) dialogView.findViewById(R.id.otp_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        final String URL = APIURL.baseurl + "reSendgenerateCustomerApprovalCode.mobile";
                        queue = Volley.newRequestQueue(getApplicationContext());
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("woNumber", wonumber.getText().toString());
                        params.put("woApprovedByName", cust_name.getText().toString());
                        params.put("woApprovedByContactNumber", work_order_list.getApprovedByContactNumber());
                        params.put("woApprovedByContactEmailId", "");
                        params.put(keyCodes.USERID, sharedPreferences.getString(keyCodes.USERID, ""));

                        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        LoginResponse loginResponse = new Gson().fromJson(response.toString(), LoginResponse.class);
                                        try {
                                            if (loginResponse.getSuccess()) {
                                                showToast(loginResponse.getMessage());


                                            } else {
                                                TastyToast.makeText(getApplicationContext(), loginResponse.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //Process os success response
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("Error: ", error.getMessage());
                                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        queue.add(request_json);
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }

        }

    });
      verify.setOnClickListener(new View.OnClickListener()
      {
          @Override
          public void onClick(View v) {
              RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
              String myotp1=get_otp.getText().toString();
              String myotp=APIURL.baseurl + "verifyCustomerApprovalCode.mobile?woNumber="+
                      wonumber.getText().toString()+
                      "&otp="+myotp1;
              JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, myotp , null,
                      new Response.Listener<JSONObject>()
                      {
                          @Override
                          public void onResponse(JSONObject response) {

                              LoginResponse loginResponse = new Gson().fromJson(response.toString(), LoginResponse.class);
                              try {
                                  if (loginResponse.getSuccess()) {


                                      get_otp.onEditorAction(EditorInfo.IME_ACTION_DONE);
                                  showToast(loginResponse.getMessage());
                                     for_approve.setVisibility(View.VISIBLE);

                                  }
                                  else
                                      {
                                      TastyToast.makeText(getApplicationContext(), loginResponse.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                                  }

                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          }
                      },
                      new Response.ErrorListener()
                      {
                          @Override
                          public void onErrorResponse(VolleyError error) {
                              Log.d("Error.Response", error.toString());
                          }
                      }
              );
              queue.add(getRequest);

          }
      });

        logout = (ImageView) findViewById(R.id.img_logout);
        findViewById(R.id.phonecall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + work_order_list.getRequestContact()));
                startActivity(intent);
            }
        });


        findViewById(R.id.reopen).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {

                if (sign==null) {
                    TastyToast.makeText(getApplicationContext(),"please add signature",
                            TastyToast.LENGTH_LONG,TastyToast.ERROR);
                }
                   else if (Objects.equals(comments.getText().toString(), "")) {
                        TastyToast.makeText(getApplicationContext(), "Fill details", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                    else
                        {
                        String returndata = signatureUpload(WorkOrderView.this, work_order_list,sign);
                        Log.i("Test", "onClick: " + returndata);
                        String url = APIURL.baseurl + "customerapproveWorkOrder.mobile";
                        queue = Volley.newRequestQueue(getApplicationContext());
                        Map<String, String> para = new HashMap<String, String>();
                        para.put("woNumber", wonumber.getText().toString());
                        para.put("approvedByUserName",approved_by.getText().toString());
                        para.put("approvedDateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        para.put("status", "CUSTOMER RE OPEN");
                        para.put("customerRemarks", comments.getText().toString());

                        JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(para),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        LoginResponse loginResponse = new Gson().fromJson(response.toString(), LoginResponse.class);
                                        try {
                                            if (loginResponse.getSuccess()) {
                                                finish();
                                                showToast(loginResponse.getMessage());
                                            } else {
                                                TastyToast.makeText(getApplicationContext(), loginResponse.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("Error: ", error.getMessage());
                                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        queue.add(request_json);
                    }
                }
        });

        findViewById(R.id.approve).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                if (sign != null) {

                    signView.setImageBitmap(sign);
                    String returndata = signatureUpload(WorkOrderView.this, work_order_list,sign);
                    Log.i("Test", "onClick: " + returndata);
                    LoginResponse response = new Gson().fromJson(returndata, LoginResponse.class);
                        String url =  APIURL.baseurl + "customerapproveWorkOrder.mobile";
                        queue = Volley.newRequestQueue(getApplicationContext());
                        Map<String, String> para = new HashMap<String, String>();
                        para.put("woNumber", wonumber.getText().toString());
                        para.put("approvedByUserName",approved_by.getText().toString());
                        para.put("approvedDateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        para.put("status", "CUSTOMER APPROVED");
                        para.put("customerRemarks",comments.getText().toString());

                        JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(para),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        LoginResponse loginResponse = new Gson().fromJson(response.toString(), LoginResponse.class);
                                        try {
                                            if (loginResponse.getSuccess()) {
                                                finish();
                                                showToast(loginResponse.getMessage());
                                            }
                                            else
                                            {
                                             TastyToast.makeText(getApplicationContext(), loginResponse.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                            }
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("Error: ", error.getMessage());
                                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        queue.add(request_json);
                }
                else
                {
                   TastyToast.makeText(getApplicationContext(), "Please add signature", TastyToast.LENGTH_LONG, TastyToast.ERROR);


                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                final PopupMenu popup = new PopupMenu(WorkOrderView.this, view);

                popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Logout");

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("SetTextI18n")
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == 0) {
                            final Dialog dialog1 = new Dialog(WorkOrderView.this);
                            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog1.setContentView(R.layout.dialog_cancelrequest);
                            Button okbtn = (Button) dialog1.findViewById(R.id.ok);
                            Button cnclbtn = (Button) dialog1.findViewById(R.id.cancel);
                            final TextView rply = (TextView) dialog1
                                    .findViewById(R.id.cancelRequestText);
                            // if button is clicked, close the custom dialog
                            rply.setText("Are you sure to logout?");
                            okbtn.setOnClickListener(
                                    new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    // TODO Auto-generated method stub
                                    dialog1.dismiss();
                                    Intent i = new Intent(WorkOrderView.this, Login.class);
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

        validation.hideKeyboard(this);
    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    private void AckViewOld(final Context context, final Work_order_list work_order_list) {
        if (isNetworkConnected()) {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = APIURL.baseurl + "acknowledgeWorkOrderMobile.mobile";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("getParams ACKView", response);

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
                    para.put(keyCodes.USERID, sharedPreferences.getString(keyCodes.USERID, ""));
                    para.put("woNumber", work_order_list.getWoNumber());
                    para.put("ackDateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS").format(new Date()));
                    return para;
                }

                @Override
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
    private void showToast(String message)
    {
        TextView textView;
        View view = getLayoutInflater().inflate(R.layout.toastview,null);
        textView= (TextView) view.findViewById(R.id.toast);
        textView.setText(message);
        Toast mToast = new Toast(getApplicationContext());
        mToast.setView(view);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }
    @SuppressLint("SimpleDateFormat")
    private void AckView(final Context context, final Work_order_list work_order_list) {
        if (isNetworkConnected()) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = APIURL.baseurl + "acknowledgeWorkOrderMobile.mobile";
            Log.i("Test", "approveWorkOrder: " + url);

            Map<String, String> para = new HashMap<String, String>();
            para.put(keyCodes.USERID, sharedPreferences.getString(keyCodes.USERID, ""));
            para.put("woNumber", work_order_list.getWoNumber());
            para.put("ackDateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            JSONObject objRegData = new JSONObject(para);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, url, objRegData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Test", "onResponse: ACK "+response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            TastyToast.makeText(getApplicationContext(), "Unknown Error", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    })
            {
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
    private String signatureUpload(final Context context, final Work_order_list work_order_list, Bitmap bitmap)
    {
        String retrundata = null;
        if (isNetworkConnected()) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            byte[] data = null;
            try {
                String url = APIURL.baseurl + "uploadUserSignature.mobile";
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                MultipartEntity entity = new MultipartEntity();

                if (bitmap != null) {

                    Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(newBitmap);
                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(bitmap, 0, 0, null);
                   ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    data = byteArrayOutputStream.toByteArray();
                    Random generator=new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String fname =  n +".jpg";
                    entity.addPart("signature ", new ByteArrayBody(data, fname ));
                }
                // entity.addPart("category", new StringBody(categoryname,"text/plain",Charset.forName("UTF-8")));
                entity.addPart("userId", new StringBody(sharedPreferences.getString(keyCodes.USERID, ""),
                        "text/plain", Charset.forName("UTF-8")));
                entity.addPart("woNumber", new StringBody(work_order_list.getWoNumber(),
                        "text/plain", Charset.forName("UTF-8")));
                entity.addPart("approvedByName",new StringBody(work_order_list.getApprovedByName()
                                    , "text/plain", Charset.forName("UTF-8")));
                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                return string;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return retrundata;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return retrundata;
            }
        }
        return retrundata;
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
    private void firebaseTokenIDUpdate() {
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
                            SharedPreferences.Editor editor= mypref.edit();
                            editor.remove("model");
                            editor.remove("user_type");
                            editor.apply();
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
