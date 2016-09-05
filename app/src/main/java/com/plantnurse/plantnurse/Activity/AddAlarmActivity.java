package com.plantnurse.plantnurse.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.Alarm;
import com.plantnurse.plantnurse.utils.AlarmInfo;
import com.plantnurse.plantnurse.utils.AlarmReceiver;
import com.plantnurse.plantnurse.utils.AlarmSelectPlantAdapter;
import com.plantnurse.plantnurse.utils.CircleImg;
import com.plantnurse.plantnurse.utils.ToastUtil;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;


import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yxuan on 2016/8/26.
 */
public class AddAlarmActivity extends KSimpleBaseActivityImpl
        implements IBaseAction, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /**
     * Create by Heloise
     * Begin
     */
    private NumberPicker numberPicker_h;
    private NumberPicker numberPicker_m;
    private NumberPicker numberPicker_ampm;
    private int iseveryday_clicked = 0;
    private int isdayoverday_clicked = 0;
    private int isthreeday_clicked = 0;
    private int isuserdefine_clicked = 0;
    private int iswater_clicked = 0;
    private int issun_clicked = 0;
    private int isback_clicked = 0;
    private int isworm_clicked = 0;
    private int ismedicine_clicked = 0;
    private ImageButton button_everyday;
    private ImageButton button_dayoverday;
    private ImageButton button_threeday;
    private ImageButton button_water;
    private ImageButton button_sun;
    private ImageButton button_back;
    private ImageButton button_worm;
    private ImageButton button_medicine;
    private ImageButton button_time;
    private ImageButton button_userdefine;
    private Button button_ok;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    /**
     * End
     * Create by Heloise
     */

    private Toolbar toolbar;
    private CircleImg circleImg_green;
    private CircleImg circleImg_pink;
    private CircleImg circleImg_blue;
//    private CircleImg circleImg_yellow;
    private RecyclerView recyclerView_plant;//选择植物列表
    private AlarmSelectPlantAdapter adapter;//添加植物的图片适配器
    private List<Integer> plantData;//添加植物的图片列表
    EditText editText;//备注
    private ImageView plantView;
    private TextView selectedFrequency;//显示当前所选或已选时间

    private long currentTime;//当前时间
    private String currentOrSelected;//当前时间或已选择时间的Sting类型，方便分隔时间和日期
    private String selectedDate;//已选择的日期
    private String selectedTime1;//已选择的时间
    private String selectedHour;//已选择的小时数
    private String selectedMin;//已选择的分钟数
    private int roleColor=1;//选择的角色颜色,绿色1、粉色2、蓝色3、黄色4，默认为绿色
    private long selectedTime;//重新选定的时间
    private int alarm_Id;//闹钟的ID号
    private int isAlarm = 0;//判断是否有添加闹钟,0为没有，1为有
    private String text;//所编辑的内容
    private String date;//新选择的日期
    private String time;//新选择的时间
    private int frequency=0;//设置重复次数对应的值：每天1、隔一天2、隔两天3、自定义4、无选择默认为当天时间0
    private AlarmInfo info;//闹钟的增删查改等工作
    private Alarm alarm;//获取闹钟的相关属性

    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置时间格式
    private int ampm;//NumberPicker的上下午
    private int hour;//NumberPicker的小时
    private int min;//NumberPicker的分钟





    @Override
    public int initLocalData() {

        currentTime=System.currentTimeMillis();
        currentOrSelected=formatter.format(currentTime);//当前时间
        //分隔日期和时间
        String str[]=currentOrSelected.split(" ");
        date=str[0];//初始化当前日期


        //获取AlarmFragment传来的值，新建闹钟的alarm_Id=0
        Intent intent = getIntent();
        alarm_Id = intent.getIntExtra("alarm_Id", 0);

        //需要从数据库中读取用户所有的植物，
        plantData = new ArrayList<Integer>(Arrays.asList(R.drawable.sunny, R.drawable.cloudy,
                R.drawable.cloudy_2, R.drawable.cloudy_3, R.drawable.rainy_2, R.drawable.rainy,
                R.drawable.rainy_3));

        //初始化对应闹钟
        info = new AlarmInfo(getSimpleApplicationContext());
        alarm = new Alarm();

        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        /**
         * Create by Heloise
         * Begin
         */
        numberPicker_h = (NumberPicker) findViewById(R.id.numberPicker_hour);
        numberPicker_m = (NumberPicker) findViewById(R.id.numberPicker_minute);
        numberPicker_ampm = (NumberPicker) findViewById(R.id.numberPicker_AMPM);
        String[] ampm = {"上午", "下午"};
        numberPicker_ampm.setDisplayedValues(ampm);
        numberPicker_ampm.setMinimumHeight(0);
        numberPicker_ampm.setMaxValue(1);
        numberPicker_h.setMinValue(0);
        numberPicker_h.setMaxValue(11);
        numberPicker_m.setMinValue(0);
        numberPicker_m.setMaxValue(59);

        setNumberPickerDividerColor(numberPicker_ampm);
        setNumberPickerDividerColor(numberPicker_h);
        setNumberPickerDividerColor(numberPicker_m);

        button_everyday = (ImageButton) findViewById(R.id.button_everyday);
        button_dayoverday = (ImageButton) findViewById(R.id.button_dayoverday);
        button_threeday = (ImageButton) findViewById(R.id.button_threeday);
        button_water = (ImageButton) findViewById(R.id.button_water);
        button_sun = (ImageButton) findViewById(R.id.button_sun);
        button_back = (ImageButton) findViewById(R.id.button_back);
        button_worm = (ImageButton) findViewById(R.id.button_worm);
        button_medicine = (ImageButton) findViewById(R.id.button_medicine);
        button_time = (ImageButton) findViewById(R.id.button_time);
        button_userdefine = (ImageButton) findViewById(R.id.button_userdefine);
        button_ok = (Button) findViewById(R.id.button_ok);

        calendar = Calendar.getInstance();

        datePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), true);
        timePickerDialog = TimePickerDialog.newInstance(this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false, false);
        /**
         * End
         * Create by Heloise
         */

        toolbar = (Toolbar) view.findViewById(R.id.setalarm_toolbar);
        circleImg_green = (CircleImg) view.findViewById(R.id.role_green);
        circleImg_pink = (CircleImg) view.findViewById(R.id.role_pink);
        circleImg_blue = (CircleImg) view.findViewById(R.id.role_blue);
