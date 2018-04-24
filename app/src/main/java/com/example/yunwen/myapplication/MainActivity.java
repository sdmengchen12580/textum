package com.example.yunwen.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        change(R.id.button);
        change(R.id.button1);
        change(R.id.button2);
    }

    private void change(final int id){
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (id){
                    case R.id.button:
                        change(Main2Activity.class);
                        break;
                    case R.id.button1:
                        change(Main3Activity.class);
                        break;
                    case R.id.button2:
                        change(Main4Activity.class);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void change(Class<?> activity){
        startActivity(new Intent(this,activity));
    }
}
