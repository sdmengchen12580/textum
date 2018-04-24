package com.example.yunwen.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;


/**
 * Created by yunwen on 2018/4/24.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化统计
        initAnalyse();
        //初始化推送
        initPush();

        // FIXME: 2018/4/24 账号：1055199957@qq.com
        // FIXME: 2018/4/24 密码：mcall1314
    }

    private void initPush() {

    }

    private void initAnalyse() {
        // FIXME: 2018/4/24 友盟获取频道--deviceType为手机和盒子
        String channelName = AnalyticsConfig.getChannel(this);
        UMConfigure.init(this, "5ade85528f4a9d52af000a6b", channelName, UMConfigure.DEVICE_TYPE_PHONE, "");
        // FIXME: 2018/4/24 场景选择
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        // FIXME: 2018/4/24 推出后台再回到主页面算为2次
//        MobclickAgent.setSessionContinueMillis(40000);

        // FIXME: 2018/4/24 当用户使用自有账号登录时，可以这样统计
        MobclickAgent.onProfileSignIn(DeviceUtils.getUniqueId(this));
        // FIXME: 2018/4/24 当用户使用第三方账号（如新浪微博）登录时，可以这样统计：
//        MobclickAgent.onProfileSignIn("WB","userID");
        // FIXME: 2018/4/24 禁止默认的页面统计功能,将不会不会再自动统计Activity页面
        MobclickAgent.openActivityDurationTrack(false);



       /* 每个应用至多添加500个自定义事件，每个event 的 key不能超过10个，
        每个key的取值不能超过1000个。如需要统计支付金额、使用时长等数值型的连续变量，请使用计算事件*/


//        //报错统计、true-打开错误统计功能（默认打开）。
//        MobclickAgent.setCatchUncaughtExceptions(true);
//        //错误内容
//        MobclickAgent.reportError(this, "Parameter Error");
//        try {
//            // 抛出异常的代码
//        } catch (Exception e) {
//            MobclickAgent.reportError(this, e);
//        }

        // FIXME: 2018/4/24 latitude	传入的纬度。
        // FIXME: 2018/4/24 longitude	传入的经度。
//        MobclickAgent.setLocation();



        // FIXME: 2018/4/24 通过OpenGL接口设置GPU信息，采集GL10.GL_VENDOR和GL10.GL_RENDERER字段值。
//        // 设置OpenGL信息，统计GPU 信息。
//        MobclickAgent.setOpenGLContext(GL10 gl);
//        参数	含义
//        gl	GL10实例。


        // FIXME: 2018/4/24 程序退出时，用于保存统计数据的API。
//        如果开发者调用kill或者exit之类的方法杀死进程，请务必在此之前调用onKillProcess方法，用来保存统计数据。
//        public static void onKillProcess(Context context);
//        参数	含义
//        context	当前宿主进程的ApplicationContext上下文。


        // FIXME: 2018/4/24 集成测试
//        集成测试是通过收集和展示已注册测试设备发送的日志，来检验SDK集成有效性和完整性的一个服务。
//        所有由注册设备发送的应用日志将实时地进行展示，您可以方便地查看包括应用版本、渠道名称、
//        自定义事件、页面访问情况等数据，提升集成与调试的工作效率。

//        使用集成测试之后，所有测试数据不会进入应用正式的统计后台，
//        只能在”【管理】—【集成测试】—【实时日志】”里查看，您不必再担心因为测试而导致的数据污染问题，
//        让数据更加真实有效的反应用户使用情况。

        UMConfigure.setLogEnabled(true);
        Log.e( "initAnalyse: ", getDeviceInfo(this));//此信息传到集成测试指定位置
    }


    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = getMac(context);

            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMac(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        if (Build.VERSION.SDK_INT < 23) {
            mac = getMacBySystemInterface(context);
        } else {
            mac = getMacByJavaAPI();
            if (TextUtils.isEmpty(mac)){
                mac = getMacBySystemInterface(context);
            }
        }
        return mac;

    }

    @TargetApi(9)
    private static String getMacByJavaAPI() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();
                if ("wlan0".equals(netInterface.getName()) || "eth0".equals(netInterface.getName())) {
                    byte[] addr = netInterface.getHardwareAddress();
                    if (addr == null || addr.length == 0) {
                        return null;
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString().toLowerCase(Locale.getDefault());
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static String getMacBySystemInterface(Context context) {
        if (context == null) {
            return "";
        }
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            } else {
                return "";
            }
        } catch (Throwable e) {
            return "";
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (context == null) {
            return result;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Throwable e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

}
