package com.example.appmanger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmanger.adapter.IUninstall;
import com.example.appmanger.adapter.MyAdapter;
import com.example.appmanger.entity.AppInfo;
import com.example.appmanger.util.Utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, IUninstall, SearchView.OnQueryTextListener {
     ListView lv;
     List<AppInfo> list;
     MyAdapter adapter;
    public static final int SORT_NAME = 0;
    public static final int SORT_DATE = 1;
    public static final int SORT_SIZE = 2 ;
    public  static  final  String[] arr_sort = {"Sorting By Name","Sorting By Date","Sorting By Size"};
    int currSort = SORT_NAME;
    Comparator<AppInfo> currComparator = null;

    TextView tv_sort;
    TextView tv_size;

     // compare data(reverse order)
    Comparator<AppInfo> dateComparator = new Comparator<AppInfo>() {
         @Override
         public int compare(AppInfo appInfo, AppInfo t1) {
             if(appInfo.upTime > t1.upTime){
                 return -1;

             }else if (appInfo.upTime == t1.upTime){
                 return 0;
             }else{
                 return 1;
             }
         }
     };
    // size compare
    Comparator<AppInfo> SizeComparator = new Comparator<AppInfo>() {
        @Override
        public int compare(AppInfo appInfo, AppInfo t1) {
            if(appInfo.byteSize > t1.byteSize){
                return -1;

            }else if (appInfo.byteSize == t1.byteSize){
                return 0;
            }else{
                return 1;
            }
        }
    };
    // name compare
    Comparator<AppInfo> NameComparator = new Comparator<AppInfo>() {
        @Override
        public int compare(AppInfo appInfo, AppInfo t1) {
         return appInfo.appName.toLowerCase().compareTo(t1.appName.toLowerCase());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tv_sort = findViewById(R.id.tv_sort);
        tv_size = findViewById(R.id.tv_size);
        // get lv
        lv = findViewById(R.id.lv_main);
        // data
        //list = Utils.getAppList(this);
        adapter = new MyAdapter(this);
        //adapter.setList(list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        adapter.setUninstall(this);

        updateDate();

    }


    private void update_top(){

        tv_sort.setText("Sorting" + arr_sort[currSort]);
        tv_size.setText("Number of app:" +list.size());
    }
    SearchView sv;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.search);
        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                // expand
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                // fold
                updateDate();
                return true;
            }
        });
        sv = (SearchView)search.getActionView();
        sv.setSubmitButtonEnabled(true); // submit button
        sv.setQueryHint("Find App Name ");
        sv.setOnQueryTextListener(this);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.refresh){
            updateDate();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort_name) {
            currSort = SORT_NAME;


            //Toast.makeText(this,"NAME",Toast.LENGTH_SHORT).show();

        }
        if (id == R.id.sort_date) {
            currSort = SORT_DATE;
            //Toast.makeText(this,"DATE",Toast.LENGTH_SHORT).show();





        }
        if (id == R.id.sort_size) {
            currSort = SORT_SIZE;





            //Toast.makeText(this,"SIZE",Toast.LENGTH_SHORT).show();

        }
        updateDate_sort(currSort );
        return true;
    }

    private  void updateDate_sort(int sort){
        if (sort == SORT_NAME){
            currComparator = NameComparator;
        }
        if (sort == SORT_DATE){
            currComparator = dateComparator;
        }
        if (sort == SORT_SIZE){
            currComparator = SizeComparator;
        }
        Collections.sort(list,currComparator);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        update_top();
    }
    //1

    ProgressDialog pd;
    public void showProgressDialog(){
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("LOADING");
        pd.setMessage("Pleas Wait");
        pd.show();
    }
    //2
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            updateDate_sort(currSort);
            pd.dismiss();


        }
    };

    //3
    private void updateDate(){
        new Thread(){
            @Override
            public void run() {
                list = Utils.getAppList(MainActivity.this);
                KEYWORD =null;
                handler.sendEmptyMessage(1);
            }
        }.start();//
        showProgressDialog();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AppInfo app = (AppInfo) adapterView.getItemAtPosition(i);
        Utils.openPackage(this,app.packageName);


    }

    public static final int CODE_UNINSTALL = 0;

    @Override
    public void btnOnClick(int pos, String packageName) {
        Utils.uninstallApk(this,packageName,CODE_UNINSTALL);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODE_UNINSTALL){
            updateDate();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public  static String KEYWORD = null;

    @Override
    public boolean onQueryTextSubmit(String query) {
        // search
        KEYWORD = query;
        list = Utils.getSearchResult(list,query);
        updateDate_sort(currSort);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return true;
    }
}
