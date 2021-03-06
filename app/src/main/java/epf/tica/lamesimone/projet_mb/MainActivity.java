package epf.tica.lamesimone.projet_mb;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static String mCurrentPhotoPath;
    private Bitmap image;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = -1;

    private static final String NAV_ITEM_ID = "navItemId";
    private DrawerLayout mDrawerLayout;
    private final Handler mDrawerActionHandler = new Handler();
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private int mNavItemId;

    public static String userEmail;
    public static String userUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // information utilisateur
        Intent intent = getIntent();
        userEmail = intent.getStringExtra(LoginActivity.EXTRA_MAIL);
        userUID = intent.getStringExtra(LoginActivity.EXTRA_UID);

        //TOOLBAR et ACTIONBAR
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (null == savedInstanceState) {
            mNavItemId = R.id.menu1;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        //AJOUT DE L'HEADER
        View header = LayoutInflater.from(this).inflate(R.layout.navigation_header, null);
        navigationView.addHeaderView(header);
        TextView textview1 = (TextView) header.findViewById(R.id.textView3);
        TextView textview2 = (TextView) header.findViewById(R.id.textView2);
        textview1.setText("identifiant: \n"+userEmail);
        textview2.setText("numero utilisateur: \n"+userUID);

        //navigation
        navigationView.getMenu().findItem(mNavItemId).setChecked(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        navigate(mNavItemId);

    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {

        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();


        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
            }
        }, DRAWER_CLOSE_DELAY_MS);

        return true;
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }

    private void navigate(final int itemId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (itemId) {
            case R.id.menu1:
                NewsFeedFragment newsfeed = new NewsFeedFragment();
                transaction.replace(R.id.content_frame, newsfeed);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.menu2:
                Bundle bundle = new Bundle();
                bundle.putString( "path",mCurrentPhotoPath);
                ImagePreviewFragment imagepreview = new ImagePreviewFragment();
                imagepreview.setArguments(bundle);
                transaction.replace(R.id.content_frame, imagepreview);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            default:


        }

    }
    }



