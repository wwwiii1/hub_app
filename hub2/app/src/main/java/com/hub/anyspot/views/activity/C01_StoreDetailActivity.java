package com.hub.anyspot.views.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hub.anyspot.R;
import com.hub.anyspot.utils.CustomActionBar;
import com.hub.anyspot.utils.HttpClient;
import com.hub.anyspot.vo.StoreFileVO;
import com.hub.anyspot.vo.StoreVO;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class C01_StoreDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private StoreVO sdVO = new StoreVO();

    /* 예약하기 onClick에 넣기위해서 전역으로*/
    TimePicker mTimePicker;
    ImageView mainImage;

    int[] currentDraw = {
            R.drawable.current_1, R.drawable.current_2, R.drawable.current_3, R.drawable.current_4, R.drawable.current_5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c01_activity_storedetail);

        Intent intent = getIntent();
        sdVO = intent.getParcelableExtra("storeVO");
        String url = intent.getStringExtra("fileURL");

        getImage(url);

        CustomActionBar customActionBar = new CustomActionBar(this, getSupportActionBar());
        customActionBar.setTextCenter(sdVO.getSt_name());
        customActionBar.setLeftButtonImage(R.drawable.btn_actionbar_left_arrow);
        customActionBar.setRightButtonImage(R.drawable.img_actionbar_heart);
        customActionBar.commit();
        (customActionBar.getLeftButton()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.enter_no_anim, R.anim.exit_no_anim);
            }
        });

        mainImage = findViewById(R.id.img_store_detail);
        TextView tv_store_name = findViewById(R.id.tv_store_name);
        TextView tv_store_addr = findViewById(R.id.tv_store_addr);
        TextView tv_store_time = findViewById(R.id.tv_store_time);
        TextView tv_store_like = findViewById(R.id.tv_store_like);
        final Button tv_store_tell = findViewById(R.id.tv_store_tell);
        TextView tv_store_intro = findViewById(R.id.tv_store_intro);
        ImageView img_current = findViewById(R.id.current_status);
        Button btn_reserve = findViewById(R.id.btn_reserve);
        Button btn_like = findViewById(R.id.btn_like);
        mTimePicker = findViewById(R.id.timePicker2);

        tv_store_name.setText(sdVO.getSt_name());
        tv_store_addr.setText(sdVO.getSt_address());
        tv_store_time.setText(sdVO.getSt_open_time()+"PM ~ "+sdVO.getSt_close_time()+"AM");
        tv_store_tell.setText(sdVO.getSt_tell());
        tv_store_intro.setText(sdVO.getSt_introduction());

        tv_store_like.setText((int) (Math.random()*200) +"");
        int num = (int) (Math.random()*5);
        img_current.setImageDrawable(getResources().getDrawable(currentDraw[num]));

        btn_reserve.setOnClickListener(this);
        tv_store_tell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.CALL", Uri.parse(tv_store_tell.getText().toString())));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.enter_no_anim, R.anim.exit_no_anim);
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {

        /////////////////// 예약하기 ////////////////////////
        /* timepicker로부터 현재 시간 받기, 버전따라 함수 다르다*/
        int hour,min;
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            hour = mTimePicker.getHour();
            min = mTimePicker.getMinute();
            Toast.makeText(getApplicationContext(), "now your time is : "+ hour+"시 "+min+"분 입니다.",Toast.LENGTH_SHORT).show();
        }else{
            hour = mTimePicker.getCurrentHour();
            min = mTimePicker.getCurrentMinute();
            Toast.makeText(getApplicationContext(), "now your time is : "+ hour+"시 "+min+"분 입니다.",Toast.LENGTH_SHORT).show();
        }

    }

    private void getImage(final String url){
        HttpClient.getImage(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] fileData) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
                if(bitmap != null) {
                    mainImage.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

}
