package elsuper.david.com.spacetravel.login;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.ListingActivity;
import elsuper.david.com.spacetravel.R;

/**
 * Created by Andrés David García Gómez.
 */
public class FBLoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult>{

    @BindView(R.id.fb_login_button) LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fblogin);
        ButterKnife.bind(this);

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager,this);

        if(AccessToken.getCurrentAccessToken() != null)
            Snackbar.make(findViewById(android.R.id.content),
                    getString(R.string.FB_msgLogin), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        startActivity(new Intent(FBLoginActivity.this, ListingActivity.class));
    }

    @Override
    public void onCancel() {
        Snackbar.make(findViewById(android.R.id.content),
                getString(R.string.FB_msgCancelLogin), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onError(FacebookException error) {
        Snackbar.make(findViewById(android.R.id.content), error.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}