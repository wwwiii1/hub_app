package com.hub.anyspot.views.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hub.anyspot.R;
import com.hub.anyspot.utils.HttpClient;
import com.hub.anyspot.vo.UserVO;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class C40_ProfileFragment extends Fragment {
    private TextView text_user_id;
    private EditText text_user_phone;
    private EditText text_user_pw;

    private UserVO uvo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.c40_fragment_profile, container, false);
        Button btn_logout = view.findViewById(R.id.btn_logout);
        Button btn_change = view.findViewById(R.id.btn_change);
        text_user_id = view.findViewById(R.id.text_user_id);
        text_user_phone = view.findViewById(R.id.text_user_phone);
        text_user_pw = view.findViewById(R.id.text_user_pw);

        setProfileInfo();

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProfileInfo();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    public void setProfileInfo(){
        String URL = "/android/user/profile/settings";

        RequestParams params = new RequestParams("usr_id", text_user_id.getText());
        HttpClient.get(URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);

                try {
                    JSONObject result = new JSONObject(responseString);

                    text_user_id.setText(result.getString("usr_id"));
                    text_user_phone.setText(result.getString("usr_phone"));
                    text_user_pw.setText(result.getString("usr_password"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void getProfileInfo() {
        String URL = "/android/user/profile/update";
        RequestParams params = new RequestParams();
      //  params.add("usr_id",text_user_id.getText().toString());
        params.add("usr_phone", text_user_phone.getText().toString());
        params.add("usr_password", text_user_pw.getText().toString());
        params.setUseJsonStreamer(true);

        HttpClient.get(URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);
                try {
                    JSONObject result = new JSONObject(responseString);

                        Log.i("profile", "profile information");
                        JSONObject user = result.getJSONObject("user");

                        text_user_phone.setText(user.getString("usr_phone"));
                        text_user_pw.setText(user.getString("usr_password"));
                        Toast.makeText(getContext(), "user정보가 수정되었습니다.",Toast.LENGTH_SHORT).show();

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
