package org.chicktech.chicktech.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.chicktech.chicktech.R;

public class ShowPopUpActivity extends Activity implements View.OnClickListener {
    private Button ok;
    private Button cancel;
    private boolean click = true;
    private String objectID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Event Reminder!");

        Intent i = getIntent();
        objectID = i.getStringExtra("id");

        setContentView(R.layout.activity_show_pop_up);

        ok = (Button)findViewById(R.id.popOkB);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowPopUpActivity.this, EventDetailActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                i.putExtra("id", objectID);
                getApplicationContext().startActivity(i);
                finish();
            }
        });

        cancel = (Button)findViewById(R.id.popCancelB);
        cancel.setOnClickListener(this);

        TextView tvMessage = (TextView) findViewById(R.id.durationTitle);
        tvMessage.setText("View this event? " + objectID);
    }
    @Override
    public void onClick(View button) {
        // cancel
        finish(); // kill the popup no matter what
    }
}