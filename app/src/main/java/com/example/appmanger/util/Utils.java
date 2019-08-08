package com.example.appmanger.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.example.appmanger.adapter.IUninstall;
import com.example.appmanger.entity.AppInfo;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {
    public static List<AppInfo> getAppList(Context context) {
        // return list of value
        List<AppInfo> list = new ArrayList<AppInfo>();
        // entity package manager
        PackageManager pm = context.getPackageManager();
        // get app Info
        List<PackageInfo> plist = pm.getInstalledPackages(0);
        // get info in the list
        for(int i = 0;i<plist.size();i++){
            PackageInfo packageInfo = plist.get(i);
            // put info in entity class
            if(isThirdPartApp(packageInfo.applicationInfo)
            && !packageInfo.packageName.equals(context.getPackageName())) {
                AppInfo app = new AppInfo();
                app.packageName = packageInfo.packageName;
                app.versionName = packageInfo.versionName;
                app.versionCode = packageInfo.versionCode;
                app.insTime = packageInfo.firstInstallTime;
                app.upTime = packageInfo.lastUpdateTime;
                // get picture
                app.icon = packageInfo.applicationInfo.loadIcon(pm);
                // get app name
                app.appName = (String) packageInfo.applicationInfo.loadLabel(pm);
                //calculate app size
                // size

                String dir = packageInfo.applicationInfo.publicSourceDir;
                long byteSize = new File(dir).length();
                app.byteSize = byteSize;
                app.Size = getSize(byteSize);

                list.add(app);
            }
        }





        return list;
    }


    // filter system application
    public static boolean isThirdPartApp(ApplicationInfo appInfo){
        boolean flag = false;
        if((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
            flag = true;
        }else if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
            flag = true;
        }
        return  flag;
    }


    public static String getSize(long size){
        return new DecimalFormat("0.##").format(size * 1.0/(1024 * 1024));
    }

    public  static String getTime(long millis){
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    // OPEN APP

    public static void openPackage(Context context,String packageName){
        Intent intent =
                context.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void uninstallApk(Activity context, String packageName, int requestCode){
        Uri uri = Uri.parse("package:"+packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE,uri);
        context.startActivityForResult(intent,requestCode);
    }

    public static List<AppInfo> getSearchResult(List<AppInfo> list, String key){
        List<AppInfo> result = new ArrayList<AppInfo>();
        for (int i = 0; i < list.size(); i++){
            AppInfo app = list.get(i);
            if (app.appName.toLowerCase().contains(key.toLowerCase())){
                result.add(app);
            }
        }
        return  result;

    }

    public static SpannableStringBuilder highLightText(String str, String key){
        int start = str.toLowerCase().indexOf(key.toLowerCase());
        int end = start + key.length();

        SpannableStringBuilder sb = new SpannableStringBuilder(str);
        sb.setSpan(
                new ForegroundColorSpan(Color.RED),
                start, // start location
                end, //location
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        );
        return sb;
    }


}
