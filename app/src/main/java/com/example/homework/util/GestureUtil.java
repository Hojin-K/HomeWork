package com.example.homework.util;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.homework.MainActivity;
import com.example.homework.R;

public class GestureUtil {

    private View view;
    private int viewId;

    private String viewName;

    private float downX;
    private float upX;

    private GestureDetector detector;
    private Context context;

    public GestureUtil(Context context, View view, int viewId) {
        this.view = view;
        this.viewId = viewId;
        this.viewName = view.getResources().getResourceEntryName(viewId);
        this.context = context;

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                int direction = 0;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    Log.i("Info", "키업했어요");
                    if(downX > upX){
                        Log.i("Info", "왼쪽");
                        direction = 0;

                    }else if(downX < upX){
                        Log.i("Info", "오른쪽");
                        direction = 1;
                    }

                    moveFragement(direction);
                }
                return true;
            }
        });

        detector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.i("Info", String.format("e1 위치 %f",e1.getX()));
                Log.i("Info", String.format("e2 위치 %f",e2.getX()));
                Log.i("Info", String.format("거리차이 %f",distanceX));

                downX = e1.getX();
                upX = e2.getX();

                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return true;
            }
        });

    }

    public void moveFragement(int direction){
        //navConroller에 view 자체를 넘겨서 해결.
        NavController navController = Navigation.findNavController((MainActivity)context, R.id.nav_host_fragment);

        switch (viewName){
            case "fragment_m_list":
                    if(direction == 0){
                        navController.navigate(R.id.memberJoinFragment);
                    }else if(direction == 1){
                        navController.navigate(R.id.memberInfoFragment);
                    }
                break;
            case "fragment_m_info":
                if(direction == 0){
                    navController.navigate(R.id.memberListFragment);
                }else if(direction == 1){
                    navController.navigate(R.id.memberJoinFragment);
                }

                break;
            case "fragment_m_join":
                if(direction == 0){
                    navController.navigate(R.id.memberInfoFragment);
                }else if(direction == 1){
                    navController.navigate(R.id.memberListFragment);
                }

                break;
        }
    }

}
