package com.example.lence.bird_hunter.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lence.bird_hunter.R;
import com.example.lence.bird_hunter.utils.NetworkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginMVP {

    @BindView(R.id.login)
    EditText mLogin;
    @BindView(R.id.pass)
    EditText mPass;
    @BindView(R.id.button)
    Button mButton;
    ProgressDialog mDialog;
    LoginPresenter mLoginPresenter;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mSharedPreferences = getSharedPreferences("bird",MODE_PRIVATE);
        mLoginPresenter = new LoginPresenter(this);
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Выполняется вход");
        mDialog.setCancelable(true);


    }

    @OnClick(R.id.button)
    public void onViewClicked() {

        if ((mLogin.getText().length() != 0) && (mPass.getText().length() != 0)) {
            if (NetworkUtil.isNetworkConnected(this)) {
                mDialog.show();
                mLoginPresenter.autor(mLogin.getText().toString(), mPass.getText().toString());
            } else
                Toast.makeText(this, "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show();


    }


    @SuppressLint("ShowToast")
    @Override
    public void show(String res) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
        mDialog.dismiss();
    }

    @Override
    public void start(String string) {
        mSharedPreferences.edit().putString("id", string).commit();
        startActivity(new Intent(this, MainActivity.class));
    }
}
