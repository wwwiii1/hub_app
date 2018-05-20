package com.hub.anyspot.views.fragment;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hub.anyspot.R;
import com.hub.anyspot.utils.APIGoogleGPA;
import com.hub.anyspot.utils.GpsInfo;
import com.hub.anyspot.utils.cList;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapTapi;

import java.util.ArrayList;

/**
 * 첫 번째 탭 화면
 * 구글 지도 API 받아오기
 */
public class C10_MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "googlemap";
    private LocationRequest mLocationRequest;
    private static final int REQUEST_CODE_GPS = 2001;

    private static final int DENIED = 100;

    private GoogleMap googleMap;

    APIGoogleGPA places;
    Marker curMarker;
    GpsInfo gps;
    PolylineOptions polylineOptions;

/*
    public static C10_MapFragment newInstance() {
        C10_MapFragment fragment = new C10_MapFragment();
        Bundle args = new Bundle();

        Log.e("","newInstance");
        fragment.setArguments(args);
        return fragment;
    }*/

    SupportMapFragment fragment;
    ViewGroup rootView;
    RelativeLayout rl;
    ArrayList<TMapPoint> arrayPoint;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        Log.e("","onCreateView");

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null){
                parent.removeView(rootView);
            }
        }

        try {
            rootView = (ViewGroup) inflater.inflate(R.layout.c10_fragment_map, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }

        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
            fragment.getMapAsync(this);
        }

        rl = (RelativeLayout)rootView.findViewById(R.id.itemDetail);

        return rootView;

