package com.hub.anyspot.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hub.anyspot.R;
import com.hub.anyspot.vo.StoreVO;
import com.hub.anyspot.views.activity.C01_StoreDetailActivity;

import java.util.ArrayList;

/**
 * Created by LYS on 2017-11-12.
 */

public class StoreListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<StoreVO> store_items = new ArrayList<>();
    LayoutInflater inflater;
    private Bitmap[] bitmaps;
    private String[] URL;

    public StoreListAdapter(Context context, int layout, ArrayList<StoreVO> store_items) {
        this.context = context;
        this.layout = layout;
        this.store_items = store_items;
        bitmaps = new Bitmap[store_items.size()];
        URL = new String[store_items.size()];
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return store_items.size();
    }

    @Override
    public Object getItem(int i) {
        return store_items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(layout, null);
        }

        ImageView img_store = view.findViewById(R.id.img_store);
        TextView txt_name = view.findViewById(R.id.text_store_name);
        TextView txt_address = view.findViewById(R.id.text_address);
        ImageButton btn_next = view.findViewById(R.id.btn_next);

        final StoreVO item = store_items.get(i);

        txt_name.setText(item.getSt_name());
        txt_address.setText(item.getSt_address());
        if(bitmaps[i] != null) {
            img_store.setImageBitmap(bitmaps[i]);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), C01_StoreDetailActivity.class);
                intent.putExtra("storeVO", item);
                intent.putExtra("fileURL", URL[i]);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }


    public void addImage(int index, String url, Bitmap bitmap){
        URL[index] = url;
        bitmaps[index] = bitmap;
    }
}
