package app.com.gitlib.activities.onboard;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import app.com.gitlib.R;
import app.com.gitlib.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding activitySplashBinding;
    private Animation topAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        init();
        bindUIWithComponents();
    }

    private void init() {
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
    }

    private void bindUIWithComponents() {
        activitySplashBinding.appIcon.setAnimation(topAnim);
        activitySplashBinding.bottomDetails.setAnimation(bottomAnim);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this,HomeActivity.class));
            }
        }, 1500);

    }
}
