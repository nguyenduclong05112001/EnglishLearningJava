package com.example.englishlearn.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.englishlearn.R;
import com.example.englishlearn.api.APIConst;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.fragments.EditProfileFragment;
import com.example.englishlearn.fragments.StoryFragment;
import com.example.englishlearn.fragments.StudyFragment;
import com.example.englishlearn.fragments.TestOfUserFragment;
import com.example.englishlearn.handlerothers.RealPathUtil;
import com.example.englishlearn.models.Achievementofuser;
import com.example.englishlearn.models.User;
import com.example.englishlearn.sharedpreferences.DataLocalManage;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private long timeBackPressed;
    private NavigationView mNavigationView;
    private TextView lbnameofuser, lbusernameofuser;
    private TextView numberpoint, numberdaychain;
    private ImageView igavatarofuser;
    private ProgressDialog mDialog;
    private int reqiest_code_permission = 999;
    private Uri mUri = null;

    private static final int FRAGMENT_STUDY = 0;
    private static final int FRAGMENT_STORY = 1;
    private static final int FRAGMENT_EDIT_PROFILE = 2;
    private static final int FRAGMENT_TEST_OF_USERS = 3;

    private int CURRENT_PAGE = FRAGMENT_STUDY;

    private ActivityResultLauncher launch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        mUri = uri;
                        uploadAvatartoServer();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mappingElements();
        checkUsernameChanged();
        eventElements();
    }

    private void getAchievementofuser(User user) {
        mDialog.show();
        APIServer.api.getAchievement(user.getUsername()).enqueue(new Callback<Achievementofuser>() {
            @Override
            public void onResponse(Call<Achievementofuser> call, Response<Achievementofuser> response) {
                Achievementofuser achievement = response.body();
                if (achievement != null) {
                    numberpoint.setText(String.valueOf(achievement.getPointofday()));
                    numberdaychain.setText(String.valueOf(achievement.getChain()));
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Achievementofuser> call, Throwable t) {
                mDialog.dismiss();
                Log.d("TAG", "onFailure: " + t.toString());
                Toast.makeText(HomeActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void eventElements() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.lmn_study:
                        if (CURRENT_PAGE != FRAGMENT_STUDY) {
                            replaceFragment(new StudyFragment());
                            CURRENT_PAGE = FRAGMENT_STUDY;
                        }
                        break;

                    case R.id.lmn_manga:
                        if (CURRENT_PAGE != FRAGMENT_STORY) {
                            replaceFragment(new StoryFragment());
                            CURRENT_PAGE = FRAGMENT_STORY;
                        }
                        break;

                    case R.id.lmn_testofuser:
                        if (CURRENT_PAGE != FRAGMENT_TEST_OF_USERS) {
                            replaceFragment(new TestOfUserFragment());
                            CURRENT_PAGE = FRAGMENT_TEST_OF_USERS;
                        }
                        break;

                    case R.id.lmn_editinfo:
                        if (CURRENT_PAGE != FRAGMENT_EDIT_PROFILE) {
                            replaceFragment(new EditProfileFragment());
                            CURRENT_PAGE = FRAGMENT_EDIT_PROFILE;
                        }
                        break;

                    case R.id.lmn_language:
                        showDialogChangeLanguage();
                        break;

                    case R.id.lmn_logout:
                        showDialogConfirmLogout();
                        break;
                }

                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        igavatarofuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPermissionMedia();
            }
        });
    }

    private void showDialogChangeLanguage() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_language);

        ImageView vn = dialog.findViewById(R.id.dclg_vi);
        ImageView en = dialog.findViewById(R.id.dclg_en);

        vn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("vi");
                dialog.dismiss();
            }
        });

        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
                dialog.dismiss();
            }
        });

        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER_VERTICAL;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }

    public void setLocale(String language) {
        Locale locale = new Locale(language);
        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config
        ,getBaseContext().getResources().getDisplayMetrics());

        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }

    public void checkUsernameChanged() {
        String username = DataLocalManage.getUsertoCheck();
        mDialog.show();
        APIServer.api.getInforUser(username).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user == null) {
                    Toast.makeText(HomeActivity.this, R.string.errorr_pass_changed, Toast.LENGTH_LONG).show();
                    toActivity(LoginActivity.class);
                    finishAffinity();
                    return;
                }
                setInfoUser(user);
                getAchievementofuser(user);
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(HomeActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setInfoUser(User user) {
        lbnameofuser.setText(user.getName());
        lbusernameofuser.setText(user.getUsername());
        Glide.with(this).load(APIConst.baseUrl + APIConst.storeImage + user.getAvatar()).error(R.drawable.avatardefault).into(igavatarofuser);
    }

    private void mappingElements() {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));
        mDialog.setCancelable(false);
        Toolbar toolbar = findViewById(R.id.ha_toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.ha_drawrlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.DrawerLayout_open,
                R.string.DrawerLayout_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.ha_navigation);

        lbnameofuser = mNavigationView.getHeaderView(0).findViewById(R.id.lhn_name);
        lbusernameofuser = mNavigationView.getHeaderView(0).findViewById(R.id.lhn_username);
        igavatarofuser = mNavigationView.getHeaderView(0).findViewById(R.id.lhn_avatar);

        numberpoint = findViewById(R.id.ha_numberpoint);
        numberdaychain = findViewById(R.id.ha_numberchain);

        replaceFragment(new StudyFragment());
        mNavigationView.getMenu().findItem(R.id.lmn_study).setChecked(true);
    }

    private void showDialogConfirmLogout() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_confirm_logout);

        Button btnNo = dialog.findViewById(R.id.dcl_btnno);
        Button btnYes = dialog.findViewById(R.id.dcl_btnyes);

        btnNo.setOnClickListener(view -> dialog.dismiss());

        btnYes.setOnClickListener(view -> {
            dialog.dismiss();
            DataLocalManage.setIsLogin(false);
            toActivity(LoginActivity.class);
            finishAffinity();
        });

        dialog.show();
    }

    private void toActivity(Class<?> to) {
        Intent intent = new Intent(this, to);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (timeBackPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(HomeActivity.this, R.string.press_back_again, Toast.LENGTH_LONG).show();
            }
            timeBackPressed = System.currentTimeMillis();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ha_layoutmain, fragment);
        transaction.commit();
    }

    private void CheckPermissionMedia() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openAndGetImage();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, reqiest_code_permission);
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openAndGetImage();
            }
        }
    }

    private void openAndGetImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        launch.launch(intent);
    }

    private void uploadAvatartoServer() {
        if (mUri == null) {
            Log.d("TAG", "uploadAvatartoServer: " + mUri);
        }
        RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), DataLocalManage.getUsertoCheck());
        String fileString = RealPathUtil.getRealPath(this, mUri);
        File file = new File(fileString);
        RequestBody avatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part avartupload = MultipartBody.Part.createFormData(APIConst.AVATAR, file.getName(), avatar);
        mDialog.show();
        APIServer.api.uploadAvatar(username, avartupload).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String resul = response.body();
                if (resul.equals("Success")) {
                    Toast.makeText(HomeActivity.this, R.string.update_avatar_success, Toast.LENGTH_LONG).show();
                    checkUsernameChanged();
                } else {
                    Toast.makeText(HomeActivity.this, R.string.update_avatar_errorr, Toast.LENGTH_LONG).show();
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mDialog.dismiss();
                Log.d("TAG", "onFailure: " + t.toString());
                Toast.makeText(HomeActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}