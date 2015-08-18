package at.htl.smarthome.entity;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Observable;

/**
 * Verwaltung der Einstellungen fuer die Anwendung
 * Serialisierung mittels Parcelable statt ueber Serialization
 */
public class Settings extends Observable {
    public static final String PREFERENCE_FILENAME = "HomematicViewerPreferences";
    private static final String DEFAULT_HOMEMATIC_IP = "10.0.0.50";
    private static Settings instance = null;
    Context context;

    // private Settings settings;
    private String homematicIp;

//    public Settings(String homematicIp) {
//        this.homematicIp = homematicIp;
//    }


    private Settings(Context context) {
        this.context = context;
        readSettingsFromSharedPreferences(context);
    }

    public static Settings getInstance(Context context) {
        if (instance == null) {
            instance = new Settings(context);
        }
        return instance;
    }

    public String getHomematicIp() {
        return homematicIp;
    }

    public void setHomematicIp(String homematicIp) {
        this.homematicIp = homematicIp;
        writeSettingsToSharedPreferences(context);
    }

    /**
     * Settings aus shared Preference auslesen
     *
     * @param context -
     */
    private void readSettingsFromSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                PREFERENCE_FILENAME, Context.MODE_PRIVATE);
        homematicIp = preferences.getString("homematicIp", DEFAULT_HOMEMATIC_IP);
    }

    /**
     * Settings in shared Prefernces speichern.
     *
     * @param context -
     */
    private void writeSettingsToSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
        preferenceEditor.putString("homematicIp", getHomematicIp());
        preferenceEditor.commit();
        setChanged(); // Observable
        this.notifyObservers();
    }
}
