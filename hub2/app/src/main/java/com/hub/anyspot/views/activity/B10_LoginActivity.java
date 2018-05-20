package com.hub.anyspot.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hub.anyspot.R;
import com.hub.anyspot.utils.CustomActionBar;
import com.hub.anyspot.utils.HttpClient;
import com.hub.anyspot.vo.UserVO;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class B10_LoginActivity extends AppCompatActivity {
    private EditText et_id;
    private EditText et_password;
    private Button btn_login;
    private Button btn_join;
    private TextView tv_test;

    private UserVO uvo = new UserVO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b10_activity_login);

        CustomActionBar customActionBar = new CustomActionBar(this, getSupportActionBar());
        customActionBar.setLeftButtonImage(R.drawable.icon_app_main);
        customActionBar.setTextCenter("로그인");
        customActionBar.commit();

        et_id = findViewById(R.id.et_id);
        et_password = findViewById(R.id.et_password);
        tv_test = findViewById(R.id.tv_test);
        btn_login = findViewById(R.id.btn_login);
        btn_join = findViewById(R.id.btn_join);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(B10_LoginActivity.this, B20_JoinActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_no_anim, R.anim.exit_no_anim);
            }
        });
    }

    public void login() {
        String URL = "/android/user/login";
        RequestParams params = new RequestParams();
        params.put("usr_id", et_id.getText().toString());
        params.put("usr_password", et_password.getText().toString());
        params.setUseJsonStreamer(true);

        HttpClient.get(URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);
                try {
                    JSONObject result = new JSONObject(responseString);
                    Boolean isSuccess = result.getBoolean("result");

                    if (isSuccess == true) {
                        Log.i("login", "Login Success");
                        JSONObject user = result.getJSONObject("user");
                        uvo.setUsr_key(user.getInt("usr_key"));
                        uvo.setUsr_id(user.getString("usr_id"));
                        uvo.setUsr_name(user.getString("usr_name"));
                        uvo.setUsr_phone(user.getString("usr_phone"));

                        ////////////////////// 세션저장 //////////////////////

                        Intent intent = new Intent(B10_LoginActivity.this, C00_MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter_no_anim, R.anim.exit_no_anim);
                        finish();
                    } else {
                        String fail = result.getString("fail");
                        if ("id".equals(fail)) {
                            Toast.makeText(B10_LoginActivity.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(B10_LoginActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}