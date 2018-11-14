package com.develop.app.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.develop.app.myapplication.API.APIURL;
import com.develop.app.myapplication.common.KeyCodes;
import com.develop.app.myapplication.model.LoginResponse;
import com.develop.app.myapplication.model.Module;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class Login extends AppCompatActivity {
    public static final String My_PREF = "nh";
    LinearLayout layout;
    TextView privacy;

    Button loginb;
    EditText emailtxt, passtxt;
    RequestQueue queue;
    String mesg, username, password, message, moduleName, submoduleName;
   SharedPreferences sharedPreferences;
    KeyCodes keyCodes=KeyCodes.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layout=(LinearLayout)findViewById(R.id.my);
        sharedPreferences=getSharedPreferences(My_PREF, MODE_PRIVATE);
        loginb = (Button) findViewById(R.id.loginb);
        emailtxt = findViewById(R.id.emailtxt);
        passtxt = findViewById(R.id.passtxt);
        privacy =(TextView)findViewById(R.id.privacy);
        forceUpdate();

        if (getSharedPreferences(My_PREF,0).getBoolean("isLoginKey",false))
        {

            startActivity(new Intent(Login.this, DashboardModule.class));
            finish();
        }

        else

        {

        //     emailtxt.setText(sharedPreferences.getString("userName",""));
        //  passtxt.setText(sharedPreferences.getString("password",""));
            login();
        }
        privacy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);
                LayoutInflater inflater=getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.webview, null);
                builder.setView(dialogView);
                WebView browser = (WebView)dialogView. findViewById(R.id.webview);
                browser.getSettings().setJavaScriptEnabled(true);
                browser.setWebChromeClient(new WebChromeClient());
                browser.loadUrl("http://cmms.narayanahealth.org/NH/privacyPolicy.html");
                AlertDialog alertDialog;

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                alertDialog=builder.create();
                alertDialog.show();
            }
        });
        loginb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
               if (emailtxt.getText().toString().isEmpty()) {
                    TastyToast.makeText(getApplicationContext(), "User Name is Empty", TastyToast.LENGTH_LONG, TastyToast.WARNING);

                } else if (passtxt.getText().toString().isEmpty()) {
                    TastyToast.makeText(getApplicationContext(), "Password is Empty", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                }

                else
                    {
                   login();
               }

            }
        });
    }
    public void login()
    {
        username = emailtxt.getText().toString().trim();
        password = passtxt.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        final String URL = APIURL.baseurl +"authenticateUser.mobile";
        queue = Volley.newRequestQueue(this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userName", username);
        params.put("password", password);
        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Test", "onResponse:--> " + response);
                        progressDialog.dismiss();
                        LoginResponse loginResponse = new Gson().fromJson(response.toString(), LoginResponse.class);
                        try {
                            if (loginResponse.getSuccess()) {
                                Type listType = new TypeToken<List<Module>>() {}.getType();
                                try
                                {
                                    if (emailtxt.getText().toString().trim().length()>0)
                                    {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("isLoginKey",true);
                                        editor.putString("userName", emailtxt.getText().toString());
                                        editor.putString("password", passtxt.getText().toString());
                                        editor.putString(keyCodes.USERID, loginResponse.getUserId() + "");
                                        editor.putString(keyCodes.SUBMODULENAME, new Gson().toJson(loginResponse.getModules(),listType));
                                        Intent i = new Intent(Login.this, DashboardModule.class);
                                        layout.setVisibility(View.GONE);
                                        //i.putExtra(keyCodes.SUBMODULENAME,new Gson().toJson(loginResponse.getModules(), listType));
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        editor.apply();
                                    }
                                    else
                                    {
                                        TastyToast.makeText(getApplicationContext(), "Error", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                                    }


                                }
                                catch (NullPointerException e)
                                {
                                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
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
    public void forceUpdate()
    {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo =  packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        new com.develop.app.myapplication.List(currentVersion,Login.this).execute();
    }
}
