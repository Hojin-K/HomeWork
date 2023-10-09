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
    private float downY;
    private float upY;
    private float distX;
    private float distY;


    private GestureDetector detector;
    private Context context;

    private static GestureUtil instance;

    private GestureUtil() {
    }

    public static synchronized GestureUtil getInstance(){
        if(instance == null){
            instance = new GestureUtil();
        }
        return instance;
    }

    public void setGesture(Context context, View view, int viewId) {
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
                    Log.i("Info", distX+"///"+distY);
                    if(distY < 3 && distY > -3){
                        if(distX > 2){
                            Log.i("Info", "왼쪽");
                            direction = 0;

                        }else if(distX < -2){
                            Log.i("Info", "오른쪽");
                            direction = 1;
                        }

                        moveFragement(direction);
                    }else{
                        Log.i("Info", "이동없음");
                    }
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
                distX = 0.0f;
                distY = 0.0f;
                distX = distanceX;
                distY = distanceY;

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
