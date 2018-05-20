package com.hub.anyspot.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.hub.anyspot.R;
/**
 * Created by LYS on 2017-11-27.
 */

public class APIGoogleGPA {

    Context mContext;
    StringBuilder mResponseBuilder = new StringBuilder();
    Float color;
    public static LinkedList<cList> mList = new LinkedList<>();

    public APIGoogleGPA(Context _con, double _lat, double _lon, double _radius, String _type, String next_page_token)
    {
        mContext = _con;
        try
        {
            String uStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"+next_page_token+"location=" + _lat + "," + _lon + "&radius=" + _radius + "&types=" + _type + "&key=AIzaSyBiAmqEiGZE9dzVfsudC3n3dFl85FK4O-0";
            URL url = new URL(uStr);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
            {
                mResponseBuilder.append(inputLine);
            }
            in.close();
        }
        catch (MalformedURLException me)
        {
            me.printStackTrace();
        }
        catch (UnsupportedEncodingException ue)
        {
            ue.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Log.d("락스크린 cGPlacesAPI생성자 ", "");
    }
    String next_page_token;
    public String parsing()
    {
        next_page_token="";
        try
        {
            JSONArray jArr;
            JSONObject jObj;

            jObj            = new JSONObject(mResponseBuilder.toString());
            jArr            = jObj.getJSONArray("results");
//        if(jObj.getString("next_page_token")!=null){
//            next_page_token = jObj.getString("next_page_token");
//        }

            Log.d("락스크린", " parsing() next_page_token"+next_page_token);

            for (int i = 0; i < jArr.length(); i++)
            {
                // 결과별로 결과 object 얻기
                JSONObject result = jArr.getJSONObject(i);

                // 위도, 경도 얻기
                JSONObject geo = result.getJSONObject("geometry");
                JSONObject location = geo.getJSONObject("location");
                String sLat = location.getString("lat");
                String sLon = location.getString("lng");

                // 이름 얻기
                String name = result.getString("name");


                //결과상태
//            String status = result.getString("status");
//            if(status.equals("ZERO_RESULTS")) Toast.makeText(mContext,"현재 주변에 위치한 정보가 없습니다",Toast.LENGTH_SHORT).show();

                //다음페이지 20개 요청토큰
                //  next_page_token = result.getString("next_page_token");

                // Rating 얻기
                String rating = "0";
                if (result.has("rating") == true)
                    rating  = result.getString("rating");

                if(name.contains("세븐일레븐")||name.contains("GS25")||name.contains("CU")) {
                    //편의점
                    color = BitmapDescriptorFactory.HUE_ORANGE;   name = "Strike 500  16/40";
                    mList.add(new cList(Float.valueOf(rating), Double.valueOf(sLat), Double.valueOf(sLon), name, color, R.drawable.marker10));
                }else if(name.contains("이디야")){
                    //카페
                    color = BitmapDescriptorFactory.HUE_CYAN;   name = "Strike 500  16/40";
                    mList.add(new cList(Float.valueOf(rating), Double.valueOf(sLat), Double.valueOf(sLon), name, color,R.drawable.marker10));
                }else if(name.contains("파리바게뜨")||name.contains("빠리바게뜨")){
                    //베이커리

                    color = BitmapDescriptorFactory.HUE_VIOLET;   name = "Strike 500  16/40";
                    mList.add(new cList(Float.valueOf(rating), Double.valueOf(sLat), Double.valueOf(sLon), name, color,R.drawable.marker10));
                }else{
                    //기타
//                color = BitmapDescriptorFactory.HUE_AZURE;
//                mList.add(new cList(Float.valueOf(rating), Double.valueOf(sLat), Double.valueOf(sLon), name, color,R.drawable.bakery_marker));
                }



                Log.d("락스크린 구글맵 카테고리 위치가져오기 ",sLat+" / "+sLon+"\n"+rating+" "+name);


            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return next_page_token;
    }

    public LinkedList<cList> getList()
    {
        return mList;
    }
}

