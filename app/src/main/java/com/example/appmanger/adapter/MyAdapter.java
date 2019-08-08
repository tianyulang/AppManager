package com.example.appmanger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appmanger.MainActivity;
import com.example.appmanger.R;
import com.example.appmanger.entity.AppInfo;
import com.example.appmanger.util.Utils;

import java.util.List;
import java.util.Map;


public class  MyAdapter extends BaseAdapter {

    List<AppInfo> list;
    LayoutInflater inflater;

    public void setUninstall(IUninstall uninstall) {
        this.uninstall = uninstall;
    }
    IUninstall uninstall;
    public MyAdapter(Context context) {

        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<AppInfo> list) {
        this.list = list;
    }

    @Override
    //获取listview
    public int getCount() {

        return (list == null)?0:list.size();
    }

    //下两方法不必要实现
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //系统在渲染每行内容时调用getview然后返还一个position0
        //返还view是什么就画什么 若是return null那么什么都不画 return什么样的对象画出什么
            //依靠反射器把xml文本文件反射成view
        if (convertView == null){
            convertView = inflater.inflate( R.layout.item, null);
            holder = new ViewHolder();
            holder.logo = (ImageView) convertView.findViewById(R.id.logo);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.version = (TextView) convertView.findViewById(R.id.version);
            holder.size = (TextView) convertView.findViewById(R.id.size);
            holder.btn = (Button) convertView.findViewById(R.id.btn);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        //position 参数
        AppInfo app = list.get(position);
        holder.logo.setImageDrawable(app.icon);
        if (MainActivity.KEYWORD == null){
            holder.title.setText(app.appName);
        }else{

        holder.title.setText(Utils.highLightText(app.appName, MainActivity.KEYWORD));}
        holder.version.setText("Version : "+app.versionName );
        holder.size.setText("Size : "+app.Size + "M");
        final  int pos = position;
        final String packageName = app.packageName;
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // uninstall
                uninstall.btnOnClick(pos,packageName);
            }
        });


        //logo,version等等相互牵连
        return convertView;
    }
    public class  ViewHolder{
        ImageView logo;
        TextView title;
        TextView version;
        TextView size;
        Button btn;

    }
}
