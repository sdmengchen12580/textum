package com.example.yunwen.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //返回
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //统计点击事件次数
        findViewById(R.id.event_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIXME: 2018/4/24 context	当前宿主进程的ApplicationContext上下文。
                // FIXME: 2018/4/24 eventId	为当前统计的事件ID。
                // FIXME: 2018/4/24 label	事件的标签属性。
                MobclickAgent.onEvent(getApplicationContext(),"点击按钮","buttonClick");
            }
        });
        //统计购书本书
        findViewById(R.id.event1_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("type","book");
                map.put("quantity","3");
                MobclickAgent.onEvent(getApplicationContext(), "点击通加次数map按钮", map);
            }
        });
        //统计播放音乐时长
        findViewById(R.id.event2_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int duration = 12000; //开发者需要自己计算音乐播放时长
                HashMap<String, String> map_value = new HashMap<String, String>();
                map_value.put("type" , "popular" );  //类型
                map_value.put("artist" , "JJLin" );  //作者
                MobclickAgent.onEventValue(getApplicationContext(), "music" , map_value, duration);
            }
        });


    }

    // FIXME: 2018/4/24 不要父类和子类都调用onResume和onPause方法，重复统计
    // FIXME: 2018/4/24 应用在后台运行超过30秒（默认）再回到前台，将被认为是两次独立的session(启动)
    @Override
    protected void onResume() {
        super.onResume();
        // FIXME: 2018/4/24 手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onPageStart("main2activity");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // FIXME: 2018/4/24 手动统计跳转页面结束
        MobclickAgent.onPageEnd("SplashScreen");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // FIXME: 2018/4/24 登出
        MobclickAgent.onProfileSignOff();
    }


    // FIXME: 2018/4/24 如果有fragment，需要将 MobclickAgent.onPageStart("main2activity");和 MobclickAgent.onPageEnd("SplashScreen");放入fragment中
}
