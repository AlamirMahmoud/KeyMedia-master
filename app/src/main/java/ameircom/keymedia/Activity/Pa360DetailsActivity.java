package ameircom.keymedia.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.panoramagl.PLCamera;
import com.panoramagl.PLImage;
import com.panoramagl.PLManager;
import com.panoramagl.PLSphericalPanorama;
import com.panoramagl.enumerations.PLCubeFaceOrientation;
import com.panoramagl.enumerations.PLTextureColorFormat;
import com.panoramagl.utils.PLUtils;

import ameircom.keymedia.AppManger.Config;
import ameircom.keymedia.R;

public class Pa360DetailsActivity extends AppCompatActivity {
    String img  ;
    PLManager plManager;
    PLSphericalPanorama panorama ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        img = getIntent().getExtras().getString("img") ;
        plManager = new PLManager(this);
        plManager.setContentView(R.layout.activity_pa360_details);
        plManager.onCreate();

        panorama = new PLSphericalPanorama();
        panorama.getCamera().lookAt(30.0f, 90.0f);
        SimpleTarget simpleTarget = new SimpleTarget<GlideBitmapDrawable>() {
            @Override
            public void onResourceReady(GlideBitmapDrawable bitmap, GlideAnimation glideAnimation) {

//        panorama.setImage(new PLImage(, false),PLCubeFaceOrientation.PLCubeFaceOrientationFront);



                panorama.setImage(new PLImage(Bitmap.createScaledBitmap(bitmap.getBitmap(), 2048, 2048, false), false  ));
                plManager.setPanorama(panorama);


            }
        } ;
        Glide.with(getApplicationContext())
                .load(Config.img_url+ img)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(simpleTarget) ;

//

    }


    @Override
    protected void onResume() {
        super.onResume();
        plManager.onResume();
    }

    @Override
    protected void onPause() {
        plManager.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        plManager.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return plManager.onTouchEvent(event);
    }
}

