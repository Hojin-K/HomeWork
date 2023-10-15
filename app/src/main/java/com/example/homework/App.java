package com.example.homework;

import android.app.Application;

public class App extends Application {

    private static App instance;

    private App() {
    }

    public static synchronized App getInstance(){
        if(instance == null){
            instance = new App();
        }
        return instance;
    }

    private String imageUri;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
