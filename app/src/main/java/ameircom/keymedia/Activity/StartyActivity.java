package ameircom.keymedia.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ameircom.keymedia.R;

public class StartyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sp);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // your code to start second activity. Will wait for 3 seconds before calling this method

                startActivity(new Intent(StartyActivity.this,StartyAnimActivity.class));
                finish();
            }
        }, 200);




    }



}
