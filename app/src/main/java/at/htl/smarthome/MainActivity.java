package at.htl.smarthome;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import at.htl.smarthome.api.HomematicRpcHandler;
import at.htl.smarthome.commandservice.CommandInterpreterService;
import at.htl.smarthome.entity.Settings;
import at.htl.smarthome.view.ControlFragment;
import at.htl.smarthome.view.LedWallFragment;
import at.htl.smarthome.view.MainFragment;


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    BroadcastReceiver motionDetectorReceiver;
    MainFragment mainFragment;
    LedWallFragment ledWallFragment;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    boolean isMotionDetectedInLastMinute = true;
    PowerManager.WakeLock wakeLock;
    // Binder bilden das Schnittstellenobjekt (Contract) zum Service
    CommandInterpreterService.CommandInterpreterServiceBinder commandInterpreterServiceBinder;
    private Handler handler;
    /**
     * Wird alle 60 Sekunden per post aufgerufen.
     * Überprüft, ob in der letzten Minute eine
     * Bewegung auftrat und schaltet, falls nicht
     * den Bildschirm ab.
     */
    private Runnable controlScreenState = new Runnable() {
        @Override
        public void run() {
            if (!isMotionDetectedInLastMinute) {
                turnOffScreen();
            }
            isMotionDetectedInLastMinute = false;
            handler.postDelayed(controlScreenState, 60000);
        }
    };
    // ServiceConnection repräsentiert die Verbindung zum Service
    private ServiceConnection commandInterpreterServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            commandInterpreterServiceBinder = ((CommandInterpreterService.CommandInterpreterServiceBinder) binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            commandInterpreterServiceBinder = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        String ipHomematic = Settings.getInstance(this).getHomematicIp();
        String url = "http://" + ipHomematic + ":2001";
        try {
            HomematicRpcHandler getEventsTask = new HomematicRpcHandler();
            getEventsTask.initXmlRpcClient(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // Direkte Ableitung und Instanzierung des abstrakten Broadcastreceivers
        // Bewegung erkannt
        motionDetectorReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(LOG_TAG, "motionDetectorReceiver-onReceive()");
                Toast.makeText(getBaseContext(), "Motion detected",
                        Toast.LENGTH_SHORT).show();
                isMotionDetectedInLastMinute = true;
                turnOnScreen();
            }
        };
        handler = new Handler();
        handler.post(controlScreenState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_refresh:
                mainFragment.refreshData();
                return true;
            case R.id.menu_quit:
                finish();
                return true;
            case R.id.menu_action_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;
            case R.id.menu_action_ip:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("IP-Adresse des Handys");
                alertDialogBuilder
                        .setMessage(getLocalIpAddress())
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog ipDialog = alertDialogBuilder.create();
                ipDialog.show();
                return true;
        }
        return false;
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @return address or empty string
     */
    public String getLocalIpAddress() {
        try {
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                List<InetAddress> addresses = Collections.list(networkInterface
                        .getInetAddresses());
                for (InetAddress address : addresses) {
                    if (!address.isLoopbackAddress()) {
                        String addressAsString = address.getHostAddress().toUpperCase(Locale.GERMAN);
                        @SuppressWarnings("deprecation") boolean isIPv4 = InetAddressUtils.isIPv4Address(addressAsString);
                        if (isIPv4) {
                            return addressAsString;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, "getLocalIpAddress() Exception: " + ex.toString());
        }
        return "";
    }

    /**
     * Beide Broadcastreceiver registrieren
     */
    public void onResume() {
        Log.d(LOG_TAG, "onResume() Broadcastreceiver registriert");
        super.onResume();
        // Broadcastreceiver für entsprechende Actions registrieren
        registerReceiver(motionDetectorReceiver, new IntentFilter("org.motion.detector.ACTION_GLOBAL_BROADCAST"));
        Log.d(LOG_TAG, "onResume() CommandInterpreterService gebunden");
        final Intent commandInterpreterServiceIntent = new Intent(this, CommandInterpreterService.class);
        bindService(commandInterpreterServiceIntent, commandInterpreterServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void onPause() {
        unbindService(commandInterpreterServiceConnection);
        stopService(new Intent(this, CommandInterpreterService.class));
        Log.d(LOG_TAG, "onResume() CommandServer gestoppt");
        if (motionDetectorReceiver != null) {
            unregisterReceiver(motionDetectorReceiver);
        }
        super.onPause();
    }

    public void turnOnScreen() {
        // turn on screen
        Log.d(LOG_TAG, "turnOnScreen");
        //set the system brightness using the brightness variable value
        android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 255);
        //preview brightness changes at this window
        //get the current window attributes
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        //set the brightness of this window
        layoutParams.screenBrightness = 1;
        //apply attribute changes to this window
        getWindow().setAttributes(layoutParams);
//        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
//        wakeLock.acquire();
    }

    public void turnOffScreen() {
        // turn off screen
        Log.d(LOG_TAG, "turnOffScreen");
        //set the system brightness using the brightness variable value
        android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 0);
        //preview brightness changes at this window
        //get the current window attributes
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        //set the brightness of this window
        layoutParams.screenBrightness = 0f;
        //apply attribute changes to this window
        getWindow().setAttributes(layoutParams);
//        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "tag");
//        wakeLock.acquire();
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        ControlFragment controlFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mainFragment = new MainFragment();
            controlFragment = new ControlFragment();
            ledWallFragment = new LedWallFragment();
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return mainFragment;
            } else if (position == 1) {
                return ledWallFragment;
            }
            return mainFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section_main).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section_ledwall).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section_control).toUpperCase(l);
            }
            return null;
        }
    }

}