//        circleImg_yellow = (CircleImg) view.findViewById(R.id.role_yellow);
        recyclerView_plant = (RecyclerView) findViewById(R.id.recyclerView_selectPlant);
        editText = (EditText) view.findViewById(R.id.edit_other);
        selectedFrequency=(TextView)view.findViewById(R.id.text_selectedFrequency);

        circleImg_green.setImageResource(R.drawable.alarmrole_green);
        circleImg_green.setBorderWidth(0);
        circleImg_pink.setImageResource(R.drawable.alarmrole_pink);
        circleImg_pink.setBorderWidth(0);
        circleImg_blue.setImageResource(R.drawable.alarmrole_blue);
        circleImg_blue.setBorderWidth(0);
//        circleImg_yellow.setImageResource(R.drawable.alarmrole_yellow);
//        circleImg_yellow.setBorderWidth(0);

        //设置toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("闹钟设置");
        //设为不可编辑
        numberPicker_ampm.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker_h.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker_m.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //设置recyclerView的布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_plant.setLayoutManager(linearLayoutManager);
        //设置recyclerView的适配器
        adapter = new AlarmSelectPlantAdapter(this, plantData);
        adapter.setOnItemClickLitener(new AlarmSelectPlantAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtil.showShort(position + "");
                plantView.setImageResource(plantData.get(position));
            }
        });
        recyclerView_plant.setAdapter(adapter);




        //初始化数据
        //alarm_Id不为0则表示是打开以前的闹钟
        if (alarm_Id != 0) {
            alarm = info.getAlarmById(alarm_Id);
            editText.setText(alarm.content);
            roleColor=alarm.roleColor;
            time = alarm.time;
            currentOrSelected=time;//已选择的时间
            isAlarm = alarm.isAlarm;
            frequency = alarm.frequency;
            iswater_clicked = alarm.water;
            issun_clicked = alarm.sun;
            isback_clicked = alarm.takeBack;
            isworm_clicked = alarm.takeCare;
            ismedicine_clicked = alarm.fertilization;

            //初始化已设置的闹钟按钮

            repeatClickEvent(frequency);
            actionClickEvent(iswater_clicked, button_water,R.drawable.action1_grey,R.drawable.action1);
            actionClickEvent(issun_clicked, button_sun,R.drawable.action2_grey,R.drawable.action2);
            actionClickEvent(isback_clicked, button_back,R.drawable.action3_grey,R.drawable.action3);
            actionClickEvent(isworm_clicked, button_worm,R.drawable.action4_grey,R.drawable.action4);
            actionClickEvent(ismedicine_clicked, button_medicine,R.drawable.action5_grey,R.drawable.action5);

        }
        setRoleColor(roleColor);

        //分隔日期和时间
        String strDorT[]=currentOrSelected.split(" ");
        selectedDate=strDorT[0];
        selectedTime1=strDorT[1];

        //分隔时钟，分钟
        String strHorM[]=selectedTime1.split(":");
        selectedHour=strHorM[0];
        selectedMin=strHorM[1];

        //初始化NumberPicker的值

        if (Integer.parseInt(selectedHour) < 12) {
            numberPicker_ampm.setValue(0);
            numberPicker_h.setValue(Integer.parseInt(selectedHour));
        } else {
            numberPicker_ampm.setValue(1);
            numberPicker_h.setValue(Integer.parseInt(selectedHour)-12);
        }
        numberPicker_m.setValue(Integer.parseInt(selectedMin));


    }


    //角色的初始化和选择
    public void setRoleColor(int i){
        switch (i){
            case 1:
                circleImg_green.setBorderWidth(5);
                circleImg_green.setBorderColor(R.color.greenborder);
                circleImg_pink.setBorderWidth(0);
                circleImg_blue.setBorderWidth(0);
//                circleImg_yellow.setBorderWidth(0);
                break;
            case 2:
                circleImg_pink.setBorderWidth(5);
                circleImg_pink.setBorderColor(R.color.pinkborder);
//                circleImg_yellow.setBorderWidth(0);
                circleImg_blue.setBorderWidth(0);
                circleImg_green.setBorderWidth(0);
                break;
            case 3:
                circleImg_blue.setBorderWidth(5);
                circleImg_blue.setBorderColor(R.color.blueborder);
                circleImg_pink.setBorderWidth(0);
//                circleImg_yellow.setBorderWidth(0);
                circleImg_green.setBorderWidth(0);
                break;
            case 4:
//                circleImg_yellow.setBorderWidth(5);
//                circleImg_yellow.setBorderColor(R.color.yellowborder);
                circleImg_pink.setBorderWidth(0);
                circleImg_blue.setBorderWidth(0);
                circleImg_green.setBorderWidth(0);
                break;
        }
    }

    //重复闹钟按钮的初始化颜色
    public void repeatClickEvent(int i) {
        switch (i) {
            case 1:
                iseveryday_clicked=1;
                selectedFrequency.setText("每天");
                button_everyday.setImageResource(R.drawable.frequency1);
                break;
            case 2:
                isdayoverday_clicked=1;
                selectedFrequency.setText("隔一天");
                button_dayoverday.setImageResource(R.drawable.frequency2);
                break;
            case 3:
                isthreeday_clicked=1;
                selectedFrequency.setText("隔两天");
                button_threeday.setImageResource(R.drawable.frequency3);
                break;
            case 4:
                isuserdefine_clicked=1;
                selectedFrequency.setText(selectedDate);
                button_userdefine.setImageResource(R.drawable.frequency4);
                break;
        }

    }

    //选择行为按钮的初始化颜色
    public void actionClickEvent(int i, ImageButton b,int picture_grey,int picture) {
        switch (i) {
            case 0:
                b.setImageResource(picture_grey);
                break;
            case 1:
                b.setImageResource(picture);
                break;
        }

    }

    @Override
    public void initController() {

        circleImg_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roleColor==1){//取消选择
                    ToastUtil.showShort("不选择颜色时默认为绿色~");
                }else {
                    roleColor=1;
                    setRoleColor(roleColor);
                }

            }
        });

        circleImg_pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roleColor==2){//取消选择
                    roleColor=1;
                    setRoleColor(roleColor);
                    ToastUtil.showShort("不选择颜色时默认为绿色~");
                }else {
                    roleColor=2;
                    setRoleColor(roleColor);
                }
            }
        });

        circleImg_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roleColor==3){//取消选择
                    roleColor=1;
                    setRoleColor(roleColor);
                    ToastUtil.showShort("不选择颜色时默认为绿色~");
                }else {
                    roleColor=3;
                    setRoleColor(roleColor);
                }
            }
        });

