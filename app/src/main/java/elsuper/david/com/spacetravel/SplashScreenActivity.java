package elsuper.david.com.spacetravel;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.login.FBLoginActivity;

public class SplashScreenActivity extends AppCompatActivity {

    //Controles del fragment
    @BindView(R.id.splash_imageView) ImageView imageView;
    @BindView(R.id.splash_ProgressBar) ProgressBar progressBar;

    public static final int seconds = 4;
    public static final int miliSeconds = seconds * 1000;
    public static final int delay = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inflamos la vista
        setContentView(R.layout.activity_splash_screen);
        //Acceso a controles de la Activity
        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(maxPreogress());
        startSplash();
    }

    private void startSplash() {
        new CountDownTimer(miliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(setProgressooo(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Intent activityMain = new Intent(SplashScreenActivity.this,FBLoginActivity.class);
                startActivity(activityMain);
                progressBar.setVisibility(View.GONE);
                finish();
            }
        }.start();
    }

    public int setProgressooo(long milisecondsSubs){
        return  (int)((miliSeconds - milisecondsSubs) / 1000);
    }

    public int maxPreogress(){
        return seconds - delay;
    }
}
