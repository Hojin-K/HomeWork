package com.example.homework.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homework.MainActivity;
import com.example.homework.R;
import com.example.homework.adapter.ListItemAdapter;
import com.example.homework.db.DBHelper;
import com.example.homework.entity.MemberVO;
import com.example.homework.util.GestureUtil;

import java.util.ArrayList;
import java.util.List;

public class MemberListFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private View view;
    private GestureUtil gestureUtil;

    private ListView listView;
    private ScrollView scrollView;
    private ListItemAdapter listItemAdapter;
    private SQLiteDatabase myDB;
    private MemberVO memberVO;
    private DBHelper dbHelper;
    private List<MemberVO> memberVOList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_m_list, container, false);
        scrollView = view.findViewById(R.id.sView);
        listView = view.findViewById(R.id.listView);
        listItemAdapter = new ListItemAdapter();

        memberVO = new MemberVO();

        findAll();

        for (MemberVO member: memberVOList) {
            listItemAdapter.addMember(member);
        }

        listView.setAdapter(listItemAdapter);
        setListViewHeightBasedOnChildren(listView);

        gestureUtil = GestureUtil.getInstance();
        gestureUtil.setGesture(getContext(), listView, R.layout.fragment_m_list);

        return view;
    }

    public void findAll(){
        memberVOList= new ArrayList<>();
        dbHelper = new DBHelper(getContext());
        myDB = dbHelper.getReadableDatabase();
        Cursor cursor;
        cursor = myDB.rawQuery("SELECT * FROM MEMBER;",null);

        while(cursor.moveToNext()){
            memberVO = new MemberVO();
            memberVO.setId(cursor.getString(0));
            memberVO.setName(cursor.getString(1));
            memberVO.setPwd(cursor.getString(2));
            memberVO.setPhone(cursor.getString(3));
            memberVO.setUri(cursor.getString(4));

            memberVOList.add(memberVO);
        }

        cursor.close();
        myDB.close();
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListItemAdapter listAdapter = (ListItemAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
