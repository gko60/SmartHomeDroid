package at.htl.smarthome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import at.htl.smarthome.api.HomematicRpcHandler;
import at.htl.smarthome.entity.Settings;
import at.htl.smarthome.view.ControlFragment;
import at.htl.smarthome.view.MainFragment;


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    MainFragment mainFragment;

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
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(addressAsString);
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
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return mainFragment;
            } else if (position == 1) {
                return controlFragment;
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
                    return getString(R.string.title_section_control).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

}
