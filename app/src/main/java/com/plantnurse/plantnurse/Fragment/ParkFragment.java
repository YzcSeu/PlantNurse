package com.plantnurse.plantnurse.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
//import com.plantnurse.plantnurse.Activity.AddplantActivity;
import com.plantnurse.plantnurse.Activity.AddplantActivity;
import com.plantnurse.plantnurse.Activity.MainActivity;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.Calendar;
import com.plantnurse.plantnurse.utils.ListDialog;
import com.plantnurse.plantnurse.utils.WeatherAdapter;
import com.plantnurse.plantnurse.utils.WeatherManager;


import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;



/**
 * Created by Eason_Tao on 2016/8/26.
 */

public class ParkFragment extends KSimpleBaseFragmentImpl implements IBaseAction{
    //view
    private TextView text_Tmp;
    private ImageView image_Weather;
    private ImageView image_Plant;
    private TextView text_City;
    private ImageButton left_Button;
    private ImageButton right_Button;
    private ListView weather_ListView;
    //data
    private String now_Tmp;
    private String city;
    private int now_Cond;
    private  List<Map<String,Object>> mData;
    private Context mContext;
    private WeatherListAdapter adapter;

    @Override
    public int initLocalData() {
        mContext=getActivity();
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        text_Tmp = (TextView) view.findViewById(R.id.text_temp);
        text_City = (TextView) view.findViewById(R.id.text_city);
        image_Weather = (ImageView) view.findViewById(R.id.image_weather);
        image_Plant = (ImageView) view.findViewById(R.id.image_flower);
        left_Button = (ImageButton) view.findViewById(R.id.button_left);
        right_Button = (ImageButton) view.findViewById(R.id.button_right);
    }

    @Override
    public void initController() {
        now_Tmp = WeatherManager.getWeatherInfo().now.tmp;
        now_Cond = Integer.parseInt(WeatherManager.getWeatherInfo().now.cond.code);
        city = WeatherManager.getWeatherInfo().basic.city;

        left_Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.left3_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.left_3);
                }
                return false;
            }
        });

        right_Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.right3_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.right_3);
                }
                return false;
            }
        });

        image_Plant.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(),AddplantActivity.class);
                startActivity(intent); // 启动Activity
                return false;
            }
        });

        image_Weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 //               Dialog calendar_Dialog = new Dialog(getActivity(), R.style.CalenderStyle);
//                calendar_Dialog.setContentView(R.layout.dialog_calender);

                ListDialog ld=new ListDialog(getActivity(),R.style.CalenderStyle);
//                Window calender_Window = calendar_Dialog.getWindow();
                Window calender_Window = ld.getWindow();
                WindowManager.LayoutParams lp = calender_Window.getAttributes();
                calender_Window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                lp.width = 1000;
                lp.height = 500;
                lp.y = -100;

                //---1---
//                ListAdapter adapter = new SimpleAdapter( getActivity(),getData(),
//                        R.layout.listview_weather,
//                        new String[] { "img","date","tmp"},
//                        new int[] {R.id.list_image, R.id.list_date,R.id.list_tmp});
//                weather_ListView.setAdapter(adapter);
                //---2---
                mData = getData();
                adapter = new WeatherListAdapter(getActivity());
                ld.getListView().setAdapter(adapter);
                //---3---
//                WeatherAdapter adapter = new WeatherAdapter(getActivity(),s);
//                weather_ListView.setAdapter(adapter);
                ld.show();
            }
        });

        text_Tmp.setText(now_Tmp + "℃");
        text_City.setText(city);

        switch (now_Cond){
            case 100:
                image_Weather.setImageResource(R.drawable.p1);
                break;
            case 101:
                image_Weather.setImageResource(R.drawable.cloudy);
                break;
            case 102:
                image_Weather.setImageResource(R.drawable.cloudy);
                break;
            case 103:
                image_Weather.setImageResource(R.drawable.cloudy_2);
                break;
            case 104:
                image_Weather.setImageResource(R.drawable.cloudy_3);
                break;
            case 300:
                image_Weather.setImageResource(R.drawable.rainy_2);
                break;
            case 302:
                image_Weather.setImageResource(R.drawable.thunder);
                break;
            case 305:
                image_Weather.setImageResource(R.drawable.rainy_3);
                break;
            case 306:
                image_Weather.setImageResource(R.drawable.rainy_3);
                break;
            case 307:
                image_Weather.setImageResource(R.drawable.rainy);
                break;
            case 400:
                image_Weather.setImageResource(R.drawable.snow);
                break;
            case 406:
                image_Weather.setImageResource(R.drawable.snow_2);
                break;
            }


    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        int image_code;
        Map<String, Object> map;
        for (int i = 0; i < 7; i++) {
            map = new HashMap<String, Object>();
            map.put("date", WeatherManager.getWeatherInfo().dailyForecast.get(i).date);
            map.put("tmp_min", WeatherManager.getWeatherInfo().dailyForecast.get(i).tmp.min);
            //Log.e("test", WeatherManager.getWeatherInfo().dailyForecast.get(i).tmp.min);
            map.put("tmp_max", WeatherManager.getWeatherInfo().dailyForecast.get(i).tmp.max);
            image_code = Integer.parseInt(WeatherManager.getWeatherInfo().dailyForecast.get(i).cond.codeD);
            switch (image_code) {
                case 100:
                    map.put("img",R.drawable.sunny);
                    break;
                case 101:
                    map.put("img", R.drawable.cloudy);
                    break;
                case 102:
                    map.put("img", R.drawable.cloudy);
                    break;
                case 103:
                    map.put("img", R.drawable.cloudy_2);
                    break;
                case 104:
                    map.put("img", R.drawable.cloudy_3);
                    break;
                case 300:
                    map.put("img", R.drawable.rainy_2);
                    break;
                case 302:
                    map.put("img", R.drawable.thunder);
                    break;
                case 305:
                    map.put("img", R.drawable.rainy_3);
                    break;
                case 306:
                    map.put("img", R.drawable.rainy_3);
                    break;
                case 307:
                    map.put("img", R.drawable.rainy);
                    break;
                case 400:
                    map.put("img", R.drawable.snow);
                    break;
                case 406:
                    map.put("img", R.drawable.snow_2);
                    break;
           }
            list.add(map);
            //adapter.notifyDataSetChanged();
        }
        return list;
    }

    public final class ViewHolder{
        public ImageView weather_image;
        public TextView weather_date;
        public TextView weather_tmp;
    }

    public class WeatherListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public  WeatherListAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData!=null?mData.size():0;
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null){
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.listview_weather,null);
                holder.weather_image = (ImageView) convertView.findViewById(R.id.list_image);
                holder.weather_date = (TextView) convertView.findViewById(R.id.list_date);
                holder.weather_tmp = (TextView) convertView.findViewById(R.id.list_tmp);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            //String str=mData.get(position).get("img");
            //int drawable =mContext .getResources().getIdentifier(str, "drawable", mContext.getPackageName());
            // holder.weather_Image.setImageResource(drawable);
            holder.weather_image.setBackgroundResource((Integer) mData.get(position).get("img"));
            holder.weather_date.setText((String) mData.get(position).get("date"));
            holder.weather_tmp.setText((String)(mData.get(position).get("tmp_min")+"~"+mData.get(position).get("tmp_max")+"℃"));
            return convertView;
        }
    }


    @Override
    public void onLoadingNetworkData() {

    }

    @Override
    public void onLoadedNetworkData(View contentView) {
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_park;
    }
}
