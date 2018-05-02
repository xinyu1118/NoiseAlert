package io.github.noisealert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.github.privacysecurer.core.AudioCallback;
import io.github.privacysecurer.core.AudioEvent;
import io.github.privacysecurer.core.Event;
import io.github.privacysecurer.core.UQI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final UQI uqi = new UQI(this);
        final Context context = getApplicationContext();

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the loudness threshold set by developers
                double threshold = getEditTextDouble(R.id.editText);

                // implement noise monitoring using PrivacySecurer
                final Event audioEvent = new AudioEvent.AudioEventBuilder()
                        .setFieldName(AudioEvent.AvgLoudness)
                        .setOperator(AudioEvent.GTE)
                        .setThreshold(threshold)
                        .setDuration(1000)
                        .setInterval(10*1000)
                        .build();
                uqi.addEventListener(audioEvent, new AudioCallback() {
                    @Override
                    public void onEvent() {
                        //Log.d("Log", String.valueOf(this.getAvgLoudness()));
                        if (audioEvent.getSatisfyCond()) {
                            //Log.d("Log", "Event happens!");
                            Looper.prepare();
                            showDialog();
                            Looper.loop();
                        }
                    }
                });

            }
        });

    }

    private double getEditTextDouble(int id) {
        EditText editText = (EditText)findViewById(id);
        String s = editText.getText().toString();
        if (s.equals("") || s.equals("."))
            return 0;
        else
            return Double.parseDouble(s);
    }

    private void showDialog() {
       AlertDialog alertDialog = new AlertDialog.Builder(this).create();
       alertDialog.setMessage("Noise Alert!");
       alertDialog.show();
    }
}
