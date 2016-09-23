package com.apppartner.androidprogrammertest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
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

import static android.R.id.message;

public class LoginActivity extends ActionBarActivity
{
    EditText usernameET, passwordET;
    long startTime;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = (EditText) findViewById(R.id.login_username);
        passwordET = (EditText) findViewById(R.id.login_password);

    }

    public void onLoginButtonClicked(View view){
        if(validateFields()){
            sendPostRequest(usernameET.getText().toString(),passwordET.getText().toString());
        }
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

//                return "CODE : " + code +  " -- MESSAGE : " + message;

                long elapsedTime = System.currentTimeMillis() - startTime;
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                Log.d("abhishek",result);

                if(result.contains("Success")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(code+message+"Time taken:"+elapsedTime)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                finish();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
                }
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
}
