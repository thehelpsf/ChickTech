package org.chicktech.chicktech.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.chicktech.chicktech.R;

public class ShowPopUpActivity extends Activity implements View.OnClickListener {
    Button ok;
    Button cancel;
    boolean click = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Event Reminder!");

        setContentView(R.layout.activity_show_pop_up);
        ok = (Button)findViewById(R.id.popOkB);
        ok.setOnClickListener(this);
        cancel = (Button)findViewById(R.id.popCancelB);
        cancel.setOnClickListener(this);

        Intent i = getIntent();
        String message = i.getStringExtra("message");
        TextView tvMessage = (TextView) findViewById(R.id.durationTitle);
        tvMessage.setText(message);
    }
    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        finish();
    }
}