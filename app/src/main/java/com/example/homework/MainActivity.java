package com.example.homework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        switch (item.getItemId()){
            case R.id.menu_list:
                navController.navigate(R.id.memberListFragment);
                break;
            case R.id.menu_info:
                navController.navigate(R.id.memberInfoFragment);
                break;
            case R.id.menu_join:
                navController.navigate(R.id.memberJoinFragment);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}