//        circleImg_yellow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(roleColor==4){//取消选择
//                    roleColor=1;
//                    setRoleColor(roleColor);
//                    ToastUtil.showShort("不选择颜色时默认为绿色~");
//                }else {
//                    roleColor=4;
//                    setRoleColor(roleColor);
//                }
//            }
//        });

        button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //timePickerDialog.setVibrate(isVibrate());
                //timePickerDialog.setCloseOnSingleTapMinute(isCloseOnSingleTapMinute());
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
                isAlarm=1;
            }
        });


        numberPicker_ampm.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (oldVal != newVal) {
                    isAlarm = 1;
                    ampm = newVal;
                }
            }
        });

        numberPicker_h.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(oldVal!=newVal){
                    isAlarm=1;
                    hour=newVal;
                }
            }
        });

        numberPicker_m.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(oldVal!=newVal){
                    isAlarm=1;
                    min=newVal;
                }
            }
        });

        /**
         * ButtonGroup:选择重复的按钮只能选择一个
         */
        button_userdefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isuserdefine_clicked == 1) {//用户取消自定义，则日期要变
                    //获取当前时间
                    currentTime=System.currentTimeMillis();
                    //时间处理
                    ampm=numberPicker_ampm.getValue();
                    hour=numberPicker_h.getValue();
                    min=numberPicker_m.getValue();
                    if(ampm==1){
                        hour+=12;
                    }
                    //获取设置的时间(取消之前设置的日期）
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                            (Calendar.DAY_OF_MONTH),hour, min, 10);
                    selectedTime=calendar.getTimeInMillis();
                    button_userdefine.setImageResource(R.drawable.frequency4_grey);
                    isuserdefine_clicked = 0;
                    frequency=0;
                    date=null;
                    selectedFrequency.setText("");
                } else {
                    //若选择自定义，则其他的按钮全为false，颜色设为灰色
                    iseveryday_clicked = 0;
                    isdayoverday_clicked = 0;
                    isthreeday_clicked = 0;
                    button_everyday.setImageResource(R.drawable.frequency1_grey);
                    button_dayoverday.setImageResource(R.drawable.frequency2_grey);
                    button_threeday.setImageResource(R.drawable.frequency3_grey);
                    //自定义按钮先不亮，不为true，只有等到点击完成才会设置
                    selectedFrequency.setText("");

                    //datePickerDialog.setVibrate(isVibrate());
                    datePickerDialog.setYearRange(1985, 2028);
                    //datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                    datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);

                }

            }
        });

        button_everyday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iseveryday_clicked == 1) {
                    button_everyday.setImageResource(R.drawable.frequency1_grey);
                    iseveryday_clicked = 0;
                    frequency=0;
                    selectedFrequency.setText("");
                } else {
                    //若用户选择每天，其他按钮false
                    isuserdefine_clicked = 0;
                    isdayoverday_clicked = 0;
                    isthreeday_clicked = 0;
                    button_userdefine.setImageResource(R.drawable.frequency4_grey);
                    button_dayoverday.setImageResource(R.drawable.frequency2_grey);
                    button_threeday.setImageResource(R.drawable.frequency3_grey);
                    //选择每天按钮
                    button_everyday.setImageResource(R.drawable.frequency1);
                    iseveryday_clicked = 1;
                    frequency = 1;
                    selectedFrequency.setText("每天");
                }
            }
        });

        button_dayoverday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isdayoverday_clicked == 1) {
                    button_dayoverday.setImageResource(R.drawable.frequency2_grey);
                    isdayoverday_clicked = 0;
                    frequency=0;
                    selectedFrequency.setText("");
                } else {
                    //其他按钮false
                    iseveryday_clicked = 0;
                    isuserdefine_clicked = 0;
                    isthreeday_clicked = 0;
                    button_everyday.setImageResource(R.drawable.frequency1_grey);
                    button_userdefine.setImageResource(R.drawable.frequency4_grey);
                    button_threeday.setImageResource(R.drawable.frequency3_grey);
                    //选择隔一天按钮
                    button_dayoverday.setImageResource(R.drawable.frequency2);
                    isdayoverday_clicked = 1;
                    frequency = 2;
                    selectedFrequency.setText("隔一天");
                }
            }
        });

        button_threeday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isthreeday_clicked == 1) {
                    button_threeday.setImageResource(R.drawable.frequency3_grey);
                    isthreeday_clicked = 0;
                    frequency=0;
                    selectedFrequency.setText("");
                } else {
                    //其他false
                    iseveryday_clicked = 0;
                    isdayoverday_clicked = 0;
                    isuserdefine_clicked = 0;
                    button_everyday.setImageResource(R.drawable.frequency1_grey);
                    button_dayoverday.setImageResource(R.drawable.frequency2_grey);
                    button_userdefine.setImageResource(R.drawable.frequency4_grey);
                    //选择隔两天
                    button_threeday.setImageResource(R.drawable.frequency3);
                    isthreeday_clicked = 1;
                    frequency = 3;
                    selectedFrequency.setText("隔两天");
                }
            }
        });
        /**
         * ButtonGroup End
         */


        /**
         * ButtonGroup：可同时选择多个
         */
        button_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iswater_clicked == 1) {
                    button_water.setImageResource(R.drawable.action1_grey);
                    iswater_clicked = 0;
                } else {
                    button_water.setImageResource(R.drawable.action1);
                    iswater_clicked = 1;
                }
            }
        });

        button_sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (issun_clicked == 1) {
                    button_sun.setImageResource(R.drawable.action2_grey);
                    issun_clicked = 0;
                } else {
                    button_sun.setImageResource(R.drawable.action2);
                    issun_clicked = 1;
                }
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isback_clicked == 1) {
                    button_back.setImageResource(R.drawable.action3_grey);
                    isback_clicked = 0;
                } else {
                    button_back.setImageResource(R.drawable.action3);
                    isback_clicked = 1;
                }
            }
        });

        button_worm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isworm_clicked == 1) {
                    button_worm.setImageResource(R.drawable.action4_grey);
                    isworm_clicked = 0;
                } else {
                    button_worm.setImageResource(R.drawable.action4);
                    isworm_clicked = 1;
                }
            }
        });

        button_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ismedicine_clicked == 1) {
                    button_medicine.setImageResource(R.drawable.action5_grey);
                    ismedicine_clicked = 0;
                } else {
                    button_medicine.setImageResource(R.drawable.action5);
                    ismedicine_clicked = 1;
                }
            }
        });
        /**
         * ButtonGroup End
         */


        DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
        if (dpd != null) {
            dpd.setOnDateSetListener(this);
        }

        TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
        if (tpd != null) {
            tpd.setOnTimeSetListener(this);
        }



        //点击确认键，将所有东西存入数据库，
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalSelectedTime();

                //获得的是最新的闹钟内容
                alarm.roleColor=roleColor;
                alarm.content=editText.getText().toString();
                //默认改时间
                alarm.isAlarm=1;
                alarm.time = formatter.format(selectedTime);
                alarm.frequency=frequency;
                //alarm.plantName=
                alarm.water=iswater_clicked;
                alarm.sun=issun_clicked;
                alarm.takeBack=isback_clicked;
                alarm.takeCare=isworm_clicked;
                alarm.fertilization=ismedicine_clicked;

                if(alarm_Id==0){//新闹钟
                    //返回插入的闹钟的alarm_id值
                    alarm_Id=info.insert(alarm);
                    //一定要传alarm_Id,这个时候alarm.alarm_id还没更新，值为0
                    setAlarm(AddAlarmActivity.this,alarm.frequency,alarm.time,alarm_Id,0);
                    ToastUtil.showShort("New Alarm!");
                }else{//以前设的闹钟
                    info.update(alarm);
                    setAlarm(AddAlarmActivity.this,alarm.frequency,alarm.time,alarm.alarm_id,0);
                    ToastUtil.showShort("Alarm Update!");
                }
                ToastUtil.showShort(alarm.time);
                finish();
            }
        });


    }

    //最终的选择时间
    public void finalSelectedTime(){
        //获取当前时间
        currentTime=System.currentTimeMillis();
        String mtime;
        //时间处理
        ampm=numberPicker_ampm.getValue();
        hour=numberPicker_h.getValue();
        min=numberPicker_m.getValue();
        if(ampm==1){
            hour+=12;
        }
        String time= hour+":"+min;
        if(frequency==4){//自定义闹钟
            mtime=date+" "+time;
            try {
                Date date = formatter.parse(mtime);
                selectedTime = date.getTime();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{//非自定义
            //获取设置的时间
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                    (Calendar.DAY_OF_MONTH), hour, min, 10);
            selectedTime=calendar.getTimeInMillis();
            //若设置的时间比当前的小，则顺延到明天
            if(selectedTime<=currentTime) {
                selectedTime+=24*3600*1000;
            }
        }

    }


    /**
     * @param frequency       周期性时间间隔的标志
     * @param time            时间
     * @param id              闹钟的id
     * @param soundOrVibrator 2表示声音和震动都执行，1表示只有铃声提醒，0表示只有震动提醒
     */
    public static void setAlarm(Context context, int frequency, String time, int id, int soundOrVibrator) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long intervalMillis = 0;
        long selectedTime = 0;
        try {
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = formatter.parse(time);
            selectedTime = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (frequency == 4||frequency==0) {//自定义
            intervalMillis = 0;
        } else if (frequency == 1) {//每天
            intervalMillis = 24 * 3600 * 1000;
        } else if (frequency == 2) {//隔一天
            intervalMillis = 24 * 3600 * 1000 * 2;
        }else if(frequency==3){//隔两天
            intervalMillis=24 * 3600 * 1000 * 3;
        }
        //发送闹钟请求
        Intent intent = new Intent(context,AlarmReceiver.class);
        intent.putExtra("alarm_Id", id);
        intent.putExtra("frequency",frequency);
//        intent.putExtra("soundOrVibrator", soundOrVibrator);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, 0);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            am.setWindow(AlarmManager.RTC_WAKEUP, selectedTime, intervalMillis, sender);
//        } else {
            if (frequency == 4||frequency==0) {//自定义闹钟
                am.set(AlarmManager.RTC_WAKEUP, selectedTime, sender);
            } else {
                am.setRepeating(AlarmManager.RTC_WAKEUP, selectedTime, intervalMillis, sender);
            }
//        }
    }



    //自定义闹钟选择
    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        button_userdefine.setImageResource(R.drawable.frequency4);
        int ampm;//上午下午的值
        int hour;//NumberPicker的小时数
        int min;//NumberPicker的分钟数
        String mtime;//分钟数<10
        String htime;//小时数<10
        String ftime;//最终时间
        month++;//原生闹钟的错误
        date = year + "-" + month + "-" + day;

        //获取当前时间
        currentTime=System.currentTimeMillis();
        //时间处理
        ampm=numberPicker_ampm.getValue();
        hour=numberPicker_h.getValue();
        min=numberPicker_m.getValue();

        if(ampm==1){
            hour+=12;
        }
        htime=""+hour;
        mtime=""+min;
        if(hour<10){
            htime="0"+hour;
        }
        if(min<10){
            mtime="0"+min;
        }

        time= htime+":"+mtime;
        ftime=date+" "+time;
        try {
            Date date = formatter.parse(ftime);
            selectedTime = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(selectedTime<=currentTime){
            button_userdefine.setImageResource(R.drawable.frequency4_grey);
            isuserdefine_clicked = 0;
            frequency=0;
            date=null;
            ToastUtil.showShort("请设置大于当前时间的闹钟！");
            selectedFrequency.setText("");

        }else{
            ToastUtil.showShort(ftime);
        }
        isuserdefine_clicked=1;
        frequency = 4;
        selectedFrequency.setText(date);

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String hour=""+hourOfDay;//小于10的小时数
        String min=""+minute;//小于10的分钟数
        //获取当前时间
        currentTime=System.currentTimeMillis();
        //获取设置的时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), hourOfDay, minute, 10);
        selectedTime=calendar.getTimeInMillis();

        //设置NumberPicker显示的值
        if (hourOfDay < 12) {
            numberPicker_ampm.setValue(0);
            numberPicker_h.setValue(hourOfDay);
        } else {
            numberPicker_ampm.setValue(1);
            numberPicker_h.setValue(hourOfDay - 12);
        }
        numberPicker_m.setValue(minute);

        //若设置的时间比当前的小，则顺延到明天
        if(selectedTime<=currentTime) {
            selectedTime+=24*3600*1000;
        }


        if(hourOfDay<10){
            hour="0"+hourOfDay;
        }
        if(minute<10){
            min="0"+minute;
        }
        time = hour + ":" + min;
        ToastUtil.showShort(date);
    }


    //当点击edittext以外的地方，取消焦点，隐藏输入键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Create by Heloise
     * Begin
     */
    //就是他们三个找不着
    //莫名其妙啊
    private boolean isVibrate() {
        return true;
    }

    private boolean isCloseOnSingleTapDay() {
        return true;
    }

    private boolean isCloseOnSingleTapMinute() {
        return true;
    }

    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(getResources().getColor(R.color.greenborder)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    /**
     * End
     * Create by Heloise
     */



    @Override
    public void onLoadingNetworkData() {

    }

    @Override
    public void onLoadedNetworkData(View contentView) {

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_addalarm;
    }


}

