package at.htl.smarthome;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;

import at.htl.smarthome.entity.Settings;

public class SettingsActivity extends Activity {
    private static final String LOG_TAG = SettingsActivity.class.getSimpleName();

    EditText editTextHomematicIp;
    Settings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");
        setContentView(R.layout.activity_settings);
        super.onCreate(savedInstanceState);
        settings = Settings.getInstance(this);
        editTextHomematicIp = (EditText) findViewById(R.id.editTextHomematicIp);
        editTextHomematicIp.setText(settings.getHomematicIp());
    }

    /**
     * Save-Button wurde gedrückt. Buch ist über Repository zu speichern.
     *
     * @param view Button
     * @throws java.text.ParseException
     */
    public void onClick(View view) throws ParseException {
        String homematicIp = editTextHomematicIp.getText().toString();
        settings.setHomematicIp(homematicIp);
        finish();  // Beendet Activity
    }


}
