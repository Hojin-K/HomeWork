package com.example.homework.service;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homework.MainActivity;
import com.example.homework.R;
import com.example.homework.util.GestureUtil;

public class MemberInfoFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private View view;
    private GestureUtil gestureUtil;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_m_info, container, false);

        gestureUtil = GestureUtil.getInstance();
        gestureUtil.setGesture(getContext(), view, R.layout.fragment_m_info);

        return view;
    }
}
