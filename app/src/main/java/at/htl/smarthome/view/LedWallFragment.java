package at.htl.smarthome.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;

import java.util.ArrayList;

import at.htl.smarthome.OnFragmentInteractionListener;
import at.htl.smarthome.R;
import at.htl.smarthome.commandservice.LedWallService;

/**
 * Steuerung der LED-Wall als
 * Beleuchtung mit Helligkeit und Kalt/Warmlicht
 * Farblicht
 * Farblichtwechsler
 * Beliebiger Text
 * Standardtext (Datum, Uhrzeit, Wetterdaten)
 */
public class LedWallFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = LedWallFragment.class.getSimpleName();

    View fragmentView;
    ArrayList<RadioButton> radioButtons;
    private Handler handler;
    private RadioButton radioButtonOut;
    private RadioButton radioButtonColorChanger;
    private RadioButton radioButtonDefaultInfoText;
    private RadioButton radioButtonLedWallText;
    private RadioButton radioButtonLight;
    private RadioButton radioButtonColorPicker;
    private LedWallService ledWallService;
    private EditText etLedWallText;
    private Button buttonOk;
    private ColorPickerView colorPickerView;
    private SeekBar seekBarBrightness;


    private OnFragmentInteractionListener mListener;
    private Runnable updateUI = new Runnable() {
        /**
         * Aktualisierung des Fragments durch Hintergrundthread
         */
        @Override
        public void run() {
            // handler.postDelayed(updateUI, 60000);  // Zyklisch selbst wieder aufrufen ==> Timer
        }
    };

    public LedWallFragment() {
        // Required empty public constructor
    }

    public static LedWallFragment newInstance(String param1, String param2) {
        LedWallFragment fragment = new LedWallFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    /*----------------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_ledwall, container, false);
        // Kick off the first runnable task right away
        radioButtonOut = (RadioButton) fragmentView.findViewById(R.id.radioButtonOut);
        radioButtonColorChanger = (RadioButton) fragmentView.findViewById(R.id.radioButtonColorChanger);
        radioButtonDefaultInfoText = (RadioButton) fragmentView.findViewById(R.id.radioButtonDefaultInfoText);
        radioButtonLedWallText = (RadioButton) fragmentView.findViewById(R.id.radioButtonLedWallText);
        radioButtonLight = (RadioButton) fragmentView.findViewById(R.id.radioButtonLight);
        radioButtonColorPicker = (RadioButton) fragmentView.findViewById(R.id.radioButtonColorPicker);
        radioButtons = new ArrayList<>();
        radioButtons.add(radioButtonOut);
        radioButtons.add(radioButtonColorChanger);
        radioButtons.add(radioButtonDefaultInfoText);
        radioButtons.add(radioButtonLedWallText);
        radioButtons.add(radioButtonLight);
        radioButtons.add(radioButtonColorPicker);
        etLedWallText = (EditText) fragmentView.findViewById(R.id.editTextLedWallText);
        seekBarBrightness = (SeekBar) fragmentView.findViewById(R.id.seekBarBrightness);
        for (RadioButton radioButton : radioButtons) {
            radioButton.setOnClickListener(this);
        }
        buttonOk = (Button) fragmentView.findViewById(R.id.buttonSetLedWall);
        buttonOk.setOnClickListener(this);
        colorPickerView = (ColorPickerView) fragmentView.findViewById(R.id.colorpickerview__color_picker_view);
        ledWallService = LedWallService.getInstance();


        handler.post(updateUI);
        return fragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onClick(View button) {
        if (button == buttonOk) {
            if (radioButtonOut.isChecked()) {
            } else if (radioButtonColorChanger.isChecked()) {
            } else if (radioButtonDefaultInfoText.isChecked()) {
                ledWallService.setInfoCommand();
            } else if (radioButtonLedWallText.isChecked()) {
                String text = etLedWallText.getText().toString();
                ledWallService.setTextCommand(text);
            } else if (radioButtonLight.isChecked()) {
                int brightness = seekBarBrightness.getProgress();
                ledWallService.setLightCommand(brightness);
            } else if (radioButtonColorPicker.isChecked()) {
                int color = colorPickerView.getColor();
                int alpha = Color.alpha(color);
                int red = Color.red(color);
                int blue = Color.blue(color);
                int green = Color.green(color);
                Log.d(LOG_TAG, "Set ColorPicker to A: " + alpha + ", R: " + red + ", G: " + green + ", B: " + blue + " color: " + color);
                ledWallService.setColorPickerCommand(red, green, blue);
            }
        } else {  // Radiobutton wurde ausgewählt
            for (RadioButton radioButton : radioButtons) {
                if (radioButton != button && radioButton.isChecked()) {
                    radioButton.setChecked(false);
                }
            }
        }
    }
}
