package com.hub.anyspot.views.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hub.anyspot.R;
import com.hub.anyspot.utils.CustomActionBar;
import com.hub.anyspot.views.fragment.C10_MapFragment;
import com.hub.anyspot.views.fragment.C20_CategoryFragment;
import com.hub.anyspot.views.fragment.C30_FavoriteFragment;
import com.hub.anyspot.views.fragment.C40_ProfileFragment;

public class C00_MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final int[] BUTTONS_LAYOUT = { // 탭 버튼 레이아웃
            R.id.btn_tab1,
            R.id.btn_tab2,
            R.id.btn_tab3,
            R.id.btn_tab4
    };
    private final int[] BUTTONS_IMG = { // 탭 버튼 이미지 url
            R.drawable.btn_locate_tab,
            R.drawable.btn_category_tab,
            R.drawable.btn_fav_tab,
            R.drawable.btn_profile_tab
    };
    private final int[] BUTTONS_IMG_CLICKED = { // 탭 버튼 이미지 url
            R.drawable.btn_locate_tab_clicked,
            R.drawable.btn_category_tab_clicked,
            R.drawable.btn_fav_tab_clicked,
            R.drawable.btn_profile_tab_clicked
    };
    private final String[] ACTIONBAR_TEXT = {
            "위치", "카테고리", "즐겨찾기", "프로필"
    };

    private final int NUM_OF_BUTTONS = 4; // 탭 버튼 개수
    private Button[] tabButtons = new Button[NUM_OF_BUTTONS]; // 탭 버튼 배열
    private int current_tab = 0; // 현재 탭
    private ViewPager pager;
    private CustomActionBar customActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c00_activity_main);

        customActionBar = new CustomActionBar(this, getSupportActionBar());
        customActionBar.setTextCenter("위치");
        customActionBar.commit();

        for(int i = 0 ; i < NUM_OF_BUTTONS ; i++){
            tabButtons[i] = (Button) findViewById(BUTTONS_LAYOUT[i]);
            tabButtons[i].setOnClickListener(this);
            tabButtons[i].setTag(i);
        }

        setViewPager();
    }

    public void setViewPager(){
        pager = (ViewPager)findViewById(R.id.fragment_container);
        pager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switchBtnColor(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void switchBtnColor(int tag){
        if(tag != current_tab){
            tabButtons[current_tab].setBackgroundResource(BUTTONS_IMG[current_tab]);
            tabButtons[tag].setBackgroundResource(BUTTONS_IMG_CLICKED[tag]);
            current_tab = tag;
        }
        switch (tag){
            case 0:
                customActionBar.setTextCenter("위치");
                break;
            case 1:
                customActionBar.setTextCenter("카테고리");
                break;
            case 2:
                customActionBar.setTextCenter("즐겨찾기");
                break;
            case 3:
                customActionBar.setTextCenter("프로필");
                break;
        }
        customActionBar.commit();
    }

    private class pagerAdapter extends FragmentStatePagerAdapter{

        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = new C20_CategoryFragment();
//                    fragment = new C10_MapFragment();
                    break;
                case 1:
                    fragment = new C20_CategoryFragment();
                    break;
                case 2:
                    fragment = new C30_FavoriteFragment();
                    break;
                case 3:
                    fragment = new C40_ProfileFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_OF_BUTTONS;
        }
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, C00_MainActivity.class);
        return intent;
    }

    @Override
    public void onClick(View view) {
        int tag = (int) view.getTag();
        switchBtnColor(tag);
        pager.setCurrentItem(tag);
    }

}
