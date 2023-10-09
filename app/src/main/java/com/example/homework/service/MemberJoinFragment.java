package com.example.homework.service;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.homework.App;
import com.example.homework.R;
import com.example.homework.db.DBHelper;
import com.example.homework.entity.MemberVO;
import com.example.homework.util.CameraUtil;
import com.example.homework.util.GestureUtil;
import com.example.homework.util.MediaScanner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MemberJoinFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private View view;
    private GestureUtil gestureUtil;
    private ImageView imageView;
    private Uri imageCaptureUri;
    private EditText inputId, inputName, inputPwd, inputPhone;
    private Button btnJoin;

    private ActivityResultLauncher<Intent> startCameraActionLauncher;

    private String imageFilePath;
    private MediaScanner mMediaScanner;

    private CameraUtil cameraUtil;
    private MemberVO memberVO;
    private SQLiteDatabase myDB;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_join, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = (ImageView) view.findViewById(R.id.input_image);
        imageView.setOnClickListener(this::onImageClick);

        //회원정보 입력
        inputId = (EditText) view.findViewById(R.id.input_id);
        inputPwd = (EditText) view.findViewById(R.id.input_pwd);
        inputName = (EditText) view.findViewById(R.id.input_name);
        inputPhone = (EditText) view.findViewById(R.id.input_phone);

        gestureUtil = GestureUtil.getInstance();
        gestureUtil.setGesture(getContext(), view, R.layout.fragment_m_join);

        mMediaScanner = MediaScanner.getInstance(getContext());
        cameraUtil = CameraUtil.getInstance();

        startCameraActionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            // There are no request codes
                            cameraUtil.setImageToBitmap(imageFilePath,imageView, mMediaScanner); //이미지 저장
                        }
                    }
                }
        );

        //join button click
        btnJoin = view.findViewById(R.id.btn_join);
    }

    @Override
    public void onResume() {
        super.onResume();

        memberVO = new MemberVO();
        App mApp = App.getInstance();

        btnJoin.setOnClickListener(v -> {
            if(isNullOrEmpty(inputId.getText().toString().trim())){
                Toast.makeText(getContext(),"ID를 입력해주세요", Toast.LENGTH_SHORT).show();
            }
            if(isNullOrEmpty(inputPwd.getText().toString().trim())){
                Toast.makeText(getContext(),"패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
            }
            if(isNullOrEmpty(inputName.getText().toString().trim())){
                Toast.makeText(getContext(),"이름를 입력해주세요", Toast.LENGTH_SHORT).show();
            }
            if(isNullOrEmpty(inputPhone.getText().toString().trim())){
                Toast.makeText(getContext(),"전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            }
            memberVO.setId(inputId.getText().toString().trim());
            memberVO.setPwd(inputPwd.getText().toString().trim());
            memberVO.setName(inputName.getText().toString().trim());
            memberVO.setPhone(inputPhone.getText().toString().trim());
            memberVO.setUri(mApp.getImageUri());
            Log.i("i", mApp.getImageUri());
            if(findOne(memberVO)){
                Toast.makeText(getContext(),"이미 존재하는 ID 입니다.", Toast.LENGTH_SHORT).show();
            }else{
                //DB에 저장
                insertMember();
            }

        });
    }

    public boolean isNullOrEmpty(String str){
        if(str == null || str == ""){
            return true;
        }
        return false;
    }

    public boolean findOne(MemberVO member){
        dbHelper = new DBHelper(getContext());
        myDB = dbHelper.getReadableDatabase();
        Cursor cursor;
        cursor = myDB.rawQuery("SELECT ID FROM MEMBER WHERE ID = '"+member.getId()+"';", null);

        if(cursor.getCount() >0){
            cursor.close();
            myDB.close();
            return true;
        }
        cursor.close();
        myDB.close();
        return false;
    }

    public void insertMember(){
        try {
            dbHelper = new DBHelper(getContext());
            myDB = dbHelper.getWritableDatabase();
            myDB.execSQL("INSERT INTO MEMBER VALUES('"
                    + memberVO.getId() + "','"
                    + memberVO.getPwd() + "','"
                    + memberVO.getName() + "','"
                    + memberVO.getPhone() + "','"
                    + memberVO.getUri() + "');");
            myDB.close();
            Toast.makeText(this.getContext(),"입력완료",Toast.LENGTH_SHORT);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void onImageClick(View view){
        if(view.getId() == R.id.input_image){
            checkImageMode(view);
        }
    }

    public void checkImageMode(View v){
        new AlertDialog.Builder(getContext())

                .setTitle("업로드할 이미지 선택")

                .setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cameraMode();
                    }
                })

                //.setNeutralButton("앨범선택", albumListener)

                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .show();
    }



    public void cameraMode(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();

        }catch (IOException e){
            e.printStackTrace();
        }

        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getContext(),
                    getContext().getPackageName()+".fileprovider",
                    photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startCameraActionLauncher.launch(intent);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imageFilePath = image.getAbsolutePath();
        return image;
    }


}
