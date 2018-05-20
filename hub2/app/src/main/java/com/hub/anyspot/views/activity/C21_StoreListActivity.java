package com.hub.anyspot.views.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.hub.anyspot.R;
import com.hub.anyspot.utils.CustomActionBar;
import com.hub.anyspot.utils.HttpClient;
import com.hub.anyspot.vo.StoreFileVO;
import com.hub.anyspot.vo.StoreVO;
import com.hub.anyspot.utils.StoreListAdapter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class C21_StoreListActivity extends AppCompatActivity{
    private ArrayList<StoreVO> store_items = new ArrayList<>();

    private String category;
    private StoreListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c21_activity_storelist);

        Intent intent = new Intent(this.getIntent());
        category = intent.getStringExtra("category");
        String category_ko = intent.getStringExtra("category_ko");
        CustomActionBar customActionBar = new CustomActionBar(this, getSupportActionBar());
        customActionBar.setLeftButtonImage(R.drawable.icon_app_main);
        customActionBar.setTextCenter(category_ko);
        customActionBar.setLeftButtonImage(R.drawable.btn_actionbar_left_arrow);
        customActionBar.commit();
        (customActionBar.getLeftButton()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        storeList();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.enter_no_anim, R.anim.exit_no_anim);
        super.onBackPressed();
    }

    public void storeList(){
        String URL = "/android/store/list";
        RequestParams params = new RequestParams();
        params.add("cate_name", category);
        HttpClient.get(URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);
                try{
                    JSONArray array = new JSONArray(responseString);

                    for(int i = 0 ; i < array.length() ; i++){
                        JSONObject store = array.getJSONObject(i);

                        StoreVO svo = new StoreVO();
                        svo.setSt_key(store.getInt("st_key"));
                        svo.setSt_name(store.getString("st_name"));
                        svo.setSt_address(store.getString("st_address"));
                        svo.setSt_tell(store.getString("st_tell"));
                        svo.setSt_open_time(store.getString("st_open_time"));
                        svo.setSt_close_time(store.getString("st_close_time"));
                        svo.setSt_introduction(store.getString("st_introduction"));
                        svo.setOwnr_key(store.getInt("ownr_key"));
                        svo.setAtch_file_id(store.getInt("atch_file_id"));
//                        svo.setCurrent_spot(store.getInt("current_spot"));

                        store_items.add(svo);
                        Log.e("", svo.toString());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ListView listView = findViewById(R.id.list_store);
                adapter = new StoreListAdapter(C21_StoreListActivity.this, R.layout.storelist_item, store_items);
                listView.setAdapter(adapter);

                for(int i = 0 ; i < store_items.size() ; i++){
                    fileList(i, store_items.get(i).getSt_key());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void fileList(final int index, int atch_file_id){
        RequestParams params = new RequestParams("atch_file_id", atch_file_id);
        HttpClient.post("/android/store/file/selectFiles", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<StoreFileVO> list = new ArrayList<>();
                String result = new String(responseBody);
                try {
                    JSONArray array = new JSONArray(result);

                    for (int j = 0; j < array.length(); j++) {
                        JSONObject json = array.getJSONObject(j);
                        StoreFileVO vo = new StoreFileVO();
                        vo.setAtch_file_id(json.getInt("atch_file_id"));
//                        vo.setFile_sn(json.getInt("file_sn"));
                        int file_index = json.getInt("file_index");
                        vo.setFile_index(file_index);
                        vo.setFile_stre_url(json.getString("file_stre_url"));
                        vo.setStre_file_name(json.getString("stre_file_name"));
                        vo.setOrign_file_name(json.getString("orign_file_name"));
                        vo.setFile_extn(json.getString("file_extn"));

                        StringBuffer sb = new StringBuffer(json.getString("file_stre_url"));
                        sb.delete(0, 28);
                        vo.setFileURL(sb.toString());

                        if(file_index == 0) {
                            getImage(index, vo);
                        }
                        list.add(vo);
                    }
                }catch (Exception e){}

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void getImage(final int index, StoreFileVO vo){
        final String url = vo.getFileURL();
        HttpClient.getImage(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] fileData) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
                if(bitmap != null) {
                    setImageView(index, url, bitmap);
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    private void setImageView(int index, String url, Bitmap bitmap){
        adapter.addImage(index, url, bitmap);
        adapter.notifyDataSetChanged();
    }
}
