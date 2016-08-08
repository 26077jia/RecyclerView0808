package com.example.jiajia.recyclerview0808;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn_linear,btn_gride,btn_stragged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        btn_linear = (Button)findViewById(R.id.btn_linear);
        btn_gride = (Button)findViewById(R.id.btn_grider);
        btn_stragged = (Button)findViewById(R.id.btn_stragged);
    }

    private void initEvent() {
        btn_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MyLinearLayout.class);
            }
        });
        btn_gride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_stragged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
