package com.hub.anyspot.utils;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hub.anyspot.R;

/**
 * Created by yeseul on 2018-03-30.
 */

public class CustomActionBar {
    private ActionBar actionBar;
    private View view;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private TextView textCenter;

    public CustomActionBar(AppCompatActivity activity, ActionBar actionBar){
        this.actionBar = actionBar;

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);     //액션바 아이콘을 업 네비게이션 형태로 표시
        actionBar.setDisplayShowTitleEnabled(false);    //액션바에 표시되는 제목의 표시유무 설정
        actionBar.setDisplayShowHomeEnabled(false);     //홈 아이콘 숨김처리

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.a00_action_bar, null);

        leftButton = view.findViewById(R.id.btn_actionbar_left);
        textCenter = view.findViewById(R.id.text_actionbar);
        rightButton = view.findViewById(R.id.btn_actionbar_right);

        actionBar.setCustomView(view); //커스텀 뷰 적용
        actionBar.setElevation(2);

        // 액션바 padding 없애기
        Toolbar parent = (Toolbar)view.getParent();
        parent.setContentInsetsAbsolute(0,0);
    }

    public View getView(){
        return view;
    }

    public void setView(View view){
        this.view = view;
    }


    public void setLeftButtonImage(int src){
        leftButton.setBackgroundResource(src);
        leftButton.setVisibility(View.VISIBLE);
    }

    public void setRightButtonImage(int src){
        rightButton.setImageResource(src);
        rightButton.setVisibility(View.VISIBLE);
    }

    public void removeLeftButton(){
        leftButton.setVisibility(View.GONE);
    }

    public void removeRightButton(){
        rightButton.setVisibility(View.GONE);
    }

    public void commit(){
        actionBar.setCustomView(view);
    }

    public ImageButton getRightButton() {
        return rightButton;
    }

    public ImageButton getLeftButton() {
        return leftButton;
    }

    public TextView getTextCenter() {
        return textCenter;
    }

    public void setTextCenter(String label) {
        textCenter.setText(label);
    }
}
