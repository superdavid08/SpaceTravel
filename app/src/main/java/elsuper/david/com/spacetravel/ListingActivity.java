package elsuper.david.com.spacetravel;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.fragment.FragmentApod;
import elsuper.david.com.spacetravel.fragment.FragmentFavorities;
import elsuper.david.com.spacetravel.fragment.FragmentListing;

public class ListingActivity extends AppCompatActivity {

    @BindView(R.id.listNav_toolbar) Toolbar toolbar;
    @BindView(R.id.listNav_view) NavigationView navigationView;
    @BindView(R.id.listNav_drawer) DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_navigation_activity);
        ButterKnife.bind(this);

        /*********************************************/
        /************ Para generar el hash ***********/
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "elsuper.david.com.spacetravel", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) { }
        catch (NoSuchAlgorithmException e) { }
        /*********************************************/
        /*********************************************/

        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                drawerLayout.closeDrawers();

                switch (item.getItemId()){
                    case R.id.navigation_todayApodItem:
                        getFragmentManager().beginTransaction().replace(R.id.listNav_FragmentFolder, new FragmentApod()).commit();
                        //Snackbar.make(findViewById(android.R.id.content), "Today Apod Item", Snackbar.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_marsRoverItem:
                        getFragmentManager().beginTransaction().replace(R.id.listNav_FragmentFolder, new FragmentListing()).commit();
                        //Snackbar.make(findViewById(android.R.id.content), "Mars Rover", Snackbar.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_favoriteItem:
                        getFragmentManager().beginTransaction().replace(R.id.listNav_FragmentFolder, new FragmentFavorities()).commit();
                        //Snackbar.make(findViewById(android.R.id.content), getString(R.string.listingNavigationMenu_favorities) , Snackbar.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getFBUserInfo();
    }

    private void getFBUserInfo() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try{
                    SimpleDraweeView userImage = (SimpleDraweeView) findViewById(R.id.header_sdvUserImage);
                    userImage.setImageURI("http://graph.facebook.com/" + object.getString("id") + "/picture?type=large");
                    TextView userName = (TextView) findViewById(R.id.header_tvUserName);
                    userName.setText(object.getString("name"));

                    //Log.d("nameFB",object.getString("name"));
                    //Log.d("idFB",object.getString("id"));
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        request.executeAsync();
    }
}