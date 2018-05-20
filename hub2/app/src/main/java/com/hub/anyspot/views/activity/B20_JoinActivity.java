package com.hub.anyspot.views.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hub.anyspot.R;
import com.hub.anyspot.utils.CustomActionBar;
import com.hub.anyspot.utils.HttpClient;
import com.hub.anyspot.vo.UserVO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class B20_JoinActivity extends AppCompatActivity{
    private EditText et_id;
    private EditText et_password;
    private EditText et_name;
    private EditText et_phone;
    private Button btn_overlap;
    private boolean checking = false;

    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b20_activity_join);

        CustomActionBar customActionBar = new CustomActionBar(this, getSupportActionBar());
        customActionBar.setLeftButtonImage(R.drawable.icon_app_main);
        customActionBar.setTextCenter("회원가입");
        customActionBar.commit();

        et_id = findViewById(R.id.et_id);
        et_password = findViewById(R.id.et_password);
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        btn_submit = findViewById(R.id.btn_submit);
        btn_overlap = findViewById(R.id.btn_overlap);

        btn_overlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overlap();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checking) {
                    join();
                }
                else{
                    Toast.makeText(getApplicationContext(), "id 중복체크하시오", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.enter_no_anim, R.anim.exit_no_anim);
        super.onBackPressed();
    }

    public void overlap(){
        String URL = "/android/user/overlap";
        RequestParams params = new RequestParams("usr_id", et_id.getText().toString());
        params.setUseJsonStreamer(true);

        HttpClient.get(URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);
                try {
                    JSONObject result = new JSONObject(responseString);
                    Boolean isSuccess = result.getBoolean("result");
                    if(isSuccess){
                        Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_SHORT).show();
                        checking = true;//아이디 중복 없음 확인
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"같은 아이디가 존재합니다",Toast.LENGTH_SHORT).show();
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

    public void join(){
        String URL = "/android/user/join";
        RequestParams params = new RequestParams();
        params.put("usr_id", et_id.getText().toString());
        params.put("usr_password", et_password.getText().toString());
        params.put("usr_name", et_name.getText().toString());
        params.put("usr_phone", et_phone.getText().toString());
        params.setUseJsonStreamer(true);

        HttpClient.get(URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "JOIN SUCCESS", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(B20_JoinActivity.this, B10_LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
