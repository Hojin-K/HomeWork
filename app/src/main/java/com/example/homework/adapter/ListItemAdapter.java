package com.example.homework.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homework.R;
import com.example.homework.entity.MemberVO;

import java.util.ArrayList;

public class ListItemAdapter extends BaseAdapter {

    ArrayList<MemberVO> list = new ArrayList<MemberVO>();
    Context context;
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        MemberVO member = list.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_view, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.image);
        TextView name = convertView.findViewById(R.id.name);
        TextView phone = convertView.findViewById(R.id.phone);

        if(member.getUri() != null){
            try {
                Log.i("i", member.getUri());
                Bitmap bmp = BitmapFactory.decodeFile(member.getUri());

                if ( bmp != null ) {
                    image.setImageBitmap(bmp);
                }
            }
            catch( Exception e ) {
                e.printStackTrace();
            }
        }

        name.setText(member.getName());
        phone.setText(member.getPhone());

        return convertView;
    }

    public void addMember(MemberVO member){
        list.add(member);
    }
}
