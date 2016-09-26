package com.apppartner.androidprogrammertest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends ActionBarActivity
{
    EditText usernameET, passwordET;
    long startTime;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = (EditText) findViewById(R.id.login_username);
        passwordET = (EditText) findViewById(R.id.login_password);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/machinatoLight.ttf");
        usernameET.setTypeface(custom_font);
        passwordET.setTypeface(custom_font);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");

    }

    public void onLoginButtonClicked(View view){
        if(validateFields()){
            if(isNetworkAvailable()) sendPostRequest(usernameET.getText().toString(),passwordET.getText().toString());
            else Toast.makeText(this,"You need internet to do this action!", Toast.LENGTH_LONG).show();

        }
    }

    //check if internet is available
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;

    }

    private Boolean validateFields(){
        if(TextUtils.isEmpty(usernameET.getText())){
            Toast.makeText(this,"Please enter username",Toast.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(passwordET.getText())){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private void sendPostRequest(String username, String givenPassword) {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            Context context;

            public SendPostReqAsyncTask(Context mContext) {
                this.context = mContext;
            }

            @Override
            protected String doInBackground(String... params) {

                // Create a new HttpClient and Post Header
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://dev3.apppartner.com/AppPartnerDeveloperTest/scripts/login.php");

                BufferedReader in = null;
                try {
                    //add data
                    startTime = System.currentTimeMillis();
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("username", params[0]));
                    nameValuePairs.add(new BasicNameValuePair("password",params[1]));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    //execute http post
                    HttpResponse response = httpclient.execute(httppost);

                    final String responseString = EntityUtils.toString(response.getEntity());

                    return responseString;


                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                String code = null;
                String message = null;
                try {
                    JSONObject json = new JSONObject(result);
                    code = json.getString("code");
                    message = json.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                long elapsedTime = System.currentTimeMillis() - startTime;

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                if(result.contains("Success")){
                builder.setMessage("CODE: "+code+". \nMessage: "+message+"\nTime:"+elapsedTime)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                finish();
                            }
                        });

                }else{
                    builder.setTitle("Wrong Credentials. Try again!")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    usernameET.setText("");
                                    passwordET.setText("");
                                }
                            });
                }
                final AlertDialog alert = builder.create();
                alert.show();
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask(this);
        sendPostReqAsyncTask.execute(username, givenPassword);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
