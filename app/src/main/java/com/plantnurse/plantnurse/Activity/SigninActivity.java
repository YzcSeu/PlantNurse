package com.plantnurse.plantnurse.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.cache.ACache;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Network.LoginResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.ToastUtil;

import java.util.HashMap;


public class SigninActivity extends KSimpleBaseActivityImpl implements IBaseAction{

    private Button button_signup;
    private Button button_login;
    private ProgressDialog progressDialog;
    private TextView text;
    private TextView text2;
    private HashMap<String, String> loginParams;
    @Override
    public int initLocalData() {
        loginParams = new HashMap<>();
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        button_signup = (Button) findViewById(R.id.register_button);
        button_login = (Button) findViewById(R.id.login_button);
        text= (TextView) view.findViewById(R.id.userID);
        text2=  (TextView) view.findViewById(R.id.pwd);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在登录...");
    }

    @Override
    public void initController() {
        button_signup.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v)
            {
        /* 新建一个Intent对象 */
                Intent intent = new Intent();
                //intent.putExtra("name","LeiPei");
        /* 指定intent要启动的类 */
                intent.setClass(SigninActivity.this,SignupActivity.class);
        /* 启动一个新的Activity */
                SigninActivity.this.startActivity(intent);
        /* 关闭当前的Activity */
                SigninActivity.this.finish();
            }
        });

        button_login.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v)
            {
                progressDialog.show();
                String username=text.getText().toString();
                String password=text2.getText().toString();
                loginParams.put("userName", username);
                loginParams.put("password", password);
                SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(),getApplicationContext(),
                        LoginResponse.class, loginParams, Constants.SIGNIN_URL, NetworkTask.GET) {
                    @Override
                    public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                        LoginResponse loginResponse = (LoginResponse) result.resultObject;
                        if (loginResponse.getresponseCode() == 1) {
                            ToastUtil.showLong("登录成功");
                            ACache mCache = ACache.get(SigninActivity.this);
                            mCache.put("userName", loginResponse.getuserName(), 7 * ACache.TIME_DAY);
                            mCache.put("token", loginResponse.gettoken(), 7 * ACache.TIME_DAY);
                            finish();
                            progressDialog.dismiss();
                        } else if (loginResponse.getresponseCode() == 0) {
                            ToastUtil.showShort("登录失败：请先注册");
                            progressDialog.dismiss();
                        } else if (loginResponse.getresponseCode() == 2) {
                            ToastUtil.showShort("登录失败：该用户被冻结");
                            progressDialog.dismiss();
                        } else if (loginResponse.getresponseCode() == 3) {
                            ToastUtil.showShort("登录失败：密码错误");
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                    }

                });
            }
        });

    }

    @Override
    public void onLoadingNetworkData() {

    }

    @Override
    public void onLoadedNetworkData(View contentView) {

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_login;
    }
}