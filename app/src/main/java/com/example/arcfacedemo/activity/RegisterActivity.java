package com.example.arcfacedemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.example.arcfacedemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {


    @BindView(R.id.person_name_edt)
    EditText personNameEdt;
    @BindView(R.id.age_tv)
    TextView ageTv;
    @BindView(R.id.gender_tv)
    TextView genderTv;

    private String age;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);


        age = getIntent().getStringExtra("age");
        gender = getIntent().getStringExtra("gender");
        ageTv.setText(age);
        genderTv.setText(gender);
    }


    @OnClick(R.id.register_btn)
    public void onViewClicked() {
        String name = personNameEdt.getText().toString().trim();

    }
}
