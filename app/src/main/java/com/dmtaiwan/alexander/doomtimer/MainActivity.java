package com.dmtaiwan.alexander.doomtimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText inputText;
    private TextView outputText;
    private Button startButton;
    private Button stopButton;
    private Button resetButton;
    private ImageView bubuImage;
    private CountDownTimer timer;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private boolean bubuStarted = false;

    private int lastInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //bind views
        inputText = (EditText) findViewById(R.id.input_text);
        outputText = (TextView) findViewById(R.id.output_text);
        startButton = (Button) findViewById(R.id.start_button);
        stopButton = (Button) findViewById(R.id.stop_button);
        resetButton = (Button) findViewById(R.id.reset_button);
        bubuImage = (ImageView) findViewById(R.id.bubu);

        //set onClickListeners
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_button:
                if (inputText.getText().length() > 0) {
                    int input = Integer.parseInt(inputText.getText().toString());
                    startTimer(input);
                    lastInput = input;
                    inputText.setText("");

                }else {
                    Toast.makeText(MainActivity.this, "Enter a time", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.stop_button:

                timer.cancel();
                outputText.setText("");
                inputText.setText("");
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();

                }
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                }
                stopBubu();

                break;
            case R.id.reset_button:
                timer.cancel();

                inputText.setText("");
                outputText.setText(String.valueOf(lastInput));
                startTimer(lastInput);
                stopBubu();
                Log.i("TEST", lastInput + "");
                Log.i("RESET", "RESET");
                break;
        }
    }

    private void startTimer(final int input) {
        int inputMilis = (input +1)* 1000;

        timer = new CountDownTimer(inputMilis, 1000-500) {

            int ticks = 0;
            @Override
            public void onTick(long timeUntilFinished) {
                ticks++;

                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();

                }
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                }

                Log.i("COUNT", Math.round(Double.valueOf(timeUntilFinished/1000))+"");
                outputText.setText(Math.round(Double.valueOf(timeUntilFinished/1000))+"");
                if (ticks % 2 != 0) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wah);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.start();
                }



            }

            @Override
            public void onFinish() {
                Log.i("DONE", "DONE");
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();

                }
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.uh_oh);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                startBubu();
            }
        }.start();
    }

    private Runnable bubuRunnable = new Runnable() {
        @Override
        public void run() {
            if (bubuImage.getVisibility() == View.GONE) {
                bubuImage.setVisibility(View.VISIBLE);
            } else bubuImage.setVisibility(View.GONE);
            startBubu();
        }
    };

    private void startBubu() {
        bubuStarted = true;
        handler.postDelayed(bubuRunnable, 500);
    }

    private void stopBubu() {
        bubuImage.setVisibility(View.GONE);
        handler.removeCallbacks(bubuRunnable);
    }
}
