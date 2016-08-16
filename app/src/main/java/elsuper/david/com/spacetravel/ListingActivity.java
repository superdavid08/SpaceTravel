package elsuper.david.com.spacetravel;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.fragment.FragmentApod;
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
                        Snackbar.make(findViewById(android.R.id.content),
                                getString(R.string.listingNavigationMenu_favorities) , Snackbar.LENGTH_SHORT).show();
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
    }
}