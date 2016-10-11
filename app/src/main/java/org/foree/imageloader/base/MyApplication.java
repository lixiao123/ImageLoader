package org.foree.imageloader.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import org.foree.imageloader.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by foree on 3/7/15.
 * 包含自己app的一些信息：
 * 1）当前网络状况
 * 2）应用程序目录和缓存目录
 * 3)Sdcard路径
 * <p/>
 * 以及启动时需要做的初始化
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private Context mContext;
    public static boolean mFirstRun;

    /**
     * Sdcard信息
     */
    //Sdcard的路径
    public static final String SdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    //Sdcard的状态（用于判断是否挂载）
    public static final String SdcardState = Environment.getExternalStorageState();

    /**
     * 应用程序信息
     */
    //应用程序名称
    public static String myApplicationName;
    //应用程序sdcard目录名
    public static String myApplicationDir = "Imageloader";
    //应用程序包名
    public static String myApplicationPackageName;
    //应用程序版本名称
    public static String myVersionName;
    //应用程序版本序号(应用程序用来判断是否升级的,例如:17)
    public static int myVersionCode;
    //应用程序版本号(开发者自定义,例如:1.7.3
    public static String myApplicationVersion;
    //应用程序的网络状态
    //应用程序的sdcard的目录路径
    public static String mySdcardDataDir;
    //应用程序的缓存目录路径
    public static String mySdcardCacheDir;
    //应用程序来源的缓存目录
    public static String MYSDCARDSOURCEDIR;
    //程序当前运行的最新日期
    public static String myDate;

    public MyApplication(Activity context) {
        mContext = context;
    }

    //初始化环境(loading...时候使用)
    public void initEnv() {

        //初始化log打印级别,默认为VERBOSE
        //LogUtils.logLevel = Log.INFO;

        //初始化应用程序名
        myApplicationName = mContext.getString(R.string.app_name);
        //初始化应用程序的包名
        myApplicationPackageName = mContext.getPackageName();
        //初始化当前日期
        myDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(new Date());
        // Log.v(TAG, myDate);

        //如果当前Sdcard已经挂载，应用程序目录与缓存目录是否建立完成
        if (SdcardState.equals(Environment.MEDIA_MOUNTED)) {
            //应用程序目录
            File myDateDir = new File(SdcardPath + "/" + myApplicationDir + "/");
            if (!myDateDir.exists())
                if (!myDateDir.mkdir()) {
                    Log.e(TAG, "创建应用程序目录失败");
                }
            mySdcardDataDir = SdcardPath + "/" + myApplicationDir;
            //缓存目录
            File myCacheDir = new File(mySdcardDataDir + "/" + "cache/");
            if (!myCacheDir.exists())
                if (!myCacheDir.mkdir()) {
                    Log.e(TAG, "创建缓存目录失败");
                }
            mySdcardCacheDir = mySdcardDataDir + "/" + "cache";
            //source目录
            File mySourceDir = new File(mySdcardDataDir + File.separator + "source/");
            if (!mySourceDir.exists())
                if (!mySourceDir.mkdir()) {
                    Log.e(TAG, "创建source目录失败");
                }
            MYSDCARDSOURCEDIR = mySdcardDataDir + File.separator + "source";
        }

        initApplicationVersionInfo(mContext);

    }

    public File getCacheDir(){
        return new File(mySdcardDataDir + "/" + "cache/");
    }

    //获取应用程序的版本信息
    private void initApplicationVersionInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            //获取当前包的信息
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            //获取应用程序版本号
            myApplicationVersion = packageInfo.versionName;
            //获取版本序号
            myVersionCode = packageInfo.versionCode;
            //获取版本名称
            myVersionName = myApplicationName + " v" + packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


}