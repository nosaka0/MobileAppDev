package com.example.midterm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MIDTERM_LOGIN";
    EditText emailET;
    EditText passwordET;
    Button loginBtn;
    Button registerBtn;

    Handler handler;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        handler = new Handler(Looper.getMainLooper());
        context = this;
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        emailET = findViewById(R.id.loginEmailET);
        passwordET = findViewById(R.id.loginPassET);
        loginBtn = findViewById(R.id.loginButton);
        registerBtn = findViewById(R.id.loginRegisterButton);

        if(isConnected()){
            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_LONG).show();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                final OkHttpClient client = new OkHttpClient();

                String url = getString(R.string.baseURL) + "auth/login";

                RequestBody body = new FormBody.Builder()
                        .add("email", email)
                        .add("password", password)
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                    @Override public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (response.isSuccessful()){
                                Log.d(TAG, "onResponse: body" + responseBody);
                                JSONObject root = new JSONObject(responseBody.string());
                                String token = root.getString("token");

                                Log.d(TAG, "onResponse: login token: " + token);

                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(getString(R.string.authToken), token);
                                editor.commit();

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent toMyNotes = new Intent(LoginActivity.this, MyNotesActivity.class);
                                        Toast.makeText(context, "Logging in...", Toast.LENGTH_SHORT).show();
                                        startActivity(toMyNotes);
                                        finish();
                                    }
                                });
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(toRegister);
                Log.d(TAG, "onClick: sending to register activity");
                finish();
            }
        });

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected() &&
                         (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                         || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

}
