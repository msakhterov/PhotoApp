package ru.msakhterov.photoapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.msakhterov.photoapp.App;
import ru.msakhterov.photoapp.R;
import ru.msakhterov.photoapp.mvp.presenters.MainPresenter;
import ru.msakhterov.photoapp.mvp.views.MainView;
import ru.msakhterov.photoapp.ui.fragments.DBFragment;
import ru.msakhterov.photoapp.ui.fragments.MainFragment;
import ru.msakhterov.photoapp.ui.fragments.PhotoListUpdatable;
import ru.msakhterov.photoapp.ui.fragments.WebFragment;
import ru.msakhterov.photoapp.utils.Constants;
import timber.log.Timber;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    private static final int REQUEST_PHOTO = 0;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.floating_action_button)
    FloatingActionButton fab;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            toolbar.setTitle(item.getTitle());
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectBottomView(Constants.MAIN_TAG);
                    fab.show();
                    return true;
                case R.id.navigation_dashboard:
                    selectBottomView(Constants.DB_TAG);
                    fab.hide();
                    return true;
                case R.id.navigation_notifications:
                    selectBottomView(Constants.WEB_TAG);
                    fab.hide();
                    return true;
            }
            return false;
        }
    };

    @InjectPresenter
    MainPresenter presenter;

    @ProvidePresenter
    public MainPresenter provideMainPresenter() {
        MainPresenter presenter = new MainPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);

        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getInstance().getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bindNavigationDrawer();
        initTitle();
        selectBottomView(Constants.MAIN_TAG);
    }

    private void initTitle() {
        toolbar.post(() -> toolbar.setTitle(navigation.getMenu().getItem(0).getTitle()));
    }

    private void bindNavigationDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_tool) {
                    showMessage("Tool");
                } else if (id == R.id.nav_share) {
                    showMessage("Share");
                } else if (id == R.id.nav_gallery) {
                    showMessage("Gallery");
                } else if (id == R.id.nav_send) {
                    showMessage("Send");
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @OnClick(R.id.floating_action_button)
    void onFabClick() {
        presenter.addNewPhoto();
    }

    @Override
    public void addNewPhoto(File photoFile) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            if (photoFile != null) {
                Uri uri = FileProvider.getUriForFile(this,
                        getString(R.string.file_provider_authorities),
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(cameraIntent, REQUEST_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PHOTO) {
                Uri uri = FileProvider.getUriForFile(this,
                        getString(R.string.file_provider_authorities),
                        presenter.getPhotoFile());
                revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                presenter.saveNewPhoto();
            }
        }
    }

    private void selectBottomView(String screenTag) {
    FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = findVisibleFragment(fm);
        Fragment newFragment = fm.findFragmentByTag(screenTag);
        if (currentFragment != null && newFragment != null && currentFragment == newFragment){
            return;
        }
        FragmentTransaction transaction = fm.beginTransaction();
        if (newFragment == null) {
            switch (screenTag){
                case Constants.MAIN_TAG:
                    newFragment = MainFragment.newInstance();
                    break;
                case Constants.DB_TAG:
                    newFragment = DBFragment.newInstance();
                    break;
                case Constants.WEB_TAG:
                    newFragment = WebFragment.newInstance();
                    break;
                default:
                    break;
            }
            transaction.add(R.id.main_container, newFragment, screenTag);
        }

        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        if (newFragment != null) {
            transaction.show(newFragment);
        }
        transaction.commitNow();
    }

    private Fragment findVisibleFragment(FragmentManager fm){
        Fragment currentFragment = null;
        List<Fragment> fragments = fm.getFragments();
        if (fragments.size() != 0) {
            for (Fragment f : fragments) {
                if (f.isVisible()) {
                    currentFragment = f;
                    break;
                }
            }
        }
        return currentFragment;
    }

    public void onBackPressed() {
        Fragment fragment = findVisibleFragment(getSupportFragmentManager());
        if (fragment instanceof MainFragment){
            finish();
        } else {
            selectBottomView(Constants.MAIN_TAG);
            fab.show();
        }
    }

    @Override
    public void updatePicturesList() {
        Fragment fragment = findVisibleFragment(getSupportFragmentManager());
        if (fragment instanceof PhotoListUpdatable){
            PhotoListUpdatable updatableFragment = (PhotoListUpdatable)fragment;
            Timber.e("MainActivity updateUI");
            updatableFragment.updateUI();
        }
    }

    public void showMessage(String message) {
        Snackbar.make(navigation, message, Snackbar.LENGTH_LONG).show();
    }
}