/*        View view = inflater.inflate(R.layout.c10_fragment_map, container, false);

        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
            fragment.getMapAsync(this);
        }

        rl = (RelativeLayout) view.findViewById(R.id.itemDetail);

        return view;*/
    }

    /**
     * Map 클릭시 터치 이벤트
     */
    public void onMapClick(LatLng point) {

        // 현재 위도와 경도에서 화면 포인트를 알려준다
        Point screenPt = googleMap.getProjection().toScreenLocation(point);

        // 현재 화면에 찍힌 포인트로 부터 위도와 경도를 알려준다.
        LatLng latLng = googleMap.getProjection().fromScreenLocation(screenPt);

        Log.d("맵좌표", "좌표: 위도(" + String.valueOf(point.latitude) + "), 경도("
                + String.valueOf(point.longitude) + ")");
        Log.d("화면좌표", "화면좌표: X(" + String.valueOf(screenPt.x) + "), Y("
                + String.valueOf(screenPt.y) + ")");
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //권한체크
            return;
        }
        Log.e("onMapReady ", "onMapReady");

        googleMap = map;

        // T map 앱 연동
        TMapTapi tmaptapi = new TMapTapi(getContext());
        tmaptapi.setSKPMapAuthentication("9ccf3adc-6564-3b8d-9462-833a25492a9c");
        tmaptapi.setOnAuthenticationListener(new TMapTapi.OnAuthenticationListenerCallback() {
            @Override
            public void SKPMapApikeySucceed() {
                Log.d("", "락스크린 SKPMapApikeySucceed");
            }

            @Override
            public void SKPMapApikeyFailed(String s) {
                Log.d("", "락스크린 SKPMapApikeyFailed" + s);
            }

            @Override
            public void SKPMapBizAppIdSucceed() {
                Log.d("락스크린 ", "SKPMapBizAppIdSucceed");

            }

            @Override
            public void SKPMapBizAppIdFailed(String s) {

                Log.d("락스크린 ", "SKPMapBizAppIdFailed" + s);
            }
        });
        //현재위치 주변 카테고리 위치검색(초기값 - 카페,베이커리,편의점 전체)
        SerachToTalMaker();

        googleMap.setMyLocationEnabled(false);//현재위치 주기적으로 나타냄
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //현재 위치로 가는 버튼 표시
        map.setMyLocationEnabled(true);
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom( SEOUL, 15));//초기 위치...수정필요

        //마커클릭리스너 구글맵에 경로를 그려준다
        setOnMarkerClickListener();

    }

    private Marker addMarker(cList Item) {
        LatLng position = new LatLng(Item.getLat(), Item.getLon());
        String name = Item.getname();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(name);
        markerOptions.position(position);

        //마커아이콘 설정
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));


        return googleMap.addMarker(markerOptions);

    }

    //마커아이콘 리사이즈
    public Bitmap resizeMapIcons(int icon, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), icon);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    LatLng GPSlatLng;


    //현재위치 가져오기
    public void GPS_getcurrentpoint() {
        //구글 현재위치 정보가져오기
        gps = new GpsInfo(getContext());
        // GPS 사용유무 가져오기
        Log.d("락스크린 구글맵 프래그먼트 마커", gps.isGetLocation() + "");
        if (gps.isGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // Creating a LatLng object for the current location
            GPSlatLng = new LatLng(latitude, longitude);

            // Showing the current location in Google Map
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(GPSlatLng));

            // Map 을 zoom 합니다.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

            // 마커 설정.
            MarkerOptions optFirst = new MarkerOptions();
            optFirst.position(GPSlatLng);// 위도 • 경도
            optFirst.title("현재위치");// 제목 미리보기
            //optFirst.snippet("snippet");


            //현재위치마커 아이콘 설정
            optFirst.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


            //optFirst.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.current, 200,200)));
            if (curMarker != null) curMarker.remove();
            curMarker = googleMap.addMarker(optFirst);
            curMarker.showInfoWindow();
            Log.d("락스크린 구글맵 마커설정 성공", "위도 : " + gps.getLatitude() + " / 경도 : " + gps.getLongitude());
        } else {
            //gps정보를 가져오지 못했을 때 설정창으로 이동
            gps.showSettingsAlert();
            Log.d("락스크린 구글맵 프래그먼트 마커설정 실패", gps.isGetLocation() + "");
        }
    }


    //마커클릭리스너
    public void setOnMarkerClickListener() {

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            public boolean onMarkerClick(final Marker marker) {
                //상세내용 하단에 보이기
                rl.setVisibility(View.VISIBLE);


                String markertitle = marker.getTitle();
                SerachToTalMaker();// 맵초기화 마커 카테고리전체 다시찍기
                final String text = "latitude ="
                        + marker.getPosition().latitude + "\nlongitude ="
                        + marker.getPosition().longitude;
//                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT)
//                        .show();

                marker.showInfoWindow();//마커 정보창 보이기
                //APIRequest.setAppKey("##APPKEY_INPUTHERE##");

                //POI 검색, 경로검색 등의 지도데이터를 관리하는 클래스
                TMapData tmapdata = new TMapData();

                arrayPoint = null;
                //출발지 목적지 위도,경도 설정 - 티맵
                TMapPoint startpoint = new TMapPoint(gps.getLatitude(), gps.getLongitude());
                TMapPoint endpoint = new TMapPoint(marker.getPosition().latitude, marker.getPosition().longitude);

                Log.d("락스크린 startpoint", gps.getLatitude() + "/" + gps.getLongitude());
                Log.d("락스크린 endpoint", marker.getPosition().latitude + "/" + marker.getPosition().longitude);
                tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startpoint, endpoint,//보행자경로찾기,출발지,목적지
                        new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine polyLine) {
                                Log.d("락스크린 onFindPathData", "");
                                arrayPoint = polyLine.getLinePoint();
                                final double distance = polyLine.getDistance(); // double 이동거리를 리턴


                                //mMapView.addTMapPath(polyLine); 티맵에 경로그리기
                                //구글맵에 경로그리기
                                LatLng startLatLng = new LatLng(gps.getLatitude(), gps.getLongitude());
                                polylineOptions = new PolylineOptions();
                                polylineOptions.width(20).color(Color.RED).add(startLatLng);
                                for (int i = 0; i < arrayPoint.size(); ++i) {
                                    TMapPoint tMapPoint = arrayPoint.get(i);
                                    LatLng point = new LatLng(tMapPoint.getLatitude(), tMapPoint.getLongitude());
                                    polylineOptions.add(point);
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //... UI 업데이트 작업
//                                        Toast.makeText(getContext(),"이동거리 : "+String.valueOf((int)distance), Toast.LENGTH_SHORT)
//                                                .show();
                                        //googleMap.clear();// 경로를지워준다
                                        googleMap.addPolyline(polylineOptions);

                                        //도착지 마커 덧붙이기
                                        LatLng position = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                                        String name = marker.getTitle();
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.title(name);
                                        String km = String.valueOf((int) distance);

                                        markerOptions.snippet("이동거리 : " + km + "m");
                                        markerOptions.position(position);
                                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                        //m0arkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.end));//도착지마커아이콘
                                        //마커아이콘 설정
                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cast_off_light));
                                        googleMap.addMarker(markerOptions).showInfoWindow();

                                        //출발지 마커 생성
//                                        MarkerOptions mOptions = new MarkerOptions();
//                                        mOptions.title("현재위치");
//                                        mOptions.position(GPSlatLng);
//                                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//                                        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.end));//도착지마커아이콘
//                                        mOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.start, 200,200)));
//                                        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cast_off_light));
//                                        googleMap.addMarker(mOptions).showInfoWindow();
                                    }
                                });
                            }
                        });

                return false;
            }
        });
    }

    //현재위치 주변 편의점,카페,베이커리 검색
    public void SerachToTalMaker() {
        //(final String endtitle)
        //현재위치 주변 편의점,카페,베이커리 검색
        googleMap.clear();
        GPS_getcurrentpoint();//현재위치 마커
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (gps.isGetLocation()) {
                    String next_page_token = "";
                    //do{
                    Log.d("락스크린 구글맵 편의점 검색 스레드", "시작");
                    places = new APIGoogleGPA(getContext(), gps.getLatitude(), gps.getLongitude(), 1000, "grocery_or_supermarket", next_page_token);
                    places.parsing();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for (int i = 0; i < APIGoogleGPA.mList.size(); i++) {
                                addMarker(APIGoogleGPA.mList.get(i));
                            }
                            Log.d("락스크린 구글맵 편의점 검색 스레드", "끝");
                        }
                    });
                    //next_page_token = "&next_page_token="+places.parsing(); //마커 추가후 다음페이지토큰 리턴


                    //현재위치 주변 베이커리 검색
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (gps.isGetLocation()) {
                                //cGPlacesAPI.mList.clear();
                                Log.d("락스크린 구글맵 베이커리 검색 스레드", "시작");
                                String add = "keyword=파리&";
                                places = new APIGoogleGPA(getContext(), gps.getLatitude(), gps.getLongitude(), 1000, "", add);
                                places.parsing();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        for (int i = 0; i < APIGoogleGPA.mList.size(); i++) {
                                            addMarker(APIGoogleGPA.mList.get(i));
                                        }
                                        Log.d("락스크린 구글맵 베이커리 검색 스레드", "끝");
                                    }
                                });

                                //현재위치 주변 카페 검색
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (gps.isGetLocation()) {
                                            //cGPlacesAPI.mList.clear();
                                            Log.d("락스크린 구글맵 베이커리 검색 스레드", "시작");
                                            places = new APIGoogleGPA(getContext(), gps.getLatitude(), gps.getLongitude(), 1000, "cafe", "");
                                            places.parsing();

                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

//                                                    for (cList Item : cGPlacesAPI.mList) {
//                                                        addMarker(Item);
//                                                    }

                                                    for (int i = 0; i < APIGoogleGPA.mList.size(); i++) {
                                                        Marker marker = addMarker(APIGoogleGPA.mList.get(i));
//                                                        if(endtitle.equals(cGPlacesAPI.mList.get(i).getname())){
//
//                                                        }
                                                    }


                                                    Log.d("락스크린 구글맵 베이커리 검색 스레드", "끝");
                                                }
                                            });
                                        }
                                    }
                                }).start();
                            }
                        }
                    }).start();
                }
            }
        }).start();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String markerId = marker.getId();
        Log.e("", "marker.getId()" + marker.getId());
    }

}
