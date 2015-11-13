package com.csxz.snowflack.custompulltorefresh;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView= (TextView) findViewById(R.id.test);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                int s = 0;
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        Log.d("sss", event.getRawY() + "");
                        textView.setPadding(0,0, (int) event.getRawY(),0);

                        break;

                }
                return false;
            }
        });
    }


}
