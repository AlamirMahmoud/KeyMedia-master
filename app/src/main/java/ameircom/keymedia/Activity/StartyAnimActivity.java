package ameircom.keymedia.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;

import ameircom.keymedia.AppManger.AppController;
import ameircom.keymedia.R;

public class StartyAnimActivity extends AppCompatActivity {
        View logoplay , logoKey , scaledView , logoname  , inplay ,  logoshadow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starty_anim);
        logoKey = findViewById(R.id.logoKey);
        logoplay = findViewById(R.id.logoplay);
        logoname = findViewById(R.id.logoName);
        scaledView = findViewById(R.id.scaledView);
        inplay = findViewById(R.id.inplay);
        logoshadow = findViewById(R.id.logoshadow);
        overridePendingTransition(0,0);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // your code to start second activity. Will wait for 3.5 seconds before calling this method
//                AppController.getInstance().getPrefManager().clear();
                if (AppController.getInstance().getPrefManager().getUser()==null)
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                else
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                finish();
            }
        }, 3500);

        scaledView.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(1500)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        inplay.animate().alpha(0).setDuration(500).setStartDelay(200).setInterpolator(new FastOutSlowInInterpolator()).start();
                        logoKey.animate().alpha(1).setDuration(500).setStartDelay(1200).setInterpolator(new FastOutSlowInInterpolator()).start();

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                     //   scaledView.animate().translationY(0).setDuration(200).setInterpolator(new AccelerateInterpolator());
                        logoshadow.animate().alpha(1).setStartDelay(50).setDuration(500).setInterpolator(new AccelerateInterpolator()).start();
                        logoname.animate().translationY(25f).alpha(1).setStartDelay(200).setDuration(500).setInterpolator(new FastOutSlowInInterpolator()).start();

                    }
                }).setStartDelay(1000).start();

        ;


    }



}
