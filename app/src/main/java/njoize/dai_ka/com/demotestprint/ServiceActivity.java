package njoize.dai_ka.com.demotestprint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.zj.wfsdk.WifiCommunication;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServiceActivity extends AppCompatActivity {

    //    Explicit
    private MyConstant myConstant = new MyConstant();
    private String loginJSONString, nameString;
    private String tag = "1devV2";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean aBoolean = true; // true ==> Check Internet
    private WifiCommunication wifiCommunication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

//        Check Internet
        checkInternet();

//        Check Printer
        checkPrinter();

//        Get ValueLogin
        getValueLogin();


//        Create Toolbar
        createToolbar();

//        Add Fragment
        addFragment(savedInstanceState);


    } // Main Method

    private void checkPrinter() {

        MyConstant myConstant = new MyConstant();
        wifiCommunication = new WifiCommunication(handler);
        wifiCommunication.initSocket(myConstant.getIpAddressPrinter(), myConstant.getPortPrinter());


    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == WifiCommunication.WFPRINTER_CONNECTED) {
                Log.d("23decV1", "Printer Connected");
                Toast.makeText(ServiceActivity.this, "Check Connected Printer OK", Toast.LENGTH_SHORT).show();
                wifiCommunication.close();
            } else {
                Log.d("23decV1", "Printer Cannot Connected");
            }



        }
    };

    private void checkInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isConnected() && aBoolean)) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ServiceActivity.this);
            alertDialogBuilder.setTitle("Check Internet").setMessage("Cannot Connected Internet").setPositiveButton("Exit App", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    dialog.dismiss();
                }
            }).setNegativeButton("Continue App", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    aBoolean = false;
                    dialog.dismiss();
                }
            }).show();

        }
    }

    private void addFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentServiceFragment, new ServiceFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);



    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        actionBarDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();

    }

    private void getValueLogin() {
        loginJSONString = getIntent().getStringExtra("Login");
        Log.d(tag, "JSON receive ==> " + loginJSONString);

        try {

            JSONArray jsonArray = new JSONArray(loginJSONString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            nameString = jsonObject.getString("name");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarService);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(myConstant.getNameShopString());
        getSupportActionBar().setSubtitle("Login by : " + nameString);

        drawerLayout = findViewById(R.id.layoutDrawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                ServiceActivity.this,
                drawerLayout,
                R.string.open,
                R.string.close);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_hamberger);



    }
} // Main Class
