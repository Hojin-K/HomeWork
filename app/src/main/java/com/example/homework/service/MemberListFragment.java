package com.example.homework.service;

import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homework.MainActivity;
import com.example.homework.R;
import com.example.homework.adapter.ListItemAdapter;
import com.example.homework.entity.MemberVO;
import com.example.homework.util.GestureUtil;

public class MemberListFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private View view;
    private GestureUtil gestureUtil;

    private ListView listView;
    private ListItemAdapter listItemAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Icon icon = Icon.createWithResource(getContext(), R.mipmap.ic_launcher);
        view =inflater.inflate(R.layout.fragment_m_list, container, false);
        listView = view.findViewById(R.id.listView);
        listItemAdapter = new ListItemAdapter();
        listItemAdapter.addMember(new MemberVO(null, "김삿갓", "11111"));
        listItemAdapter.addMember(new MemberVO(icon, "김삿갓", "11111"));
        listView.setAdapter(listItemAdapter);

        gestureUtil = new GestureUtil((MainActivity)getContext(), listView, R.layout.fragment_m_list);

        return view;
    }
}
