package at.htl.smarthome.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

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
    private Handler handler;
    private RadioButton radioButtonOut;
    private RadioButton radioButtonColorChanger;
    private RadioButton radioButtonDefaultInfoText;
    private RadioButton radioButtonLedWallText;
    private RadioButton radioButtonLight;
    private RadioButton radioButtonColorPicker;
    private LedWallService ledWallService;
    private EditText etLedWallText;

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
        etLedWallText = (EditText) fragmentView.findViewById(R.id.editTextLedWallText);
        radioButtonOut.setOnClickListener(this);
        radioButtonColorChanger.setOnClickListener(this);
        radioButtonDefaultInfoText.setOnClickListener(this);
        radioButtonLedWallText.setOnClickListener(this);
        radioButtonLight.setOnClickListener(this);
        radioButtonColorPicker.setOnClickListener(this);
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
    public void onClick(View radioButton) {
        radioButtonOut.setChecked(false);
        radioButtonColorChanger.setChecked(false);
        radioButtonDefaultInfoText.setChecked(false);
        radioButtonLedWallText.setChecked(false);
        radioButtonLight.setChecked(false);
        if (radioButton == radioButtonOut) {
            radioButtonOut.setChecked(true);
        } else if (radioButton == radioButtonColorChanger) {
            radioButtonColorChanger.setChecked(true);
        } else if (radioButton == radioButtonDefaultInfoText) {
            radioButtonDefaultInfoText.setChecked(true);
            ledWallService.setInfoCommand();
        } else if (radioButton == radioButtonLedWallText) {
            radioButtonLedWallText.setChecked(true);
            String text = etLedWallText.getText().toString();
            ledWallService.setTextCommand(text);
        } else if (radioButton == radioButtonLight) {
            radioButtonLight.setChecked(true);
        } else if (radioButton == radioButtonColorPicker) {
            radioButtonColorPicker.setChecked(true);
        }

    }
}
