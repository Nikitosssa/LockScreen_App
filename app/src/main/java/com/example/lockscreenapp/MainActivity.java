package com.example.lockscreenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.KeyguardManager;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERM_CODE = 0;



    private LockscreenUtils mLockscreenUtils;
    int firstRightAnswer,
        firstUserAnswer,
        secondRightAnswer,
        secondUserAnswer,
        thirdRightAnswer,
        thirdUserAnswer;
    @Override
    public void onAttachedToWindow() {
        this.getWindow().setType(
                WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        this.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );

        super.onAttachedToWindow();
    }

    private class StateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    unlockHomeButton();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        // unlock screen in case of app get killed by system
        if (getIntent() != null && getIntent().hasExtra("kill")
                && getIntent().getExtras().getInt("kill") == 1) {
            enableKeyguard();
            unlockHomeButton();
        } else {

            try {
                // disable keyguard
                disableKeyguard();

                // lock home button
                lockHomeButton();

                // start service for observing intents
                startService(new Intent(this, LockscreenService.class));

                // listen the events get fired during the call
                StateListener phoneStateListener = new StateListener();
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                telephonyManager.listen(phoneStateListener,
                        PhoneStateListener.LISTEN_CALL_STATE);

            } catch (Exception e) {
            }

        }

        Button checkAnswerButton = (Button)findViewById(R.id.checkAnswerButton);

        TextView firstProblem = (TextView)findViewById(R.id.firstCase);
        TextView secondProblem = (TextView)findViewById(R.id.secondCase);
        TextView thirdProblem = (TextView)findViewById(R.id.thirdCase);



        String[] perms = new String[] { Manifest.permission.READ_EXTERNAL_STORAGE };

        if (!(ContextCompat.checkSelfPermission(MainActivity.this, perms[0]) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, perms, REQUEST_PERM_CODE);
        } else continueAfterRequest();

        int selectedLevel = 5;
        ProblemGenerator problemGenerator = new ProblemGenerator();

        problemGenerator.generate(selectedLevel);
        firstProblem.setText(problemGenerator.problemString+"=");
        firstRightAnswer = problemGenerator.acceptAnswer;

        problemGenerator.generate(selectedLevel);
        secondProblem.setText(problemGenerator.problemString+"=");
        secondRightAnswer = problemGenerator.acceptAnswer;

        problemGenerator.generate(selectedLevel);
        thirdProblem.setText(problemGenerator.problemString+"=");
        thirdRightAnswer = problemGenerator.acceptAnswer;
    }




    // Lock home button
    public void lockHomeButton() {
        mLockscreenUtils.lock(MainActivity.this);
    }

    // Unlock home button and wait for its callback
    public void unlockHomeButton() {
        mLockscreenUtils.unlock();
    }

    // Simply unlock device when home button is successfully unlocked
    public void onLockStatusChanged(boolean isLocked) {
        if (!isLocked) {
            unlockDevice();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unlockHomeButton();
    }

    @SuppressWarnings("deprecation")
    private void disableKeyguard() {
        KeyguardManager mKM = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock mKL = mKM.newKeyguardLock("IN");
        mKL.disableKeyguard();
    }

    @SuppressWarnings("deprecation")
    private void enableKeyguard() {
        KeyguardManager mKM = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock mKL = mKM.newKeyguardLock("IN");
        mKL.reenableKeyguard();
    }




            @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERM_CODE){
            for(int result : grantResults){
                Log.d("LockScreenApp","Grant result: " + result);
            }

            continueAfterRequest();
        }
    }

    private void continueAfterRequest(){
        final Drawable wallpaperDrawable = WallpaperManager.getInstance(this).getDrawable();

        ImageView imageView = (ImageView)findViewById(R.id.wallpaper);
        imageView.setImageDrawable(wallpaperDrawable);

        BitmapDrawable originalDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap originalBitmap = originalDrawable.getBitmap();

        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
        imageView.setImageBitmap(blurredBitmap);


    }

    public void onClickCheckAnswers(View view) {


    }

    private void unlockDevice()
    {
        finish();
    }

    private void init() {
        mLockscreenUtils = new LockscreenUtils();
        Button btnUnlock = (Button) findViewById(R.id.checkAnswerButton);
        btnUnlock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // unlock home button and then screen on button press
                boolean isRight = false;

                EditText firstAnswer = (EditText)findViewById(R.id.firstAnswer);
                firstUserAnswer = Integer.parseInt(firstAnswer.getText().toString());

                EditText secondAnswer = (EditText)findViewById(R.id.secondAnswer);
                secondUserAnswer = Integer.parseInt(secondAnswer.getText().toString());

                EditText thirdAnswer = (EditText)findViewById(R.id.thirdAnswer);
                thirdUserAnswer = Integer.parseInt(thirdAnswer.getText().toString());

                Log.d("FIRST ANSWEARS",firstRightAnswer +" AND "+ firstUserAnswer);
                Log.d("SECOND ANSWEARS",secondRightAnswer +" AND "+ secondUserAnswer);
                Log.d("THIRD ANSWEARS",thirdRightAnswer +" AND "+ thirdUserAnswer);
                if (firstRightAnswer == firstUserAnswer &&
                        secondRightAnswer == secondUserAnswer &&
                        thirdRightAnswer == thirdUserAnswer){

                    //Toast.makeText(this, "Добро пожаловать!!!", Toast.LENGTH_SHORT).show();
                    isRight = true;
                }else {
                    //Toast.makeText(this, "Не верно!!!", Toast.LENGTH_SHORT).show();
                }
                if (isRight){isRight = false; finish();unlockHomeButton();}

            }
        });
    }
}