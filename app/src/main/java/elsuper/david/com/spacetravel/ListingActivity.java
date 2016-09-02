package elsuper.david.com.spacetravel;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.design.widget.NavigationView;
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
import elsuper.david.com.spacetravel.fragment.FragmentFavorites;
import elsuper.david.com.spacetravel.fragment.FragmentListing;

/**
 * Created by Andrés David García Gómez.
 */
public class ListingActivity extends AppCompatActivity {

    //Controles de la Activity
    @BindView(R.id.listNav_toolbar) Toolbar toolbar;
    @BindView(R.id.listNav_view) NavigationView navigationView;
    @BindView(R.id.listNav_drawer) DrawerLayout drawerLayout;

    //Para almacenar el nombre del usuario logueado
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_navigation_activity);

        //Acceso a controles del fragment
        ButterKnife.bind(this);
        //Agregando el toolbar
        setSupportActionBar(toolbar);

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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();

                //Reemplazamos FrameLayout con el fragment seleccionado por el usuario
                switch (item.getItemId()){
                    case R.id.navigation_todayApodItem:
                        getFragmentManager().beginTransaction().replace(R.id.listNav_FragmentFolder, new FragmentApod()).commit();
                        return true;
                    case R.id.navigation_marsRoverItem:
                        getFragmentManager().beginTransaction().replace(R.id.listNav_FragmentFolder, new FragmentListing()).commit();
                        return true;
                    case R.id.navigation_favoriteItem:
                        //Si seleccionó el Fragment de "Favoritos, le pasamos el username
                        FragmentFavorites f = FragmentFavorites.newInstance(user);
                        getFragmentManager().beginTransaction().replace(R.id.listNav_FragmentFolder, f).commit();
                        return true;
                    default:
                        return false;
                }
            }
        });

        //Para abrir y cerrar el drawer
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
                    //Usando la información de logueo por Facebook
                    SimpleDraweeView userImage = (SimpleDraweeView) findViewById(R.id.header_sdvUserImage);
                    userImage.setImageURI("http://graph.facebook.com/" + object.getString("id") + "/picture?type=large");
                    TextView userName = (TextView) findViewById(R.id.header_tvUserName);
                    user = object.getString("name");
                    userName.setText(user);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        request.executeAsync();
    }
}